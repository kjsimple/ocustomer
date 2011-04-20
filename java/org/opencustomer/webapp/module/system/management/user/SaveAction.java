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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.calendar.CalendarDAO;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditSaveAction;

public final class SaveAction extends EditSaveAction
{
    private static Logger log = Logger.getLogger(SaveAction.class);

    @Override
    protected void saveEntity(EditPanel panel, ActionMessages errors, HttpServletRequest request)
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);

        try
        {
            UserVO saveUser = (UserVO) panel.getEntity();

            HibernateContext.beginTransaction();
            
            if (saveUser.getId() == null)
            {
                if (log.isDebugEnabled())
                    log.debug("create user " + saveUser);

                if(saveUser.getLocale() == null)
                    saveUser.setLocale(activeUser.getLocale());

                new UserDAO().insert(saveUser, activeUser);

                CalendarVO calendar = new CalendarVO();
                calendar.setUser(saveUser);
                calendar.setAccessUser(EntityAccess.Access.WRITE_SYSTEM);
                calendar.setOwnerUser(saveUser.getId());
                calendar.setAccessGroup(EntityAccess.Access.NONE);
                calendar.setOwnerGroup(saveUser.getProfile().getDefaultUsergroup().getId());
                calendar.setAccessGlobal(EntityAccess.Access.WRITE_SYSTEM);
                
                new CalendarDAO().insert(calendar, activeUser);
            }
            else
            {
                if (log.isDebugEnabled())
                    log.debug("save user " + saveUser);
                new UserDAO().update(saveUser, activeUser);
            }
            
            HibernateContext.commitTransaction();
        }
        catch (HibernateException e)
        {
            HibernateContext.rollbackTransaction();

            log.error("could not save user", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.rollback"));
        }
    }

    @Override
    protected boolean validateData(EditPanel panel, ActionMessages errors, HttpServletRequest request)
    {
        UserVO user = (UserVO) panel.getEntity();

        if (user.getUserName() == null)
            errors.add("userName", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.user.userName")));
        else
        {
            try
            {
                UserDAO dao = new UserDAO();

                UserVO editUser = dao.getByUserName(user.getUserName());
                if (editUser != null && !editUser.getId().equals(user.getId())) {
                    if(editUser.getProfile().getRole().isAdmin())
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.system.user.manage.error.nameExistsInAdmin", MessageUtil.message(request, "entity.system.user.userName")));
                    else
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.system.user.manage.error.nameExists", MessageUtil.message(request, "entity.system.user.userName")));
                }

                HibernateContext.getSession().clear();
            }
            catch (HibernateException e)
            {
                log.error("could not read user for name: " + user.getUserName());
            }
        }

        if (user.getProfile().getRole() == null)
            errors.add("role", new ActionMessage("module.system.user.manage.error.roleNeeded", MessageUtil.message(request, "entity.system.role")));

        if (user.getProfile().getUsergroups().size() == 0)
            errors.add("usergroup", new ActionMessage("module.system.user.manage.error.usergroupNeeded", MessageUtil.message(request, "entity.system.usergroup")));
        else if (user.getProfile().getDefaultUsergroup() == null)
            errors.add("mainUsergroupId", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.profile.defaultUsergroup")));

        if (errors.isEmpty())
            return true;
        else
            return false;
    }   

    @Override
    protected ActionForward getActionForward(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getAttribute("external_usergroup_id") != null)
            return mapping.findForward("jumpUsergroup");
        else if(request.getAttribute("external_role_id") != null)
            return mapping.findForward("jumpRole");
        else
            return null;
    }
}
