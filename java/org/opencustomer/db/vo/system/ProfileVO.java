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

import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.db.vo.VersionedVO;

@Entity
@Table(name = "profile")
@AttributeOverride(name = "id", column = @Column(name = "profile_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class ProfileVO extends VersionedVO implements EntityAccess
{
    private static final long serialVersionUID = 3258407322534359863L;

    private LdapGroupVO ldapGroup;
    
    private RoleVO role;

    private UsergroupVO defaultUsergroup;

    private Set<UsergroupVO> usergroups = new HashSet<UsergroupVO>();

    private Integer timeLock;    

    private String ipPattern; 

    private boolean locked;

    private Date validFrom;
    
    private Date validUntil;
    
    private Integer ownerUser;

    private Integer ownerGroup;

    private Access accessUser;

    private Access accessGroup;

    private Access accessGlobal;

    @OneToOne(mappedBy = "profile", fetch = FetchType.EAGER)
    public LdapGroupVO getLdapGroup()
    {
        return ldapGroup;
    }

    public void setLdapGroup(LdapGroupVO ldapGroup)
    {
        this.ldapGroup = ldapGroup;
    }

    @Column(name = "time_lock")
    public Integer getTimeLock()
    {
        return timeLock;
    }

    public void setTimeLock(Integer timeLock)
    {
        this.timeLock = timeLock;
    }

    @Column(name = "ip_pattern")
    public String getIpPattern()
    {
        return ipPattern;
    }

    public void setIpPattern(String ipPattern)
    {
        this.ipPattern = ipPattern;
    }


    @Transient
    public boolean isProfileLocked() {
        Date now = new Date();

        if(this.isLocked() 
                || (this.getValidFrom() != null && now.before(this.getValidFrom()))
                || (this.getValidUntil() != null && now.after(this.getValidUntil())))
            return true;
        else
            return false;
    }

    @Column(name = "locked")
    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

    @Column(name = "valid_from")
    public Date getValidFrom()
    {
        return validFrom;
    }

    public void setValidFrom(Date validFrom)
    {
        this.validFrom = validFrom;
    }

    @Column(name = "valid_until")
    public Date getValidUntil()
    {
        return validUntil;
    }

    public void setValidUntil(Date validUntil)
    {
        this.validUntil = validUntil;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    public RoleVO getRole()
    {
        return role;
    }

    public void setRole(RoleVO role)
    {
        this.role = role;
    }
    
    @ManyToOne
    @JoinColumn(name = "default_usergroup_id")
    public UsergroupVO getDefaultUsergroup()
    {
        return defaultUsergroup;
    }

    public void setDefaultUsergroup(UsergroupVO defaultUsergroup)
    {
        this.defaultUsergroup = defaultUsergroup;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "profile_usergroup", joinColumns = { @JoinColumn(name = "profile_id") }, inverseJoinColumns = { @JoinColumn(name = "usergroup_id") })
    public Set<UsergroupVO> getUsergroups()
    {
        return usergroups;
    }

    public void setUsergroups(Set<UsergroupVO> usergroups)
    {
        this.usergroups = usergroups;
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
        builder.append("timeLock", timeLock);
        builder.append("ipPattern", ipPattern);
        builder.append("locked", locked);
        builder.append("validFrom", validFrom);
        builder.append("validUntil", validUntil);
        builder.append("ownerUser", ownerUser);
        builder.append("ownerGroup", ownerGroup);
        builder.append("accessUser", accessUser);
        builder.append("accessGroup", accessGroup);
        builder.append("accessGlobal", accessGlobal);
    }
}
