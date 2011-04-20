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
 * Copyright (C) 2006 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Felix Breske <felix.breske@bader-jene.de
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.util.logon;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.opencustomer.connector.ldap.auth.LdapAuthenticator;
import org.opencustomer.connector.ldap.auth.LdapSync;
import org.opencustomer.connector.ldap.auth.LdapUtil;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.util.SignatureUtility;

/**
 * This class handels ldap logins to opencustomer. For normal user the ldap password validation is enabled,
 * admin users uses the normal password validation. If a normal user exists at the ldap direktory and does not exists
 * at the opencustomer database. The user will be created by the LdapSync class.
 * @author fbreske
 *
 */
public final class LdapLogon extends LocalLogon
{
    private static final Logger log = Logger.getLogger(LdapLogon.class); 
    
    /**
     * This method overrides validatePassword from LocalLogin. The method validates the Password for normal user
     * with the ldap server, and for admin user with the OpenCustomer Database
     * Encrypted password validation is not possible for ldap users.
     * For admin user the validatePassword method from the superclass is called.
     * @return true if the password is valid, otherwise false.
     */
    @Override
    protected boolean validatePassword(UserVO user, String clientPassword) {
        if(user.getProfile().getRole().isAdmin()) {
            if(log.isDebugEnabled())
                log.debug("using local password validation for admin user");
            return super.validatePassword(user, clientPassword);
        }
        else {
            if(log.isDebugEnabled())
                log.debug("using ldap password validation for normal user");
            boolean login = LdapAuthenticator.getInstance().isSignatureValid(user.getUserName(),clientPassword);
            if(login)
                user.setPassword(SignatureUtility.getInstance().createSignature(clientPassword));
            return login;
        }          
    }

    /**
     * This method searches for the user for the login. If the user is a normal user, a  ldap syncronisation is started
     * if the user does not exists, a user creation is started.
     * @return the UserVO if the user if found or created, otherwise null.
     */
    @Override
    protected UserVO findUser(String userName) {
        UserVO user = super.findUser(userName);

        if (user == null) {
            try {
                user = LdapUtil.getInstance().createUserFromLdap(userName);
            } catch (NamingException e) {
                log.error("cannot create user from ldap", e);
                user = null;
            }
        } else if (LdapAuthenticator.getInstance().isLdapUser(userName)) {
            try {
                user = LdapSync.getInstance().syncLdapUser(userName);
            } catch (NamingException e) {
                log.error("cannot sync user with ldap Database", e);
                user = null;
            }
        }
        return user;
    }
}
