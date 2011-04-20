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

package org.opencustomer.webapp.module.system.management.usergroup;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.dao.system.UsergroupDAO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.UsergroupVO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.ScrollBean;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.util.EnumUtility;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.util.configuration.UserConfiguration;
import org.opencustomer.webapp.Globals;

public final class ListAction extends org.opencustomer.framework.webapp.action.ListAction<ListForm>
{
    private static Logger log = Logger.getLogger(ListAction.class);

    @Override
    protected Panel createPanel(ListForm form, HttpServletRequest request, HttpServletResponse response)
    {
        Panel panel = new Panel(Panel.Type.LIST);
        panel.setTitle(MessageUtil.message(request, "module.system.usergroup.showUsergroups.headLine"));
        panel.setPath("/system/management/usergroup");
        
        return panel;
    }
    
    @Override
    protected void search(Panel panel, ListForm form, boolean formCached, ActionMessages errors, HttpServletRequest request, HttpServletResponse response)
    {
        UserVO user = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        UserConfiguration conf = (UserConfiguration)request.getSession().getAttribute(Globals.CONFIGURATION_KEY);

        List<UsergroupVO> list = null;
        long count = 0;

        Sort sort = extractSort(panel, form, new Sort(UsergroupDAO.SORT_NAME, true));
        Page page = new Page(conf.getIntValue(UserConfiguration.Key.LIST_NUMBER_ROWS), form.getPage());

        String paramName = form.getName();
        UsergroupDAO.AdminSelect paramAdmin = EnumUtility.valueOf(UsergroupDAO.AdminSelect.class, form.getAdmin(), UsergroupDAO.AdminSelect.ALL);
                
        UsergroupDAO dao = new UsergroupDAO();

        count = dao.countList(paramName, paramAdmin, user);
        if (count < page.getFirstEntry())
            page.setPage(1);

        if (count > 0)
            list = dao.getList(paramName, paramAdmin, sort, page, user);
        else
            list = new ArrayList<UsergroupVO>();

        ScrollBean scroll = new ScrollBean();
        scroll.setCount(count);
        scroll.setPage(page);

        panel.setAttribute(LISTSCROLL_KEY, scroll);
        panel.setAttribute(LIST_KEY, list);

    }
 }
