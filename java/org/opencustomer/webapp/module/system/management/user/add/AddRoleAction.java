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

package org.opencustomer.webapp.module.system.management.user.add;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.RoleDAO;
import org.opencustomer.db.dao.system.UsergroupDAO;
import org.opencustomer.db.vo.system.RoleVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.ScrollBean;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.webapp.action.ChooseAction;
import org.opencustomer.framework.webapp.panel.EntityPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.util.configuration.UserConfiguration;
import org.opencustomer.webapp.Globals;

public final class AddRoleAction extends ChooseAction<AddRoleForm>
{
    private static Logger log = Logger.getLogger(AddRoleAction.class);

    @Override
    protected final EntityPanel createPanel(EntityPanel mainPanel, AddRoleForm form, HttpServletRequest request, HttpServletResponse response)
    {
        EntityPanel panel = new EntityPanel(Panel.Type.CHOOSE, mainPanel.getEntity());
        panel.setTitle(MessageUtil.message(request, "module.generic.add.usergroup.title"));
        panel.setPath("/system/management/user/addRole");
        
        form.reset();
        
        return panel;
    }
    

    @Override
    protected void chooseEntity(EntityPanel panel, AddRoleForm form, ActionMessages errors, HttpServletRequest request, HttpServletResponse response)
    {
        if (log.isDebugEnabled())
            log.debug("add role with ID: " + form.getId());

        try
        {
            UserVO user = (UserVO) panel.getEntity();

            RoleVO role = new RoleDAO().getById(form.getId());
            
            if(user.getProfile().getLdapGroup() == null && user.getProfile().getRole() == null || user.getProfile().getRole().isAdmin() != role.isAdmin()) {
                user.getProfile().getUsergroups().clear();
                user.getProfile().getUsergroups().add(role.getDefaultUsergroup());
                user.getProfile().setDefaultUsergroup(role.getDefaultUsergroup());
            }
            
            user.getProfile().setRole(role);
            
            if(role.isAdmin())
                user.setPerson(null);
        }
        catch (HibernateException e)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));

            log.error("problems loading role (ID:" + form.getId() + ")");
        }
    }
    
    @Override
    protected final void search(EntityPanel panel, AddRoleForm form, ActionMessages errors, HttpServletRequest request, HttpServletResponse response)
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        UserConfiguration conf = (UserConfiguration)request.getSession().getAttribute(Globals.CONFIGURATION_KEY);

        List<RoleVO> list = null;
        long count = 0;

        Sort sort = extractSort(panel, form, UsergroupDAO.SORT_NAME);

        Page page = new Page(conf.getIntValue(UserConfiguration.Key.LIST_NUMBER_ROWS), form.getPage());

        String paramName = form.getName();

        RoleDAO.AdminSelect adminSelect = RoleDAO.AdminSelect.NOT_ADMIN;
        if(activeUser.getProfile().getRole().isAdmin())
            adminSelect = RoleDAO.AdminSelect.ALL;
        
        RoleDAO dao = new RoleDAO();
        count = dao.countList(paramName, adminSelect, activeUser);
        if (count < page.getFirstEntry())
            page.setPage(1);

        if (count > 0)
            list = dao.getList(paramName, adminSelect, sort, page, activeUser);
        else
            list = new ArrayList<RoleVO>();

        ScrollBean scroll = new ScrollBean();
        scroll.setCount(count);
        scroll.setPage(page);

        panel.setAttribute(LISTSCROLL_KEY, scroll);
        panel.setAttribute(LIST_KEY, list);
    }
    
}
