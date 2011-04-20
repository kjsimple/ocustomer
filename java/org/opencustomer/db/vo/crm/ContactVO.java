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

package org.opencustomer.db.vo.crm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.db.vo.VersionedVO;

@Entity
@Table(name = "contact")
@AttributeOverride(name = "id", column = @Column(name = "contact_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class ContactVO extends VersionedVO implements EntityAccess
{
    private static final long serialVersionUID = 3834315033474906417L;

    public static enum ContactType {
        TELEPHONE,
        EMAIL,
        LETTER,
        FAX,
        PERSONAL;
    }

    public static enum BoundType {
        IN,
        OUT;
    }

    public static enum ImportType {
        NONE,
        NEW,
        ACCEPTED;
    }

    public static enum ContentType {
        PLAINTEXT,
        HTML;
    }

    private Date contactTimestamp;

    private String subject;

    private String content;

    private String contactName;

    private ContactType contactType;

    private BoundType boundType;

    private CompanyVO company;

    private ContentType contentType = ContentType.PLAINTEXT;

    private ImportType importType = ImportType.NONE;

    private Set<PersonContactVO> personContacts = new HashSet<PersonContactVO>();
    
    private JobVO job;
    
    private Integer ownerUser;

    private Integer ownerGroup;

    private Access accessUser;

    private Access accessGroup;

    private Access accessGlobal;
    
    @Column(name = "bound_type")
    @Enumerated(EnumType.STRING)
    public BoundType getBoundType()
    {
        return boundType;
    }

    public void setBoundType(BoundType boundType)
    {
        this.boundType = boundType;
    }

    @Column(name = "contact_name")
    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    @Column(name = "contact_timestamp")
    public Date getContactTimestamp()
    {
        return contactTimestamp;
    }

    public void setContactTimestamp(Date contactTimestamp)
    {
        this.contactTimestamp = contactTimestamp;
    }

    @Column(name = "contact_type")
    @Enumerated(EnumType.STRING)
    public ContactType getContactType()
    {
        return contactType;
    }

    public void setContactType(ContactType contactType)
    {
        this.contactType = contactType;
    }

    @Column(name = "content")
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
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

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    public ContentType getContentType()
    {
        return contentType;
    }

    public void setContentType(ContentType contentType)
    {
        this.contentType = contentType;
    }

    @Column(name = "import_type")
    @Enumerated(EnumType.STRING)
    public ImportType getImportType()
    {
        return importType;
    }

    public void setImportType(ImportType importType)
    {
        this.importType = importType;
    }

    @ManyToOne
    @JoinColumn(name = "company_id")
    public CompanyVO getCompany()
    {
        return company;
    }

    public void setCompany(CompanyVO company)
    {
        this.company = company;
    }

    @OneToMany(mappedBy = "contact", cascade=CascadeType.ALL)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN }) 
    public Set<PersonContactVO> getPersonContacts()
    {
        return personContacts;
    }

    public void setPersonContacts(Set<PersonContactVO> personContacts)
    {
        this.personContacts = personContacts;
    }

    @ManyToOne
    @JoinColumn(name = "job_id")
    public JobVO getJob()
    {
        return job;
    }
    
    public void setJob(JobVO job)
    {
        this.job = job;
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
    
    protected void toString(ToStringBuilder builder)
    {
        builder.append("company", company != null ? company.getId() : "null");
        builder.append("job", job != null ? job.getId() : "null");
        builder.append("contactTimestamp", contactTimestamp);
        builder.append("subject", subject);
        builder.append("content", content);
        builder.append("contentType", contentType);
        builder.append("contactName", contactName);
        builder.append("contactType", contactType);
        builder.append("boundType", boundType);
        builder.append("importType", importType);
        builder.append("ownerUser", ownerUser);
        builder.append("ownerGroup", ownerGroup);
        builder.append("accessUser", accessUser);
        builder.append("accessGroup", accessGroup);
        builder.append("accessGlobal", accessGlobal);
    }

}
