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
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.vo.AdminAccess;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.db.vo.VersionedVO;

@Entity
@Table(name = "role")
@AttributeOverride(name = "id", column = @Column(name = "role_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class RoleVO extends VersionedVO implements EntityAccess, AdminAccess
{
    private static final long serialVersionUID = 3258126955776849203L;

    private String name;

    private String description;
    
    private boolean admin;

    private UsergroupVO defaultUsergroup;
    
    private Set<RightVO> rights = new HashSet<RightVO>();
    
    private Integer ownerUser;

    private Integer ownerGroup;

    private Access accessUser;

    private Access accessGroup;

    private Access accessGlobal;
    
    @Column(name = "admin")
    public boolean isAdmin()
    {
        return admin;
    }

    public void setAdmin(boolean admin)
    {
        this.admin = admin;
    }

    @Column(name = "description")
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Column(name = "name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.EAGER)
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
    @JoinTable(name = "right_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "right_id") })
    public Set<RightVO> getRights()
    {
        return rights;
    }

    public void setRights(Set<RightVO> rights)
    {
        this.rights = rights;
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
        builder.append("name", name);
        builder.append("description", description);
        builder.append("admin", admin);
        builder.append("ownerUser", ownerUser);
        builder.append("ownerGroup", ownerGroup);
        builder.append("accessUser", accessUser);
        builder.append("accessGroup", accessGroup);
        builder.append("accessGlobal", accessGlobal);
    }
}
