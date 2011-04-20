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

package org.opencustomer.connector.ldap.auth;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;
import org.opencustomer.util.configuration.SystemConfiguration;

/**
 * The class LdapAuthenticator handels the user authentication with a ldap server.
 * @author fbreske
 *
 */
public class LdapAuthenticator
{
    private static final Logger log = Logger.getLogger(LdapAuthenticator.class);
    
    private static LdapAuthenticator instance;
    
    /**
     * 
     * @return instance of LdapAuthenticator
     */
    public static LdapAuthenticator getInstance() {
        if( instance == null)
            instance = new LdapAuthenticator();
        
        return instance;
    }
    
    /**
     * Checks if the user is a ldap user.
     * The DN consists of "uid=" + user + LdapSettings.USER_PREFIX + LdapSettings.BASE_DN.
     * @param user the user on the ldap server
     * @return true if the user is a ldap user, if not false
     */
    public boolean isLdapUser(String user)
    {
        Hashtable<String,String> env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL,"ldap://" + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_SERVER) + ":" + SystemConfiguration.getInstance().getIntValue(SystemConfiguration.Key.LDAP_PORT) + "/" + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_USER_PREFIX) + "," + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_BASE_DN));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");    
        env.put(Context.SECURITY_PRINCIPAL, SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADMIN_USER));
        env.put(Context.SECURITY_CREDENTIALS, SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADMIN_PASSWORD));
        try
        {
            InitialDirContext ctx = new InitialDirContext(env);
            ctx.getAttributes("uid=" + user);
            return true;
        }
        catch (NamingException e)
        {
            if(log.isDebugEnabled())
                log.debug("no ldap user for username: " + user);
            return false;
        }  
    }
    
    /**
     * Checks if the password of the user is correct.
     * The DN consists of "uid=" + user + LdapSettings.USER_PREFIX + LdapSettings.BASE_DN.
     * @param user
     * @param password
     * @return true if the password is correct, otherwise false.
     */
    public boolean isSignatureValid(String user, String password)
    {
        user = "uid=" + user + "," + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_BASE_DN);
        Hashtable<String,String> env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL,"ldap://" + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_SERVER) + ":" + SystemConfiguration.getInstance().getIntValue(SystemConfiguration.Key.LDAP_PORT) + "/" + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_USER_PREFIX) + "," + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_BASE_DN));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");    
        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, password);
        try
        {
            InitialDirContext ctx = new InitialDirContext(env);
            return true;
        }
        catch (NamingException e)
        {
            if(log.isDebugEnabled())
                log.debug("ldap password not valid for user: " + user);
            return false;
        }      
    }
}
