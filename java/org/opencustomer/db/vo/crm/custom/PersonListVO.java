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

package org.opencustomer.db.vo.crm.custom;

import org.opencustomer.db.vo.crm.PersonVO;

public final class PersonListVO
{
    private Integer personId;

    private String firstName;

    private String lastName;

    private String phone;

    private String fax;

    private String mobile;

    private String email;

    private String companyName;
    
    private PersonVO.Type type;

    private boolean user;

    private Integer userId;
    
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

    public final String getFirstName()
    {
        return firstName;
    }

    public final void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public final String getLastName()
    {
        return lastName;
    }

    public final void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public final String getMobile()
    {
        return mobile;
    }

    public final void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public final String getPhone()
    {
        return phone;
    }

    public final void setPhone(String phone)
    {
        this.phone = phone;
    }

    public final boolean isUser()
    {
        return user;
    }

    public final void setUser(boolean user)
    {
        this.user = user;
    }

    public final Integer getPersonId()
    {
        return personId;
    }

    public final void setPersonId(Integer personId)
    {
        this.personId = personId;
    }

    public final Integer getUserId()
    {
        return userId;
    }

    public final void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public PersonVO.Type getType()
    {
        return type;
    }

    public void setType(PersonVO.Type type)
    {
        this.type = type;
    }    
}
