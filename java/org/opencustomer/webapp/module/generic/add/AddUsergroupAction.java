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

package org.opencustomer.webapp.module.generic.add;

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
import org.opencustomer.framework.webapp.action.ChooseAction;
import org.opencustomer.framework.webapp.panel.EntityPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.util.configuration.UserConfiguration;
import org.opencustomer.webapp.Globals;

public abstract class AddUsergroupAction<E extends AddUsergroupForm> extends ChooseAction<E>
{
    private static Logger log = Logger.getLogger(AddUsergroupAction.class);

    @Override
    protected final EntityPanel createPanel(EntityPanel mainPanel, E form, HttpServletRequest request, HttpServletResponse response)
    {
        EntityPanel panel = new EntityPanel(Panel.Type.CHOOSE, mainPanel.getEntity());
        panel.setTitle(MessageUtil.message(request, "module.generic.add.usergroup.title"));
        panel.setPath(getPath());
        
        form.reset();
        
        return panel;
    }
    
    protected abstract String getPath();
    
    protected abstract boolean isAdmin(EntityPanel panel);
    
    @Override
    protected final void search(EntityPanel panel, E form, ActionMessages errors, HttpServletRequest request, HttpServletResponse response)
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        UserConfiguration conf = (UserConfiguration)request.getSession().getAttribute(Globals.CONFIGURATION_KEY);

        List<UsergroupVO> list = null;
        long count = 0;

        Sort sort = extractSort(panel, form, UsergroupDAO.SORT_NAME);

        Page page = new Page(conf.getIntValue(UserConfiguration.Key.LIST_NUMBER_ROWS), form.getPage());

        String paramName = form.getName();

        UsergroupDAO.AdminSelect adminSelect = UsergroupDAO.AdminSelect.NOT_ADMIN;
        if(isAdmin(panel))
            adminSelect = UsergroupDAO.AdminSelect.ADMIN;
        
        UsergroupDAO dao = new UsergroupDAO();
        count = dao.countList(paramName, adminSelect, activeUser);
        if (count < page.getFirstEntry())
            page.setPage(1);

        if (count > 0)
            list = dao.getList(paramName, adminSelect, sort, page, activeUser);
        else
            list = new ArrayList<UsergroupVO>();

        ScrollBean scroll = new ScrollBean();
        scroll.setCount(count);
        scroll.setPage(page);

        panel.setAttribute(LISTSCROLL_KEY, scroll);
        panel.setAttribute(LIST_KEY, list);
    }
    
}
