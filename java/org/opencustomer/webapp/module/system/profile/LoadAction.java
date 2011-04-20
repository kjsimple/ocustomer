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

package org.opencustomer.webapp.module.system.profile;

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
import org.opencustomer.db.vo.system.UserLoginLogVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.util.LocaleUtility;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditLoadAction;
import org.opencustomer.webapp.action.EditLoadForm;
import org.opencustomer.webapp.auth.Right;

public class LoadAction extends EditLoadAction<EditLoadForm>
{
    private static Logger log = Logger.getLogger(LoadAction.class);

    @Override
    public EditPanel createPanel(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UserVO user = (UserVO)attributes.get("user");

        EditPanel panel = new EditPanel(Right.ADMINISTRATION_USERPROFILE_WRITE, user);
        
        panel.setTitle(MessageUtil.message(request, "module.system.user.profile.default.title"));
        
        panel.addAction(Action.Type.SAVE, "/system/profile/save");
        
        panel.addPage("STANDARD", "/system/profile/pageStandard", "module.generic.panel.tab.standard");
        
        return panel;
    }
    
    @Override
    public void loadEntity(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UserVO activeUser = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        
        try
        {
            UserVO user = new UserDAO().getById(activeUser.getId());
            attributes.put("user", user);
            
            UserLoginLogDAO dao = new UserLoginLogDAO();
            UserLoginLogVO lastInvalidLogin = dao.getLastInvalidLoginForUser(user);
            if(lastInvalidLogin != null)
                attributes.put("lastInvalidLogin", lastInvalidLogin);
        }
        catch (HibernateException e)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));

            log.error("could not load user with id: " + form.getId(), e);
        }
    }
    
    @Override
    public void createEntity(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        loadEntity(errors, form, attributes, request, response);
    }
    
    @Override
    public void loadEnvironment(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        List<LocaleUtility.LocaleBean> locales = new ArrayList<LocaleUtility.LocaleBean>();
        for(Locale locale : LocaleUtility.getInstance().getLocales()) {
            locales.add(new LocaleUtility.LocaleBean(locale, MessageUtil.message(request, "locale."+locale)));
        }
        
        Collections.sort(locales);
        
        attributes.put("locales", locales);
    }
}
