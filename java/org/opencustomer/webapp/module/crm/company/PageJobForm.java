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

public final class PageJobForm extends EditPageListForm
{
    private static final long serialVersionUID = 3257853160084353849L;

    private static Logger log = Logger.getLogger(PageJobForm.class);

    private String subject;

    private String priority;

    private String status;

    private String dateStart;

    private String dateEnd;
    
    @Override
    protected boolean forceValidation()
    {
        return true;
    }
    
    @Override
    protected void adjustInput()
    {
        subject = FormUtility.adjustParameter(subject);
        priority = FormUtility.adjustParameter(priority);
        status = FormUtility.adjustParameter(status);
        dateStart = FormUtility.adjustParameter(dateStart);
        dateEnd = FormUtility.adjustParameter(dateEnd);
    }
    
    @Override
    protected void reset()
    {
        subject   = null;
        priority  = null;
        status    = null;
        dateStart = null;
        dateEnd   = null;
    }
    
    @Override
    public void validate(ActionMessages errors, HttpServletRequest request)
    {
        if(log.isDebugEnabled())
            log.debug("validate search form");
        
        String format = MessageUtil.message(request, "default.format.input.date");

        if (dateStart != null)
        {
            try
            {
                dateStart = DateUtility.adjustDate(format, dateStart);
            }
            catch (ParseException e)
            {
                errors.add("dateStart", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "default.date.dateStart"), format));
            }
        }
        if (dateEnd != null)
        {
            try
            {
                dateEnd = DateUtility.adjustDate(format, dateEnd);
            }
            catch (ParseException e)
            {
                errors.add("dateEnd", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "default.date.dateEnd"), format));
            }
        }
    }

    public String getDateEnd()
    {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd)
    {
        this.dateEnd = dateEnd;
    }

    public String getDateStart()
    {
        return dateStart;
    }

    public void setDateStart(String dateStart)
    {
        this.dateStart = dateStart;
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
    

}
