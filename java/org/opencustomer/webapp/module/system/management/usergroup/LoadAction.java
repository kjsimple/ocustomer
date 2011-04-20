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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.RoleDAO;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.dao.system.UsergroupDAO;
import org.opencustomer.db.vo.system.RoleVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.UsergroupVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditLoadAction;
import org.opencustomer.webapp.auth.Right;
import org.opencustomer.webapp.module.crm.company.DeleteAction;

public class LoadAction extends EditLoadAction<LoadForm>
{
    private static Logger log = Logger.getLogger(LoadAction.class);

    @Override
    public EditPanel createPanel(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UsergroupVO usergroup = (UsergroupVO)attributes.get("usergroup");
        List<UserVO> users = (List<UserVO>)attributes.get("users");
        List<RoleVO> roles = (List<RoleVO>)attributes.get("roles");

        boolean deletable = false;
        if(users.isEmpty() && roles.isEmpty())
            deletable = true;

        EditPanel panel = new EditPanel(Right.ADMINISTRATION_USERGROUP_WRITE, usergroup);
        
        if(usergroup.getId() == null)
            panel.setTitle(MessageUtil.message(request, "module.system.usergroup.headLine.create"));
        else
        {
            panel.setTitle(MessageUtil.message(request, "module.system.usergroup.headLine.edit", usergroup.getName()));

            panel.setAttribute(DeleteAction.TEXT_TITLE, MessageUtil.message(request, "module.system.usergroup.delete.headLine", usergroup.getName()));
            panel.setAttribute(DeleteAction.TEXT_QUESTION, MessageUtil.message(request, "module.system.usergroup.delete.question", usergroup.getName()));
        }
        
        panel.addAction(Action.Type.DELETE, "/system/management/usergroup/delete", deletable);
        panel.addAction(Action.Type.SAVE, "/system/management/usergroup/save");
        
        panel.addPage("STANDARD", "/system/management/usergroup/pageStandard", "module.generic.panel.tab.standard");
        panel.addPage("SYSTEM", "/system/management/usergroup/pageSystem", "module.generic.panel.tab.system");
        
        return panel;
    }
    
    @Override
    public void loadEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug("load usergroup (ID:" + form.getId() + ")");

        try
        {
            UsergroupVO usergroup = new UsergroupDAO().getById(form.getId());

            attributes.put("usergroup", usergroup);
            attributes.put("users", new UserDAO().getForUsergroup(usergroup));
            attributes.put("roles", new RoleDAO().getForDefaultUsergroup(usergroup));
        }
        catch (HibernateException e)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));

            log.debug("problems usergroup role (ID:" + form.getId() + ")");
        }
    }
    
    @Override
    public void createEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);

        UsergroupVO usergroup = new UsergroupVO();
        
        usergroup.setAccessUser(EntityAccess.Access.WRITE_SYSTEM);
        usergroup.setOwnerUser(activeUser.getId());
        usergroup.setAccessGroup(EntityAccess.Access.NONE);
        usergroup.setOwnerGroup(activeUser.getProfile().getDefaultUsergroup().getId());
        usergroup.setAccessGlobal(EntityAccess.Access.WRITE_SYSTEM);

        attributes.put("usergroup", usergroup);
        attributes.put("users", new ArrayList<UserVO>());
        attributes.put("roles", new ArrayList<RoleVO>());
    }

}
