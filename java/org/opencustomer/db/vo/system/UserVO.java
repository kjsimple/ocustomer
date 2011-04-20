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

package org.opencustomer.db.vo.system;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Where;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.db.vo.UndeletableVO;
import org.opencustomer.framework.util.LocaleUtility;

@Entity
@Table(name = "account")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@Where(clause="deleted=0")
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class UserVO extends UndeletableVO implements EntityAccess
{
    private static final long serialVersionUID = 3258407322534359863L;

    private String userName;

    private String password;

    private String locale;
    
    private PersonVO person;

    private CalendarVO calendar;
    
    private Integer ownerUser;

    private Integer ownerGroup;

    private Access accessUser;

    private Access accessGroup;

    private Access accessGlobal;

    private Set<CalendarVO> calendars = new HashSet<CalendarVO>();
    
    private ProfileVO profile;
     
    @Column(name = "password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Column(name = "user_name")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        if (userName != null)
            this.userName = userName.toLowerCase();
        else
            this.userName = userName;
    }

    @Column(name = "locale")
    public Locale getLocale()
    {
        if(locale == null)
            return null;
        else {
            return LocaleUtility.parseLocale(locale);
        }
    }

    public void setLocale(Locale locale)
    {
        if(locale == null)
            this.locale = null;
        else
            this.locale = locale.toString();
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    public PersonVO getPerson()
    {
        return person;
    }

    public void setPerson(PersonVO person)
    {
        this.person = person;
    }

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    public CalendarVO getCalendar()
    {
        return calendar;
    }

    public void setCalendar(CalendarVO calendar)
    {
        this.calendar = calendar;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_calendar", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "calendar_id") })
    public Set<CalendarVO> getCalendars()
    {
        return calendars;
    }

    public void setCalendars(Set<CalendarVO> calendars)
    {
        this.calendars = calendars;
    }
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public ProfileVO getProfile()
    {
        return profile;
    }

    public void setProfile(ProfileVO profile)
    {
        this.profile = profile;
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
    
    public void toString(ToStringBuilder builder)
    {
        builder.append("userName", userName);
        builder.append("password", password);
        builder.append("ownerUser", ownerUser);
        builder.append("ownerGroup", ownerGroup);
        builder.append("accessUser", accessUser);
        builder.append("accessGroup", accessGroup);
        builder.append("accessGlobal", accessGlobal);
    }
}
