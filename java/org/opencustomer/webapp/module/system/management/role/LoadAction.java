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

package org.opencustomer.webapp.module.system.management.role;

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
import org.opencustomer.db.dao.system.RightAreaDAO;
import org.opencustomer.db.dao.system.RoleDAO;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.system.RoleVO;
import org.opencustomer.db.vo.system.UserVO;
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
        RoleVO role = (RoleVO)attributes.get("role");
        List<UserVO> users = (List<UserVO>)attributes.get("users");

        boolean deletable = false;
        if(users.isEmpty())
            deletable = true;

        EditPanel panel = new EditPanel(Right.ADMINISTRATION_ROLE_WRITE, role);
        
        if(role.getId() == null)
            panel.setTitle(MessageUtil.message(request, "module.system.role.headLine.create"));
        else
        {
            panel.setTitle(MessageUtil.message(request, "module.system.role.headLine.edit", role.getName()));

            panel.setAttribute(DeleteAction.TEXT_TITLE, MessageUtil.message(request, "module.system.role.delete.headLine", role.getName()));
            panel.setAttribute(DeleteAction.TEXT_QUESTION, MessageUtil.message(request, "module.system.role.delete.question", role.getName()));

        }
        
        panel.addAction(Action.Type.DELETE, "/system/management/role/delete", deletable);
        panel.addAction(Action.Type.SAVE, "/system/management/role/save");
        
        panel.addPage("STANDARD", "/system/management/role/pageStandard", "module.generic.panel.tab.standard");
        panel.addPage("RIGHT", "/system/management/role/pageRights", "module.system.role.pageRights");
        panel.addPage("SYSTEM", "/system/management/role/pageSystem", "module.generic.panel.tab.system");
        
        return panel;
    }
    
    @Override
    public void createEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);

        RoleVO role = new RoleVO();
        
        role.setAccessUser(EntityAccess.Access.WRITE_SYSTEM);
        role.setOwnerUser(activeUser.getId());
        role.setAccessGroup(EntityAccess.Access.NONE);
        role.setOwnerGroup(activeUser.getProfile().getDefaultUsergroup().getId());
        role.setAccessGlobal(EntityAccess.Access.WRITE_SYSTEM);
        
        List<UserVO> users = new ArrayList<UserVO>();

        attributes.put("role", role);
        attributes.put("users", users);
    }
    
    @Override
    public void loadEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            RoleVO role = new RoleDAO().getById(form.getId());
            List<UserVO> users = new UserDAO().getForRole(role);
            
            attributes.put("role", role);
            attributes.put("users", users);
        }
        catch (HibernateException e)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));

            log.error("could not load role for id: " + form.getId());
        }
    }
    
    @Override
    public void loadEnvironment(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        attributes.put("rightAreas", new RightAreaDAO().getAll());
    }
}
