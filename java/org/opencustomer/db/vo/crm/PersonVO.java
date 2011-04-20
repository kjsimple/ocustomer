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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.db.vo.VersionedVO;

@Entity
@Table(name = "person")
@AttributeOverride(name = "id", column = @Column(name = "person_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class PersonVO extends VersionedVO implements EntityAccess
{
    private static final long serialVersionUID = 3257570615761449780L;

    public static enum Gender {
        UNKNOWN,
        MALE,
        FEMALE;
    }

    public static enum Type {
        FREELANCER,
        INDIVIDUAL,
        EMPLOYEE,
        BUSINESSMAN;
    }
    
    private Type type;
    
    private String firstName;

    private String lastName;

    private Gender gender;

    private String email;

    private String phone;

    private String mobile;

    private String fax;

    private String url;

    private String comment;

    private String function;

    private String degree;

    private String nameAffix;

    private String title;

    private Date dayOfBirth;
    
    private UserVO user;

    private CompanyVO company;

    private Set<AddressVO> addresses = new HashSet<AddressVO>();

    private Set<PersonContactVO> personContacts = new HashSet<PersonContactVO>();

    private Set<JobVO> jobs = new HashSet<JobVO>();
    
    private Integer ownerUser;

    private Integer ownerGroup;

    private Access accessUser;

    private Access accessGroup;

    private Access accessGlobal;
    
    @Column(name = "person_type")
    @Enumerated(EnumType.STRING)
    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
    
    @Column(name = "notice")
    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    @Column(name = "degree")
    public String getDegree()
    {
        return degree;
    }

    public void setDegree(String degree)
    {
        this.degree = degree;
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

    @Column(name = "first_name")
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @Column(name = "role")
    public String getFunction()
    {
        return function;
    }

    public void setFunction(String function)
    {
        this.function = function;
    }

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    @Column(name = "last_name")
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @Column(name = "mobile")
    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    @Column(name = "name_affix")
    public String getNameAffix()
    {
        return nameAffix;
    }

    public void setNameAffix(String nameAffix)
    {
        this.nameAffix = nameAffix;
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

    @Column(name = "title")
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Column(name = "day_of_birth")
    public Date getDayOfBirth()
    {
        return dayOfBirth;
    }

    public void setDayOfBirth(Date dayOfBirth)
    {
        this.dayOfBirth = dayOfBirth;
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

    @OneToOne(mappedBy = "person")
    public UserVO getUser()
    {
        return user;
    }

    public void setUser(UserVO user)
    {
        this.user = user;
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

    @OneToMany(mappedBy = "person", cascade=CascadeType.ALL)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public Set<AddressVO> getAddresses()
    {
        return addresses;
    }

    public void setAddresses(Set<AddressVO> addresses)
    {
        this.addresses = addresses;
    }

    @OneToMany(mappedBy = "person")
    public Set<PersonContactVO> getPersonContacts()
    {
        return personContacts;
    }

    public void setPersonContacts(Set<PersonContactVO> personContacts)
    {
        this.personContacts = personContacts;
    }

    @OneToMany(mappedBy = "referencedPerson")
    public Set<JobVO> getJobs()
    {
        return jobs;
    }

    public void setJobs(Set<JobVO> jobs)
    {
        this.jobs = jobs;
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
        builder.append("lastName", lastName);
        builder.append("firstName", firstName);
        builder.append("gender", gender);
        builder.append("email", email);
        builder.append("phone", phone);
        builder.append("mobile", mobile);
        builder.append("fax", fax);
        builder.append("url", url);
        builder.append("comment", comment);
        builder.append("function", function);
        builder.append("degree", degree);
        builder.append("nameAffix", nameAffix);
        builder.append("title", title);
        builder.append("dayOfBirth", dayOfBirth);        
        builder.append("ownerUser", ownerUser);
        builder.append("ownerGroup", ownerGroup);
        builder.append("accessUser", accessUser);
        builder.append("accessGroup", accessGroup);
        builder.append("accessGlobal", accessGlobal);
    }

}
