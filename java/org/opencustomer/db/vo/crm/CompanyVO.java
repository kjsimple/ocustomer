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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.opencustomer.db.UserAssigned;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.db.vo.VersionedVO;

@Entity
@Table(name = "company")
@AttributeOverride(name = "id", column = @Column(name = "company_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class CompanyVO extends VersionedVO implements EntityAccess, UserAssigned
{
    private static final long serialVersionUID = 3906363835614180917L;

    private String companyName;

    private String url;

    private String email;

    private String phone;

    private String fax;

    private String comment;

    private String info;

    private Integer staffCount;

    private Set<PersonVO> persons = new HashSet<PersonVO>();

    private LegalFormVO legalForm;

    private SectorVO sector;

    private CompanyStateVO companyState;

    private CompanyTypeVO companyType;
    
    private CategoryVO category;
    
    private RatingVO rating;

    private Set<AddressVO> addresses = new HashSet<AddressVO>();

    private Set<ContactVO> contacts = new HashSet<ContactVO>();

    private Set<PersonContactVO> personContacts = new HashSet<PersonContactVO>();

    private Set<JobVO> jobs = new HashSet<JobVO>();
    
    private UserVO assignedUser;
    
    private Integer ownerUser;

    private Integer ownerGroup;

    private Access accessUser;

    private Access accessGroup;

    private Access accessGlobal;
    
    @Column(name = "notice")
    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    @Column(name = "company_name")
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    @Column(name = "email")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Column(name = "fax")
    public String getFax()
    {
        return fax;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
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

    @OneToMany(mappedBy = "company")
    @OrderBy("lastName, firstName")
    public Set<PersonVO> getPersons()
    {
        return persons;
    }

    public void setPersons(Set<PersonVO> persons)
    {
        this.persons = persons;
    }


    @OneToMany(mappedBy = "company")
    public Set<PersonContactVO> getPersonContacts()
    {
        return personContacts;
    }

    public void setPersonContacts(Set<PersonContactVO> personContacts)
    {
        this.personContacts = personContacts;
    }
    
    @Column(name = "phone")
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Column(name = "staff_count")
    public Integer getStaffCount()
    {
        return staffCount;
    }

    public void setStaffCount(Integer staffCount)
    {
        this.staffCount = staffCount;
    }

    @Column(name = "url")
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @ManyToOne
    @JoinColumn(name = "legal_form_id")
    public LegalFormVO getLegalForm()
    {
        return legalForm;
    }

    public void setLegalForm(LegalFormVO legalForm)
    {
        this.legalForm = legalForm;
    }

    @ManyToOne
    @JoinColumn(name = "company_state_id")
    public CompanyStateVO getCompanyState()
    {
        return companyState;
    }

    public void setCompanyState(CompanyStateVO companyState)
    {
        this.companyState = companyState;
    }

    @ManyToOne
    @JoinColumn(name = "company_type_id")
    public CompanyTypeVO getCompanyType()
    {
        return companyType;
    }

    public void setCompanyType(CompanyTypeVO companyType)
    {
        this.companyType = companyType;
    }

    @ManyToOne
    @JoinColumn(name = "sector_id")
    public SectorVO getSector()
    {
        return sector;
    }

    public void setSector(SectorVO sector)
    {
        this.sector = sector;
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    public CategoryVO getCategory()
    {
        return category;
    }

    public void setCategory(CategoryVO category)
    {
        this.category = category;
    }
    
    @ManyToOne
    @JoinColumn(name = "rating_id")
    public RatingVO getRating()
    {
        return rating;
    }

    public void setRating(RatingVO rating)
    {
        this.rating = rating;
    }
    
    @OneToMany(mappedBy = "company", cascade=CascadeType.ALL)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public Set<AddressVO> getAddresses()
    {
        return addresses;
    }

    public void setAddresses(Set<AddressVO> addresses)
    {
        this.addresses = addresses;
    }

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

    @OneToMany(mappedBy = "referencedCompany")
    public Set<JobVO> getJobs()
    {
        return jobs;
    }

    public void setJobs(Set<JobVO> jobs)
    {
        this.jobs = jobs;
    }
    
    @OneToMany(mappedBy = "company")
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
    
    protected void toString(ToStringBuilder builder)
    {
        builder.append("companyName", companyName);
        builder.append("email", email);
        builder.append("phone", phone);
        builder.append("fax", fax);
        builder.append("comment", comment);
        builder.append("info", info);
        builder.append("staffCount", staffCount);
        builder.append("ownerUser", ownerUser);
        builder.append("ownerGroup", ownerGroup);
        builder.append("accessUser", accessUser);
        builder.append("accessGroup", accessGroup);
        builder.append("accessGlobal", accessGlobal);
    }

}
