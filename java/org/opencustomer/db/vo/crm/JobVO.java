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

package org.opencustomer.db.vo.crm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.db.UserAssigned;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.db.vo.VersionedVO;

@Entity
@Table(name = "job")
@AttributeOverride(name = "id", column = @Column(name = "job_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class JobVO extends VersionedVO implements EntityAccess, UserAssigned
{
    private static final long serialVersionUID = -2957177393182714565L;

    public static enum Priority {
        LOW,
        MEDIUM,
        HIGH;
    }

    public static enum Status {
        PLANNED,
        IN_PROGRESS,
        COMPLETED;
    }

    private String subject;
    
    private String info;
    
    private Date dueDate;
    
    private Priority priority;
    
    private Status status;
    
    private UserVO assignedUser;
    
    private PersonVO referencedPerson;
    
    private CompanyVO referencedCompany;
    
    private Set<ContactVO> contacts = new HashSet<ContactVO>();
    
    private JobVO parentJob;
    
    private Set<JobVO> childJobs = new HashSet<JobVO>();
    
    private Integer ownerUser;

    private Integer ownerGroup;

    private Access accessUser;

    private Access accessGroup;

    private Access accessGlobal;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    public UserVO getAssignedUser()
    {
        return assignedUser;
    }

    public void setAssignedUser(UserVO assignedUser)
    {
        this.assignedUser = assignedUser;
    }

    @Column(name = "info")
    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    @Column(name = "due_date")
    public Date getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    public Priority getPriority()
    {
        return priority;
    }

    public void setPriority(Priority priority)
    {
        this.priority = priority;
    }

    @ManyToOne
    @JoinColumn(name = "referenced_company_id")
    public CompanyVO getReferencedCompany()
    {
        return referencedCompany;
    }

    public void setReferencedCompany(CompanyVO referencedCompany)
    {
        this.referencedCompany = referencedCompany;
    }

    @ManyToOne
    @JoinColumn(name = "referenced_person_id")
    public PersonVO getReferencedPerson()
    {
        return referencedPerson;
    }

    public void setReferencedPerson(PersonVO referencedPerson)
    {        
        this.referencedPerson = referencedPerson;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    @Column(name = "subject")
    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    @OneToMany(mappedBy = "job")
    public Set<ContactVO> getContacts()
    {
        return contacts;
    }
    
    public void setContacts(Set<ContactVO> contacts)
    {
        this.contacts = contacts;
    }

    @Column(name = "owner_user")
    public Integer getOwnerUser()
    {
        return ownerUser;
    }

    public void setOwnerUser(Integer ownerUser)
    {
        this.ownerUser = ownerUser;
    }

    @Column(name = "owner_group")
    public Integer getOwnerGroup()
    {
        return ownerGroup;
    }

    public void setOwnerGroup(Integer ownerGroup)
    {
        this.ownerGroup = ownerGroup;
    }

    @Column(name = "access_user")
    @Enumerated(EnumType.STRING)
    public Access getAccessUser()
    {
        return accessUser;
    }

    public void setAccessUser(Access accessUser)
    {
        this.accessUser = accessUser;
    }

    @Column(name = "access_group")
    @Enumerated(EnumType.STRING)
    public Access getAccessGroup()
    {
        return accessGroup;
    }

    public void setAccessGroup(Access accessGroup)
    {
        this.accessGroup = accessGroup;
    }
    
    @Column(name = "access_global")
    @Enumerated(EnumType.STRING)
    public Access getAccessGlobal()
    {
        return accessGlobal;
    }

    public void setAccessGlobal(Access accessGlobal)
    {
        this.accessGlobal = accessGlobal;
    }

    @OneToMany(mappedBy = "parentJob")
    public Set<JobVO> getChildJobs()
    {
        return childJobs;
    }

    public void setChildJobs(Set<JobVO> childJobs)
    {
        this.childJobs = childJobs;
    }

    @ManyToOne
    @JoinColumn(name = "parent_job_id")
    public JobVO getParentJob()
    {
        return parentJob;
    }

    public void setParentJob(JobVO parentJob)
    {
        this.parentJob = parentJob;
    }

    @Override
    protected void toString(ToStringBuilder builder)
    {
        builder.append("subject", subject);
        builder.append("info", info);
        builder.append("dueDate", dueDate);
        builder.append("priority", priority);
        builder.append("status", status);
        builder.append("assignedUser", assignedUser == null ? "null" : assignedUser.getId());
        builder.append("referencedPerson", referencedPerson == null ? "null" : referencedPerson.getId());
        builder.append("referencedCompany", referencedCompany == null ? "null" : referencedCompany.getId());
        builder.append("parentJob", parentJob == null ? "null" : parentJob.getId());
        builder.append("ownerUser", ownerUser);
        builder.append("ownerGroup", ownerGroup);
        builder.append("accessUser", accessUser);
        builder.append("accessGroup", accessGroup);
        builder.append("accessGlobal", accessGlobal);
    }
}
