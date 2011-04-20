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

package org.opencustomer.webapp.module.crm.contact;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.ImageButtonBean;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.framework.webapp.util.RequestUtility;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageStandardForm extends EditPageForm
{
    private static final long serialVersionUID = 3257571706599061552L;

    private static Logger log = Logger.getLogger(PageStandardForm.class);

    private String subject;

    private String content;

    private String contactName;

    private String contactType;

    private String boundType;

    private String contactTime;

    private int personId;

    private int companyId;

    private int jumpPersonId;

    private int doRemovePerson;

    private ImageButtonBean doAddPerson = new ImageButtonBean();

    private ImageButtonBean doJumpCompany = new ImageButtonBean();

    private ImageButtonBean doAddOwnPerson = new ImageButtonBean();

    private ImageButtonBean doAddCompany = new ImageButtonBean();

    private ImageButtonBean doRemoveCompany = new ImageButtonBean();
    
    @Override
    protected void analyzeRequest(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        jumpPersonId = RequestUtility.parseParamId(request, "doJumpPerson_");
        doRemovePerson = RequestUtility.parseParamId(request, "doRemovePerson_");
    }

    @Override
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        subject = FormUtility.adjustParameter(subject, 255);
        content = FormUtility.adjustParameter(content);
        contactName = FormUtility.adjustParameter(contactName, 255);
        contactType = FormUtility.adjustParameter(contactType);
        boundType = FormUtility.adjustParameter(boundType);
        contactTime = FormUtility.adjustParameter(contactTime);

        if (contactTime == null)
        {
            errors.add("contactTime", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.contact.contactTimestamp")));
        }
        else
        {
            String format = MessageUtil.message(request, "default.format.input.dateTime");
            try
            {
                contactTime = DateUtility.adjustDate(format, contactTime);
            }
            catch (ParseException e)
            {
                errors.add("contactTime", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "entity.crm.contact.contactTimestamp"), format));
            }
        }
    }

    public void reset()
    {
        subject = null;
        content = null;
        contactName = null;
        contactType = null;
        boundType = null;
        contactTime = null;
        personId = 0;
        companyId = 0;
    }

    public final String getBoundType()
    {
        return boundType;
    }

    public final void setBoundType(String boundType)
    {
        this.boundType = boundType;
    }

    public final int getCompanyId()
    {
        return companyId;
    }

    public final void setCompanyId(int companyId)
    {
        this.companyId = companyId;
    }

    public final String getContactName()
    {
        return contactName;
    }

    public final void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public final String getContactTime()
    {
        return contactTime;
    }

    public final void setContactTime(String contactTime)
    {
        this.contactTime = contactTime;
    }

    public final String getContactType()
    {
        return contactType;
    }

    public final void setContactType(String contactType)
    {
        this.contactType = contactType;
    }

    public final String getContent()
    {
        return content;
    }

    public final void setContent(String content)
    {
        this.content = content;
    }

    public final int getPersonId()
    {
        return personId;
    }

    public final void setPersonId(int personId)
    {
        this.personId = personId;
    }

    public final String getSubject()
    {
        return subject;
    }

    public final void setSubject(String subject)
    {
        this.subject = subject;
    }

    public final ImageButtonBean getDoJumpCompany()
    {
        return doJumpCompany;
    }

    public final void setDoJumpCompany(ImageButtonBean doJumpCompany)
    {
        this.doJumpCompany = doJumpCompany;
    }

    /**
     * @return Returns the doAddPerson.
     */
    public final ImageButtonBean getDoAddPerson()
    {
        return doAddPerson;
    }

    /**
     * @param doAddPerson The doAddPerson to set.
     */
    public final void setDoAddPerson(ImageButtonBean doAddPerson)
    {
        this.doAddPerson = doAddPerson;
    }

    /**
     * @return Returns the jumpPersonId.
     */
    public final int getJumpPersonId()
    {
        return jumpPersonId;
    }

    /**
     * @param jumpPersonId The jumpPersonId to set.
     */
    public final void setJumpPersonId(int jumpPersonId)
    {
        this.jumpPersonId = jumpPersonId;
    }

    /**
     * @return Returns the deletePersonId.
     */
    public final int getDoRemovePerson()
    {
        return doRemovePerson;
    }

    /**
     * @param deletePersonId The deletePersonId to set.
     */
    public final void setDoRemovePerson(int doRemovePerson)
    {
        this.doRemovePerson = doRemovePerson;
    }

    /**
     * @return Returns the doAddOwnPerson.
     */
    public final ImageButtonBean getDoAddOwnPerson()
    {
        return doAddOwnPerson;
    }

    /**
     * @param doAddOwnPerson The doAddOwnPerson to set.
     */
    public final void setDoAddOwnPerson(ImageButtonBean doAddOwnPerson)
    {
        this.doAddOwnPerson = doAddOwnPerson;
    }

    /**
     * @return Returns the doAddCompany.
     */
    public final ImageButtonBean getDoAddCompany()
    {
        return doAddCompany;
    }

    /**
     * @param doAddCompany The doAddCompany to set.
     */
    public final void setDoAddCompany(ImageButtonBean doAddCompany)
    {
        this.doAddCompany = doAddCompany;
    }

    public ImageButtonBean getDoRemoveCompany()
    {
        return doRemoveCompany;
    }

    public void setDoRemoveCompany(ImageButtonBean doRemoveCompany)
    {
        this.doRemoveCompany = doRemoveCompany;
    }

}
