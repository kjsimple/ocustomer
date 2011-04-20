/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is the OpenCustomer CRM.
 * 
 * The Initial Developer of the Original Code is Thomas Bader (Bader & Jene
 * Software-Ingenieurbüro). Portions created by the Initial Developer are
 * Copyright (C) 2005 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.webapp.module.calendar;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.dao.calendar.CalendarDAO;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.webapp.action.CommonAction;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.module.calendar.util.CalendarView;

public class ShowMonthAction extends CommonAction<ShowMonthForm>
{
    private static Logger log = Logger.getLogger(ShowMonthAction.class);
    
    @Override
    protected Panel createPanel(ShowMonthForm form, HttpServletRequest request, HttpServletResponse response)
    {
        Panel panel = new Panel(Panel.Type.LIST);
        panel.setTitle(MessageUtil.message(request, "module.calendar.showCalendar.month.headLine"));
        panel.setPath("/calendar/showMonth");
        
        return panel;
    }
    
    @Override
    protected void execute(Panel panel, ShowMonthForm form, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response)
    {
        UserVO activeUser   = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        boolean update = true;
        
        // Prüfe ob schon ein Standardkalender besteht
        CalendarVO calendar = (CalendarVO) panel.getAttribute("calendar");

        ActionMessages errors = getErrors(request);
        if(errors.isEmpty()) {
            if (form.getId() > 0) {
                calendar = new CalendarDAO().getById(form.getId());
                if (!EntityAccessUtility.isAccessGranted(activeUser, calendar, EntityAccess.Access.READ)) {
                    log.debug("user tries to access an invalid calendar");
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", calendar.getId()));
                    calendar = null;
                    
                    // refresh the user
                    UserVO user = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
                    user = new UserDAO().getById(user.getId());
                    if(user != null) {
                        request.getSession().setAttribute(Globals.USER_KEY, user);
                    } else {
                        log.info("could not reload user (may be deleted)");
                    }
                } else {
                    if (log.isDebugEnabled())
                        log.debug("use calendar: " + calendar);
                }
            } 
            
            if (form.isMain() || calendar == null) {
                calendar = new CalendarDAO().getForUser(activeUser);
                if (log.isDebugEnabled())
                    log.debug("use calendar: " + calendar);
            }
        } else if (log.isDebugEnabled()) {
            log.debug("found errors > use old calendar: " + calendar);
        }
        
        String title = MessageUtil.message(request, "module.calendar.showCalendar.month.headLine")+" - ";
        if(calendar.getUser().equals(activeUser)) {
            title += MessageUtil.message(request, "module.calendar.personalCalendar");
        } else {
            title += calendar.getUser().getUserName();
        }
        panel.setTitle(title);

        
        // update calendar
        if (!calendar.equals(panel.getAttribute("calendar"))) {
            if (log.isDebugEnabled())
                log.debug("update calendar");
            panel.removeAttribute("calendar");
            panel.removeAttribute("calendarView");
        }

        panel.setAttribute("calendar", calendar);

        CalendarView calendarView = (CalendarView) panel.getAttribute("calendarView");
        if (calendarView == null)
        {
            Date referenceDate = new Date();
            if (panel.getAttribute("reference_day") != null)
                referenceDate = (Date) panel.getAttribute("reference_day");

            calendarView = new CalendarView(calendar, referenceDate, CalendarView.Period.MONTH, activeUser);
            panel.setAttribute("calendarView", calendarView);
            update = false;
        }

        if (form.isDoLastMonth())
        {
            if (log.isDebugEnabled())
                log.debug("go to last month");

            java.util.Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(calendarView.getReferenceDate());
            if (cal.get(java.util.Calendar.MONTH) == java.util.Calendar.JANUARY)
                cal.roll(java.util.Calendar.YEAR, -1);
            cal.roll(java.util.Calendar.MONTH, -1);
            calendarView.setReferenceDate(cal.getTime());

            panel.setAttribute("reference_day", cal.getTime());
        }
        else if (form.isDoNextMonth())
        {
            if (log.isDebugEnabled())
                log.debug("go to next month");

            java.util.Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(calendarView.getReferenceDate());
            if (cal.get(java.util.Calendar.MONTH) == java.util.Calendar.DECEMBER)
                cal.roll(java.util.Calendar.YEAR, +1);
            cal.roll(java.util.Calendar.MONTH, +1);
            calendarView.setReferenceDate(cal.getTime());

            panel.setAttribute("reference_day", cal.getTime());
        }
        else if (update)
        {
            calendarView.update();
        }
        
        if(!errors.isEmpty())
            saveErrors(request, errors);
    }
    
    @Override
    protected ActionForward findForward(ActionMapping mapping, ShowMonthForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getParameter("id") != null && request.getParameter("deeplink") != null) {
            return mapping.findForward("edit");
        } else {
            return super.findForward(mapping, form, request, response);
        }
    }
}
