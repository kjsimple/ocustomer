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

package org.opencustomer.webapp.module.generic.add;

import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.action.ChooseForm;

public final class AddUserForm extends ChooseForm
{
    private static final long serialVersionUID = 3689345529142458675L;

    private String firstName;

    private String lastName;

    private String userName;
    
    @Override
    protected void adjustInput() {
        firstName = FormUtility.adjustParameter(firstName);
        lastName  = FormUtility.adjustParameter(lastName);
        userName  = FormUtility.adjustParameter(userName);
    }

    @Override
    protected void reset() {
        firstName = null;
        lastName  = null;
        userName  = null;
    }
    
    /**
     * @return Returns the firstName.
     */
    public final String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The firstName to set.
     */
    public final void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return Returns the lastName.
     */
    public final String getLastName() {
        return lastName;
    }

    /**
     * @param lastName The lastName to set.
     */
    public final void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public final String getUserName() {
        return userName;
    }

    public final void setUserName(String userName) {
        this.userName = userName;
    }

}
