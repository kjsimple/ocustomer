package org.opencustomer.db.vo.system;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Where;
import org.opencustomer.framework.db.vo.AdminAccess;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.db.vo.UndeletableVO;

@Entity
@Table(name = "usergroup")
@AttributeOverride(name = "id", column = @Column(name = "usergroup_id"))
@Where(clause="deleted=0")
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class UsergroupVO extends UndeletableVO implements EntityAccess, AdminAccess
{
    private static final long serialVersionUID = 3834309548801405745L;

    private String name;

    private String description;
    
    private boolean admin;
    
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