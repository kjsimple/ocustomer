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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;

/**
 * 
 * @author thbader
 */
public final class LogonForm extends org.opencustomer.framework.webapp.struts.ActionForm
{
    private static final long serialVersionUID = 3904680479067550513L;

    /** Holds value of property login. */
    private String login;

    /** Holds value of property password. */
    private String password;

    /** Holds value of property doLogin. */
    private String doLogin;

    private int id;
    
    private String deeplink;
    
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request) {
        login    = FormUtility.adjustParameter(login, 20);
        password = FormUtility.adjustParameter(password, 20);
        deeplink = FormUtility.adjustParameter(deeplink, 20);

        if (doLogin != null) {
            id       = 0;
            deeplink = null;

            if (login == null)
                errors.add("login", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.user.userName")));
            if (password == null)
                errors.add("password", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.user.password")));
        }

        if(id > 0 && deeplink != null) {
            if(errors.isEmpty())
                doLogin = "DEEP";
        } else {
            id       = 0;
            deeplink = null;
        }
    }

    /**
     * Getter for property login.
     * 
     * @return Value of property login.
     */
    public String getLogin()
    {
        return this.login;
    }

    /**
     * Setter for property login.
     * 
     * @param loginName New value of property login.
     */
    public void setLogin(String login)
    {
        this.login = login;
    }

    /**
     * Getter for property password.
     * 
     * @return Value of property password.
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * Setter for property password.
     * 
     * @param password New value of property password.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Getter for property submit.
     * 
     * @return Value of property submit.
     */
    public String getDoLogin()
    {
        return this.doLogin;
    }

    /**
     * Setter for property submit.
     * 
     * @param submit New value of property submit.
     */
    public void setDoLogin(String doLogin)
    {
        this.doLogin = doLogin;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
