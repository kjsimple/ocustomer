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

package org.opencustomer.webapp.module.system.management.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.ScrollBean;
import org.opencustomer.framework.db.util.Sort;
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
        panel.setTitle(MessageUtil.message(request, "module.system.user.manage.showUsers.headLine"));
        panel.setPath("/system/management/user");
        
        return panel;
    }
    
    @Override
    protected void search(Panel panel, ListForm form, boolean formCached, ActionMessages errors, HttpServletRequest request, HttpServletResponse response)
    {
        UserVO user = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        UserConfiguration conf = (UserConfiguration)request.getSession().getAttribute(Globals.CONFIGURATION_KEY);

        List<UserVO> list = null;
        long count = 0;

        Sort sort = extractSort(panel, form, new Sort(UserDAO.SORT_USERNAME, true));
        Page page = new Page(conf.getIntValue(UserConfiguration.Key.LIST_NUMBER_ROWS), form.getPage());

        String paramUserName = form.getUserName();
        Boolean paramLocked = null;
        if (form.getLocked() != null)
            paramLocked = Boolean.parseBoolean(form.getLocked());

        Date paramLastLoginStart = null;
        Date paramLastLoginEnd = null;
        String format = MessageUtil.message(request, "default.format.input.date");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try
        {
            if (form.getLastLoginStart() != null)
                paramLastLoginStart = sdf.parse(form.getLastLoginStart());
            if (form.getLastLoginEnd() != null)
            {
                paramLastLoginEnd = sdf.parse(form.getLastLoginEnd());
                Calendar cal = GregorianCalendar.getInstance();
                cal.setTime(paramLastLoginEnd);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.SECOND, -1);
                paramLastLoginEnd = cal.getTime();
            }
        }
        catch (ParseException e)
        {
            log.error("bad form validation", e);
        }

        UserDAO dao = new UserDAO();

        count = dao.countList(paramUserName, paramLocked, user);
        if (count < page.getFirstEntry())
            page.setPage(1);

        if (count > 0)
            list = dao.getList(paramUserName, paramLocked, sort, page, user);
        else
            list = new ArrayList<UserVO>();

        ScrollBean scroll = new ScrollBean();
        scroll.setCount(count);
        scroll.setPage(page);

        panel.setAttribute(LISTSCROLL_KEY, scroll);
        panel.setAttribute(LIST_KEY, list);
    }


}
