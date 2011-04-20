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

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.system.LdapGroupVO;
import org.opencustomer.db.vo.system.UserVO;

/**
 * The class LdapSync handels the synconisation of the ldap directory and the OpenCustomer database.
 * @author fbreske
 *
 */
public class LdapSync
{
    private static final Logger log = Logger.getLogger(LdapUtil.class);
    
    private static LdapSync INSTANCE = new LdapSync();
    
    /**
     * 
     * @return instance of LdapSync
     */
    public static LdapSync getInstance()
    {
        return INSTANCE;
    }

    /**
     * This method syncronise an local user with the corresponding ldap user.
     * The method checks if the user exists and if the user is member of the correct group.
     * @param username the username to check
     * @return the UserVO of the updated username, or null if the user not exists.
     * @throws NamingException on ldap error
     */
    public UserVO syncLdapUser(String username) throws NamingException
    {
        if(log.isDebugEnabled())
            log.debug("sync user: " + username);
           
        UserVO user = new UserDAO().getByUserName(username);
        LdapGroupVO profile = LdapUtil.getInstance().getProfileForUser(username);
        
        if(user == null && profile == null)
        {
            if(log.isDebugEnabled())
                log.debug("ignoring new user without ldapgroups");
        }
        else if(user == null)
        {
            if(log.isDebugEnabled())
                log.debug("creating new user");
            LdapUtil.getInstance().createUserFromLdap(username); 
        }
        else if(user.getProfile().getRole().isAdmin())
        {
            if(log.isDebugEnabled())
                log.debug("user is admin, do nothing");
        }
        else if(user.getProfile().getLdapGroup() != null && !LdapAuthenticator.getInstance().isLdapUser(username))
        {
            new UserDAO().delete(user);
            user = null;
            if(log.isDebugEnabled())
                log.debug("oc user deleted: " + username);
        }
        else if((user.getProfile().getRole().isAdmin() || (user.getProfile().getLdapGroup() == null))&& LdapAuthenticator.getInstance().isLdapUser(username))
        {
            log.error("username conflict: " +  username);
            user = null;
        }
        else if(profile == null)
        {
            if(log.isDebugEnabled())
                log.debug("no ldapgroup for user, deleting user");
            new UserDAO().delete(user);
            user = null;
        }
        else if(user.getProfile().getId() != profile.getProfile().getId())
        {
            if(log.isDebugEnabled())
                log.debug("ldap group change");
            user.setProfile(profile.getProfile());
        }
        else
        {
            if(log.isDebugEnabled())
                log.debug("no changes for user: " + username);
        } 
        return user;
    }
}
