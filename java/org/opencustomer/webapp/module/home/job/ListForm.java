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

package org.opencustomer.webapp.module.home.job;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;

public final class ListForm extends org.opencustomer.framework.webapp.action.ListForm
{
    private static final long serialVersionUID = 3258135743162364472L;

    private String subject;

    private String priority;

    private String status;

    private String dateStart;

    private String dateEnd;
    
    private boolean assignedUser = true;

    @Override
    protected void adjustInput()
    {
        subject   = FormUtility.adjustParameter(subject);
        priority  = FormUtility.adjustParameter(priority);
        status    = FormUtility.adjustParameter(status);
        dateStart = FormUtility.adjustParameter(dateStart);
        dateEnd   = FormUtility.adjustParameter(dateEnd);
    }
    
    @Override
    public void validate(ActionMessages errors, HttpServletRequest request)
    {
        String format = MessageUtil.message(request, "default.format.input.date");

        if (dateStart != null) {
            try {
                dateStart = DateUtility.adjustDate(format, dateStart);
            } catch (ParseException e) {
                errors.add("dateStart", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "default.date.dateStart"), format));
            }
        }
        if (dateEnd != null) {
            try {
                dateEnd = DateUtility.adjustDate(format, dateEnd);
            } catch (ParseException e) {
                errors.add("dateEnd", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "default.date.dateEnd"), format));
            }
        }
    }
    
    @Override
    protected void reset() 
    {
        subject   = null;
        priority  = null;
        status    = null;
        dateStart = null;
        dateEnd   = null;
        assignedUser = true;
    }

    public final String getDateEnd()
    {
        return dateEnd;
    }
    

    public final void setDateEnd(String dateEnd)
    {
        this.dateEnd = dateEnd;
    }
    

    public final String getDateStart()
    {
        return dateStart;
    }
    

    public final void setDateStart(String dateStart)
    {
        this.dateStart = dateStart;
    }
    

    public final String getPriority()
    {
        return priority;
    }
    

    public final void setPriority(String priority)
    {
        this.priority = priority;
    }
    

    public final String getStatus()
    {
        return status;
    }
    

    public final void setStatus(String status)
    {
        this.status = status;
    }
    

    public final String getSubject()
    {
        return subject;
    }
    

    public final void setSubject(String subject)
    {
        this.subject = subject;
    }

    public boolean isAssignedUser()
    {
        return assignedUser;
    }

    public void setAssignedUser(boolean assignedUser)
    {
        this.assignedUser = assignedUser;
    }
    
}
