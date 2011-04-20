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

package org.opencustomer.webapp.module.crm.company;

import org.opencustomer.framework.util.FormUtility;

public final class ListForm extends org.opencustomer.framework.webapp.action.ListForm
{
    private static final long serialVersionUID = 4049923748826067250L;

    private String companyName;

    private String phone;

    private String fax;

    private String url;

    private String email;

    @Override
    protected void adjustInput()
    {
        companyName = FormUtility.adjustParameter(companyName);
        url = FormUtility.adjustParameter(url);
        phone = FormUtility.adjustParameter(phone);
        fax = FormUtility.adjustParameter(fax);
        email = FormUtility.adjustParameter(email);
    }
    
    @Override
    public void reset()
    {
        companyName = null;
        url = null;
        phone = null;
        fax = null;
        email = null;
    }
    
    public final String getCompanyName()
    {
        return companyName;
    }

    public final void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public final String getEmail()
    {
        return email;
    }

    public final void setEmail(String email)
    {
        this.email = email;
    }

    public final String getFax()
    {
        return fax;
    }

    public final void setFax(String fax)
    {
        this.fax = fax;
    }

    public final String getPhone()
    {
        return phone;
    }

    public final void setPhone(String phone)
    {
        this.phone = phone;
    }

    public final String getUrl()
    {
        return url;
    }

    public final void setUrl(String url)
    {
        this.url = url;
    }
}
