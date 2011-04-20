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

public final class AddCompanyForm extends ChooseForm
{
    private static final long serialVersionUID = 3257567308586168377L;

    private String companyName;

    @Override
    protected void adjustInput()
    {
        companyName = FormUtility.adjustParameter(companyName);
    }
    
    @Override
    protected void reset()
    {
        companyName = null;
    }
    
    public final String getCompanyName()
    {
        return companyName;
    }

    public final void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
}
