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
 *                 Felix Breske <felix.breske@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.connector.webdav;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.slide.simple.authentication.SessionAuthenticationManager;
import org.apache.struts.action.ActionErrors;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.util.SignatureUtility;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.util.logon.LdapLogon;
import org.opencustomer.util.logon.LocalLogon;
import org.opencustomer.util.logon.Logon;
import org.opencustomer.webapp.auth.Authenticator;
import org.opencustomer.webapp.auth.Right;

/**
 * The class WebdavSessionManager manages the webdav user sessions. The user will be authentificated against the
 * OpenCustomer user database. This class implements the SessionAutenticationManager
 * 
 * @author fbreske
 *
 */
public class WebdavSessionManager implements SessionAuthenticationManager {

    private static Logger log = Logger.getLogger(WebdavSessionManager.class);
    private HashMap<String,String> userMap = new HashMap<String,String>();
    
    /**
     * @param user 
     * @return a saved session
     */
    public Object getAuthenticationSession(String user) 
    {
        return userMap.get(user);
    }

    /**
     *  authenticaes the user against the userdatabase with username and password,
     *  and saves session if the autenticaion will be successfull.
     *  @return the session or null if the authenification failed
     *  @param user username
     *  @param password password
     */
    public Object getAuthenticationSession(String user, String password) {
        Logon logon;
        
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_AUTHENTICATION_ENABLED)){
            if(log.isDebugEnabled())
                log.debug("do local login");
            logon = new LocalLogon();
        }     
        else
        {
            if(log.isDebugEnabled())
                log.debug("do ldap login");
            logon = new LdapLogon();          
        }
        
        ActionErrors errors = new ActionErrors();
        UserVO uservo = logon.validate(user, password, Logon.Type.WEBDAV, errors);
        
        if(uservo != null && errors.isEmpty())
        {
            Authenticator auth = new Authenticator(uservo);
            if(auth.isValid(Right.EXTERN_WEBDAV_READ, Right.EXTERN_WEBDAV_WRITE) 
                    && SignatureUtility.getInstance().isSignatureValid(uservo.getPassword(),password))
            {
                if(log.isInfoEnabled())
                    log.info("webdav session for user [" + user + "]");
                String session = user+":"+password;
                userMap.put(user, session);
                return session;
            }
            if(log.isInfoEnabled())
                log.info("webdav access denied for username [" + user + "]");
        }
        else
        {
            if(log.isInfoEnabled())
                log.info("user not found");
        }
            
        return null;
    }

    public void closeAuthenticationSession(Object session) {  }
}
