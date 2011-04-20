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

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageListForm;

public final class PageContactForm extends EditPageListForm
{
    private static final long serialVersionUID = 3257853160084353849L;

    private static Logger log = Logger.getLogger(PageContactForm.class);

    private String subject;

    private String boundType;

    private String contactType;

    private String contactTimestampStart;

    private String contactTimestampEnd;

    private String name;
    
    @Override
    protected boolean forceValidation()
    {
        return true;
    }
    
    @Override
    protected void adjustInput()
    {
        subject = FormUtility.adjustParameter(subject);
        boundType = FormUtility.adjustParameter(boundType);
        contactType = FormUtility.adjustParameter(contactType);
        contactTimestampStart = FormUtility.adjustParameter(contactTimestampStart);
        contactTimestampEnd = FormUtility.adjustParameter(contactTimestampEnd);
        name = FormUtility.adjustParameter(name);
    }
    
    @Override
    protected void reset()
    {
        subject = null;
        boundType = null;
        contactType = null;
        contactTimestampStart = null;
        contactTimestampEnd = null;
        name = null;
    }
    
    @Override
    public void validate(ActionMessages errors, HttpServletRequest request)
    {
        String format = MessageUtil.message(request, "default.format.input.date");

        if (contactTimestampStart != null)
        {
            try
            {
                contactTimestampStart = DateUtility.adjustDate(format, contactTimestampStart);
            }
            catch (ParseException e)
            {
                errors.add("contactTimestamp", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "module.crm.person.contactTimestamp.dateStart"), format));
            }
        }
        if (contactTimestampEnd != null)
        {
            try
            {
                contactTimestampEnd = DateUtility.adjustDate(format, contactTimestampEnd);
            }
            catch (ParseException e)
            {
                errors.add("contactTimestamp", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "module.crm.person.contactTimestamp.dateEnd"), format));
            }
        }
    }
 
    public final String getBoundType()
    {
        return boundType;
    }

    public final void setBoundType(String boundType)
    {
        this.boundType = boundType;
    }

    public final String getContactType()
    {
        return contactType;
    }

    public final void setContactType(String contactType)
    {
        this.contactType = contactType;
    }

    public final String getSubject()
    {
        return subject;
    }

    public final void setSubject(String subject)
    {
        this.subject = subject;
    }

    public final String getContactTimestampEnd()
    {
        return contactTimestampEnd;
    }

    public final void setContactTimestampEnd(String contactTimestampEnd)
    {
        this.contactTimestampEnd = contactTimestampEnd;
    }

    public final String getContactTimestampStart()
    {
        return contactTimestampStart;
    }

    public final void setContactTimestampStart(String contactTimestampStart)
    {
        this.contactTimestampStart = contactTimestampStart;
    }

    /**
     * @return Returns the name.
     */
    public final String getName()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public final void setName(String name)
    {
        this.name = name;
    }

}
