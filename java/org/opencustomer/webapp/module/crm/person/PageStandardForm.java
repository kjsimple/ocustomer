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

package org.opencustomer.webapp.module.crm.person;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.ImageButtonBean;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.util.validator.EmailAddressValidator;
import org.opencustomer.framework.util.validator.URLValidator;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageStandardForm extends EditPageForm
{
    private static final long serialVersionUID = 3256437002093016884L;

    private String firstName;

    private String lastName;

    private String gender;

    private String nameAffix;

    private String title;

    private String email;

    private String phone;

    private String fax;

    private String mobile;

    private String comment;

    private String degree;

    private String function;
    
    private String dayOfBirth;
    
    private String type;

    private String url;
    
    private ImageButtonBean doRemoveCompany = new ImageButtonBean();
    
    private ImageButtonBean doAddCompany = new ImageButtonBean();

    private ImageButtonBean doJumpCompany = new ImageButtonBean();

    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        type     = FormUtility.adjustParameter(type);
        
        firstName  = FormUtility.adjustParameter(firstName, 255);
        lastName   = FormUtility.adjustParameter(lastName, 255);
        gender     = FormUtility.adjustParameter(gender);
        nameAffix  = FormUtility.adjustParameter(nameAffix, 255);
        title      = FormUtility.adjustParameter(title, 255);
        dayOfBirth = FormUtility.adjustParameter(dayOfBirth);

        email  = FormUtility.adjustParameter(email, 255);
        phone  = FormUtility.adjustParameter(phone, 50);
        fax    = FormUtility.adjustParameter(fax, 50);
        mobile = FormUtility.adjustParameter(mobile, 50);
        url    = FormUtility.adjustParameter(url, 255);

        comment = FormUtility.adjustParameter(comment);

        degree   = FormUtility.adjustParameter(degree, 255);
        function = FormUtility.adjustParameter(function, 255);
        
        if (dayOfBirth != null) {
            String format = MessageUtil.message(request, "default.format.input.date");
            try {
                dayOfBirth = DateUtility.adjustDate(format, dayOfBirth);
            } catch (ParseException e) {
                errors.add("dayOfBirth", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "entity.crm.person.dayOfBirth"), format));
            }
        }
        
        if(email !=  null && !EmailAddressValidator.getInstance().validate(email)) {
            errors.add("email", new ActionMessage("default.error.invalidFormat.email", MessageUtil.message(request, "entity.crm.person.email")));
        }

        if(url !=  null && !URLValidator.getInstance().validate(url)) {
            errors.add("url", new ActionMessage("default.error.invalidFormat.url", MessageUtil.message(request, "entity.crm.person.url")));
        }

    }

    public final String getComment()
    {
        return comment;
    }

    public final void setComment(String comment)
    {
        this.comment = comment;
    }

    public final String getDegree()
    {
        return degree;
    }

    public final void setDegree(String degree)
    {
        this.degree = degree;
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

    public final String getFunction()
    {
        return function;
    }

    public final void setFunction(String function)
    {
        this.function = function;
    }

    public final String getGender()
    {
        return gender;
    }

    public final void setGender(String gender)
    {
        this.gender = gender;
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

    public final ImageButtonBean getDoAddCompany()
    {
        return doAddCompany;
    }

    public final void setDoAddCompany(ImageButtonBean doAddCompany)
    {
        this.doAddCompany = doAddCompany;
    }

    public final ImageButtonBean getDoJumpCompany()
    {
        return doJumpCompany;
    }

    public final void setDoJumpCompany(ImageButtonBean doJumpCompany)
    {
        this.doJumpCompany = doJumpCompany;
    }

    public final String getNameAffix()
    {
        return nameAffix;
    }

    public final void setNameAffix(String nameAffix)
    {
        this.nameAffix = nameAffix;
    }

    public final String getTitle()
    {
        return title;
    }

    public final void setTitle(String title)
    {
        this.title = title;
    }

    public ImageButtonBean getDoRemoveCompany()
    {
        return doRemoveCompany;
    }

    public void setDoRemoveCompany(ImageButtonBean doRemoveCompany)
    {
        this.doRemoveCompany = doRemoveCompany;
    }

    public final String getDayOfBirth()
    {
        return dayOfBirth;
    }
    
    public final void setDayOfBirth(String dayOfBirth)
    {
        this.dayOfBirth = dayOfBirth;
    }

    public final String getType()
    {
        return type;
    }

    public final void setType(String type)
    {
        this.type = type;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
    
}
