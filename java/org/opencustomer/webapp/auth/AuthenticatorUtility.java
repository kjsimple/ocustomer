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

package org.opencustomer.webapp.auth;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.opencustomer.webapp.Globals;

public final class AuthenticatorUtility
{
    private static final Logger log = Logger.getLogger(AuthenticatorUtility.class);

    private static AuthenticatorUtility instance;

    private AuthenticatorUtility()
    {
    }

    public static AuthenticatorUtility getInstance()
    {
        if (instance == null)
            instance = new AuthenticatorUtility();

        return instance;
    }

    public boolean authenticate(HttpServletRequest request, Right... rights)
    {
        boolean access = false;

        Authenticator auth = (Authenticator) request.getSession().getAttribute(Globals.AUTHENTICATOR_KEY);
        if (auth == null && rights.length > 0)
        {
            if (log.isDebugEnabled())
                log.debug("access: false (no auth, " + rights.length + " rights)");
            access = false;
        }
        else if (auth == null || rights.length == 0)
        {
            if (log.isDebugEnabled())
                log.debug("access: true (no auth, " + rights.length + " rights)");
            access = true;
        }
        else if (auth.isValid(rights))
        {
            if (log.isDebugEnabled())
                log.debug("access: true (validation successful)");
            access = true;
        }
        else
        {
            if (log.isDebugEnabled())
                log.debug("access: false (using default)");
        }

        return access;
    }
    
    public static boolean isLoggedIn(HttpServletRequest request) {
        if(request.getSession().getAttribute(Globals.AUTHENTICATOR_KEY) != null)
            return true;
        else
            return false;
    }
    
    public static Authenticator getAuthenticator(HttpServletRequest request) {
        return (Authenticator)request.getSession().getAttribute(Globals.AUTHENTICATOR_KEY);
    }
}
