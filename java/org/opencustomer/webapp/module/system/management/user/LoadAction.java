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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.dao.system.UserLoginLogDAO;
import org.opencustomer.db.vo.system.ProfileVO;
import org.opencustomer.db.vo.system.UserLoginLogVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.util.LocaleUtility;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditLoadAction;
import org.opencustomer.webapp.auth.Right;
import org.opencustomer.webapp.module.crm.company.DeleteAction;

public final class LoadAction extends EditLoadAction<LoadForm>
{
    private static Logger log = Logger.getLogger(LoadAction.class);

    @Override
    public EditPanel createPanel(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);

        UserVO user = (UserVO)attributes.get("user");

        boolean isActiveUser = false;
        if(user.equals(activeUser))
            isActiveUser = true;   
        boolean isLdapUser =  user.getProfile().getLdapGroup() != null;
       
        
        EditPanel panel = new EditPanel(Right.ADMINISTRATION_USERMANAGEMENT_WRITE, user);
        
        if(user.getId() == null)
            panel.setTitle(MessageUtil.message(request, "module.system.user.manage.headLine.create"));
        else
        {
            panel.setTitle(MessageUtil.message(request, "module.system.user.manage.headLine.edit", user.getUserName()));

            panel.setAttribute(DeleteAction.TEXT_TITLE, MessageUtil.message(request, "module.system.user.manage.deleteUser.headLine", user.getUserName()));
            panel.setAttribute(DeleteAction.TEXT_QUESTION, MessageUtil.message(request, "module.system.user.manage.deleteUser.question", user.getUserName()));
        }
        
        panel.addAction(Action.Type.DELETE, "/system/management/user/delete", !isActiveUser&&!isLdapUser);
        panel.addAction(Action.Type.SAVE, "/system/management/user/save");
        
        panel.addPage("STANDARD", "/system/management/user/pageStandard", "module.generic.panel.tab.standard");
        panel.addPage("SYSTEM", "/system/management/user/pageSystem", "module.generic.panel.tab.system");
        
        panel.setAttribute("isActiveUser", isActiveUser);
        panel.setAttribute("isLdapUser", isLdapUser);
        
        return panel;
    }
    
    @Override
    public void loadEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            UserVO user = new UserDAO().getById(form.getId());
            attributes.put("user", user);
            
            UserLoginLogDAO dao = new UserLoginLogDAO();
            UserLoginLogVO lastValidLogin = dao.getLastValidLoginForUser(user);
            if(lastValidLogin != null)
                attributes.put("lastValidLogin", lastValidLogin);
            UserLoginLogVO lastInvalidLogin = dao.getLastInvalidLoginForUser(user);
            if(lastInvalidLogin != null)
                attributes.put("lastInvalidLogin", lastInvalidLogin);
            attributes.put("invalidLoginCount", dao.countInvalidLoginsForUser(user));

        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));

            log.error("could not load user with id: " + form.getId(), e);
        }
    }
    
    @Override
    public void createEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        
        UserVO user = new UserVO();
        
        ProfileVO profile = new ProfileVO();
        profile.setAccessUser(EntityAccess.Access.WRITE_SYSTEM);
        profile.setOwnerUser(activeUser.getId());
        profile.setAccessGroup(EntityAccess.Access.NONE);
        profile.setOwnerGroup(activeUser.getProfile().getDefaultUsergroup().getId());
        profile.setAccessGlobal(EntityAccess.Access.WRITE_SYSTEM);
        user.setProfile(profile);

        user.getProfile().setTimeLock(SystemConfiguration.getInstance().getIntValue(SystemConfiguration.Key.DEFAULT_TIMELOCK));
        user.getProfile().setIpPattern(SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.DEFAULT_IPPATTERN));
        user.setLocale(activeUser.getLocale());
        
        user.setAccessUser(EntityAccess.Access.WRITE_SYSTEM);
        user.setOwnerUser(activeUser.getId());
        user.setAccessGroup(EntityAccess.Access.NONE);
        user.setOwnerGroup(activeUser.getProfile().getDefaultUsergroup().getId());
        user.setAccessGlobal(EntityAccess.Access.WRITE_SYSTEM);

        attributes.put("user", user);
    }

    @Override
    public void loadEnvironment(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);

        List<LocaleUtility.LocaleBean> locales = new ArrayList<LocaleUtility.LocaleBean>();
        for(Locale locale : LocaleUtility.getInstance().getLocales()) {
            locales.add(new LocaleUtility.LocaleBean(locale, MessageUtil.message(request, "locale."+locale)));
        }
        
        Collections.sort(locales);
        
        attributes.put("locales", locales);        
    }
}
