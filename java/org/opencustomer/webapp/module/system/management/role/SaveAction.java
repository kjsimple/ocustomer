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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.RoleDAO;
import org.opencustomer.db.vo.system.RoleVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditSaveAction;

public class SaveAction extends EditSaveAction
{
    private static Logger log = Logger.getLogger(SaveAction.class);

    @Override
    protected void saveEntity(EditPanel panel, ActionMessages errors, HttpServletRequest request)
    {
        RoleVO role = (RoleVO) panel.getEntity();
        UserVO user = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        
        try
        {
            RoleDAO dao = new RoleDAO();
            if (role.getId() == null)
            {
                if (log.isDebugEnabled())
                    log.debug("create role");
                dao.insert(role, user);
            }
            else
            {
                if (log.isDebugEnabled())
                    log.debug("save role (ID:" + role.getId() + ")");
                dao.update(role, user);
            }
        }
        catch (HibernateException e)
        {
            log.error("could not save role", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.rollback"));
        }
    }
    
    @Override
    protected boolean validateData(EditPanel panel, ActionMessages errors, HttpServletRequest request)
    {
        RoleVO role = (RoleVO) panel.getEntity();

        if (role.getName() != null)
        {
            try
            {
                RoleDAO dao = new RoleDAO();

                RoleVO editRole = dao.getByName(role.getName());
                if (editRole != null && !editRole.getId().equals(role.getId())) {
                    if(editRole.isAdmin()) 
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.system.role.error.nameExistsInAdmin"));
                    else
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.system.role.error.nameExists"));
                }

                HibernateContext.getSession().evict(editRole);
            }
            catch (HibernateException e)
            {
                log.error("could not read role for name: " + role.getName());
            }
        }
        
        if(role.getDefaultUsergroup() == null) {
            errors.add("defaultUsergroup", new ActionMessage("module.system.role.error.missingDefaultUsergroup"));
        } else {
            if(role.isAdmin() && !role.getDefaultUsergroup().isAdmin()) {
                errors.add("defaultUsergroup", new ActionMessage("module.system.role.error.invalidUsergroupAdminNeeded"));
            } else if (!role.isAdmin() && role.getDefaultUsergroup().isAdmin()) {
                errors.add("defaultUsergroup", new ActionMessage("module.system.role.error.invalidUsergroupNotAdminNeeded"));
            }
        }

        if (errors.isEmpty())
            return true;
        else
            return false;
    }
    
    @Override
    protected ActionForward getActionForward(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getAttribute("external_user_id") != null)
            return mapping.findForward("jumpUser");
        else if(request.getAttribute("external_usergroup_id") != null)
            return mapping.findForward("jumpUsergroup");
        else
            return null;
    }
}