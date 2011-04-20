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
 * Copyright (C) 2006 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.db.vo.crm.custom;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.db.vo.crm.JobVO;


public class JobListVO
{
    private static final long serialVersionUID = -2957177393182714565L;

    private Integer jobId;
    
    private String subject;
    
    private String info;
    
    private Date dueDate;
    
    private JobVO.Priority priority;
    
    private JobVO.Status status;
    
    private String assignedUserName;
    
    private String reference;


    public Integer getJobId()
    {
        return jobId;
    }


    public void setJobId(Integer jobId)
    {
        this.jobId = jobId;
    }


    public String getAssignedUserName()
    {
        return assignedUserName;
    }


    public void setAssignedUserName(String assignedUserName)
    {
        this.assignedUserName = assignedUserName;
    }


    public Date getDueDate()
    {
        return dueDate;
    }


    public void setDueDate(Date dueDate)
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


    public JobVO.Priority getPriority()
    {
        return priority;
    }


    public void setPriority(JobVO.Priority priority)
    {
        this.priority = priority;
    }


    public JobVO.Status getStatus()
    {
        return status;
    }


    public void setStatus(JobVO.Status status)
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


    public String getReference()
    {
        return reference;
    }


    public void setReference(String reference)
    {
        this.reference = reference;
    }


    @Override
    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("subject", subject);
        builder.append("info", info);
        builder.append("dueDate", dueDate);
        builder.append("priority", priority);
        builder.append("status", status);
        builder.append("assignedUserName", assignedUserName);
        builder.append("reference", reference);
        
        return builder.toString();
    }
}
