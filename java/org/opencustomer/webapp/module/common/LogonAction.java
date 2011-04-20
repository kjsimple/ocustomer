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

package org.opencustomer.webapp.module.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.connector.ldap.auth.LdapUtil;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.webapp.struts.Action;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.util.configuration.UserConfiguration;
import org.opencustomer.util.logon.LdapLogon;
import org.opencustomer.util.logon.LocalLogon;
import org.opencustomer.util.logon.Logon;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.auth.Authenticator;
import org.opencustomer.webapp.auth.AuthenticatorUtility;
import org.opencustomer.webapp.util.menu.Menu;
import org.opencustomer.webapp.util.menu.MenuFactory;
import org.opencustomer.webapp.util.menu.MenuItem;

/**
 * 
 * @author thbader
 */
public final class LogonAction extends Action<LogonForm>
{
    private static Logger log = Logger.getLogger(LogonAction.class);

    private static final int LOGIN_NOK = 0;

    private static final int LOGIN_OK = 1;

    /**
     * Method which have to be overwritten to handle the request.
     * 
     * @param servlet The ActionServlet instance owning this Action
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The servlet request we are processing
     * @param response The servlet response we are processing
     * @param log the log to save messages
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward execute(ActionMapping mapping, LogonForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        ActionMessages errors = new ActionMessages();

        int forward = LOGIN_NOK;

        if (AuthenticatorUtility.isLoggedIn(request)) {
            if (log.isInfoEnabled())
                log.info("user session is already active ... login is not necessary");

            forward = LOGIN_OK;
        } else if (form.getDoLogin() != null) {   
            Logon logon;
            
            if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_AUTHENTICATION_ENABLED)){
                if(log.isDebugEnabled())
                    log.debug("do local login");
                logon = new LocalLogon();
            } else {
                if(log.isDebugEnabled())
                    log.debug("do ldap login");
                logon = new LdapLogon();          
            }
            
            UserVO user = logon.validate(form.getLogin(), form.getPassword(), request.getRemoteAddr(), Logon.Type.WEBAPP, errors);
            
            if((user == null || !errors.isEmpty()) && SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_AUTHENTICATION_ENABLED) && !LdapUtil.getInstance().testLdapConnection()) {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.common.login.error.noLdapConnection"));
            }
            
            if(errors.isEmpty()) {
                request.getSession().setAttribute(Globals.USER_KEY, user);
                request.getSession().setAttribute(Globals.AUTHENTICATOR_KEY, new Authenticator(user));
                request.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY, user.getLocale());
                request.getSession().setAttribute(Globals.CONFIGURATION_KEY, new UserConfiguration(user));
                
                MenuFactory menuFactory = (MenuFactory)request.getSession().getServletContext().getAttribute(Globals.MENU_FACTORY_KEY);
                Menu menu = menuFactory.getCustomizedMenu(AuthenticatorUtility.getAuthenticator(request));
                request.getSession().setAttribute(Globals.MENU_KEY, menu);

                forward = LOGIN_OK;
            }
        }

        if (!errors.isEmpty())
            saveErrors(request, errors);

        if (forward == LOGIN_OK) {
            Menu menu     = (Menu) request.getSession().getAttribute(Globals.MENU_KEY);
            
            if(form.getDeeplink() != null) {
                MenuItem item = null;
                
                if(log.isDebugEnabled())
                    log.debug("execute deeplink");

                if("EVENT".equals(form.getDeeplink())) {
                    if(log.isDebugEnabled()) {
                        log.debug("prepare deeplink to event with id: "+form.getId());
                    }
                    item   = menu.findItem("/calendar/showMainCalendar");
                } else if("JOB".equals(form.getDeeplink())) {
                    if(log.isDebugEnabled()) {
                        log.debug("prepare deeplink to job with id: "+form.getId());
                    }
                    item   = menu.findItem("/home/jobs");
                }

                if(item != null) {
                    if(log.isDebugEnabled())
                        log.debug("active menu item with id: "+item.getId());
                        
                    menu.activate(item.getId());
                }
            }

            return new ActionForward(getActionURL(request, menu.getActiveItem().getAction()));
        }  else {
            return mapping.getInputForward();
        }
    }

    private String getActionURL(HttpServletRequest request, String path)
    {
        String pattern = (String) request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.SERVLET_KEY);

        StringBuilder url = new StringBuilder();

        if (pattern.startsWith("*."))
        {
            url.append(path);
            url.append(pattern.substring(1));

        }
        else if (pattern.endsWith("/*"))
        {
            url.append(pattern.substring(0, pattern.length() - 2));
            url.append(path);

        }
        else if (pattern.equals("/"))
        {
            url.append(path);
        }

        return url.toString();
    }
}
