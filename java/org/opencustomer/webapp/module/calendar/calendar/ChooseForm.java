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

package org.opencustomer.webapp.module.calendar.calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.FormUtility;

public final class ChooseForm extends org.opencustomer.framework.webapp.action.ChooseForm
{
    private final static Logger log = Logger.getLogger(ChooseForm.class);

    private static final long serialVersionUID = 3258135743162364472L;

    private String userName;

    private String firstName;

    private String lastName;

    private String name;
    
    @Override
    protected void adjustInput()
    {
        userName = FormUtility.adjustParameter(userName);
        firstName = FormUtility.adjustParameter(firstName);
        lastName = FormUtility.adjustParameter(lastName);
        name = FormUtility.adjustParameter(name);
    }

    @Override
    public void validate(ActionMessages errors, HttpServletRequest request)
    {
    }
    
    @Override
    public void reset()
    {
        userName = null;
        firstName = null;
        lastName = null;
        name = null;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
