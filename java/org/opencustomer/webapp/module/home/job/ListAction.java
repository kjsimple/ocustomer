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

package org.opencustomer.webapp.module.home.job;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.dao.crm.custom.JobListDAO;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.ScrollBean;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.util.EnumUtility;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.util.configuration.UserConfiguration;
import org.opencustomer.webapp.Globals;

public class ListAction extends org.opencustomer.framework.webapp.action.ListAction<ListForm>
{
    private static Logger log = Logger.getLogger(ListAction.class);

    @Override
    protected Panel createPanel(ListForm form, HttpServletRequest request, HttpServletResponse response)
    {
        Panel panel = new Panel(Panel.Type.LIST);
        panel.setTitle(MessageUtil.message(request, "module.home.job.list.title"));
        panel.setPath("/home/jobs");
        
        return panel;
    }
    
    @Override
    protected void search(Panel panel, ListForm form, boolean formCached, ActionMessages errors, HttpServletRequest request, HttpServletResponse response)
    {
        UserVO user   = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        UserConfiguration conf = (UserConfiguration)request.getSession().getAttribute(Globals.CONFIGURATION_KEY);

        List list = null;
        long count = 0;

        Sort sort = extractSort(panel, form, new Sort(JobListDAO.SORT_DUEDATE, true));
        Page page = new Page(conf.getIntValue(UserConfiguration.Key.LIST_NUMBER_ROWS), form.getPage());

        String paramSubject = form.getSubject();
        boolean paramAssignedUser = form.isAssignedUser();
        JobVO.Status[] paramStatus = new JobVO.Status[] {JobVO.Status.IN_PROGRESS, JobVO.Status.PLANNED};
        if(form.getStatus() != null) {
            paramStatus = new JobVO.Status[]{EnumUtility.valueOf(JobVO.Status.class, form.getStatus())};
        }
        JobVO.Priority paramPriority = EnumUtility.valueOf(JobVO.Priority.class, form.getPriority());

        Date paramDateStart = null;
        Date paramDateEnd = null;
        SimpleDateFormat sdf = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.date"));
        try
        {
            if (form.getDateStart() != null)
                paramDateStart = sdf.parse(form.getDateStart());
            if (form.getDateEnd() != null)
                paramDateEnd = DateUtility.getEndOfDay(sdf.parse(form.getDateEnd()));
        }
        catch (ParseException e)
        {
            log.error("bad form validation", e);
        }
        
        JobListDAO dao = new JobListDAO();

        count = dao.countList(null, null, null, paramSubject, paramStatus, paramPriority, paramDateStart, paramDateEnd, paramAssignedUser, user, null);
        if (count < page.getFirstEntry())
            page.setPage(1);

        if (count > 0) {
            list = dao.getList(null, null, null, paramSubject, paramStatus, paramPriority, paramDateStart, paramDateEnd, paramAssignedUser, user, sort, page, null);
        } else {
            list = new ArrayList();
        }
        
        ScrollBean scroll = new ScrollBean();
        scroll.setCount(count);
        scroll.setPage(page);

        panel.setAttribute(LISTSCROLL_KEY, scroll);
        panel.setAttribute(LIST_KEY, list);
    }
    
    @Override
    protected ActionForward findForward(ActionMapping mapping, ListForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getParameter("id") != null && request.getParameter("deeplink") != null) {
            return mapping.findForward("edit");
        } else {
            return super.findForward(mapping, form, request, response);
        }
    }
}
