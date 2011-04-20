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

package org.opencustomer.webapp.module.crm.job;

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
import org.opencustomer.webapp.action.EditPageForm;

public final class PageStandardForm extends EditPageForm
{
    private static final long serialVersionUID = 3257571706599061552L;

    private static Logger log = Logger.getLogger(PageStandardForm.class);

    private String subject;

    private String info;

    private String dueDate;

    private String priority;

    private String status;

    private ImageButtonBean doAddReferencedPerson = new ImageButtonBean();

    private ImageButtonBean doAddReferencedCompany = new ImageButtonBean();

    private ImageButtonBean doRemoveReference = new ImageButtonBean();

    private ImageButtonBean doJumpReference = new ImageButtonBean();
    
    private ImageButtonBean doAddUser = new ImageButtonBean();
    
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        subject = FormUtility.adjustParameter(subject, 255);
        info = FormUtility.adjustParameter(info);
        dueDate = FormUtility.adjustParameter(dueDate);
        priority = FormUtility.adjustParameter(priority);
        status = FormUtility.adjustParameter(status);

        if (subject == null) {
            errors.add("subject", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.job.subject")));
        }
        
        if (dueDate != null)
        {
            String format = MessageUtil.message(request, "default.format.input.dateTime");
            try
            {
                dueDate = DateUtility.adjustDate(format, dueDate);
            }
            catch (ParseException e)
            {
                errors.add("dueDate", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "entity.crm.job.dueDate"), format));
            }
        }
    }

    public void reset()
    {
        subject = null;
        info = null;
        dueDate = null;
        priority = null;
        status = null;
    }

    public String getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public ImageButtonBean getDoAddReferencedCompany()
    {
        return doAddReferencedCompany;
    }

    public void setDoAddReferencedCompany(ImageButtonBean doAddReferencedCompany)
    {
        this.doAddReferencedCompany = doAddReferencedCompany;
    }

    public ImageButtonBean getDoAddReferencedPerson()
    {
        return doAddReferencedPerson;
    }

    public void setDoAddReferencedPerson(ImageButtonBean doAddReferencedPerson)
    {
        this.doAddReferencedPerson = doAddReferencedPerson;
    }

    public ImageButtonBean getDoRemoveReference()
    {
        return doRemoveReference;
    }

    public void setDoRemoveReference(ImageButtonBean doRemoveReference)
    {
        this.doRemoveReference = doRemoveReference;
    }

    public ImageButtonBean getDoAddUser()
    {
        return doAddUser;
    }

    public void setDoAddUser(ImageButtonBean doAddUser)
    {
        this.doAddUser = doAddUser;
    }

    public ImageButtonBean getDoJumpReference()
    {
        return doJumpReference;
    }

    public void setDoJumpReference(ImageButtonBean doJumpReference)
    {
        this.doJumpReference = doJumpReference;
    }
}
