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

package org.opencustomer.framework.db.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.opencustomer.db.listener.InsertOrUpdate;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.hibernate.IgnoreDirtyProperties;

@MappedSuperclass
public abstract class StampedVO extends BaseVO implements IgnoreDirtyProperties, InsertOrUpdate
{
    private final static Logger log = Logger.getLogger(StampedVO.class);
    
    private Integer createUser;

    private Date createDate;

    private Integer modifyUser;

    private Date modifyDate;

    /**
     * @hibernate.property column="create_timestamp" not-null="true"
     *                     update="false"
     */
    @Column(name = "create_timestamp", updatable = false, nullable = false)
    public Date getCreateDate()
    {
        return this.createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    /**
     * @hibernate.property column="create_user" not-null="true" update="false"
     */
    @Column(name = "create_user", updatable = false, nullable = false)
    public Integer getCreateUser()
    {
        return this.createUser;
    }

    public void setCreateUser(Integer createUser)
    {
        this.createUser = createUser;
    }

    /**
     * @hibernate.property column="modify_timestamp" not-null="true"
     */
    @Column(name = "modify_timestamp", nullable = false)
    public Date getModifyDate()
    {
        return this.modifyDate;
    }

    public void setModifyDate(Date modifyDate)
    {
        this.modifyDate = modifyDate;
    }

    /**
     * @hibernate.property column="modify_user" not-null="true"
     */
    @Column(name = "modify_user", nullable = false)
    public Integer getModifyUser()
    {
        return this.modifyUser;
    }

    public void setModifyUser(Integer modifyUser)
    {
        this.modifyUser = modifyUser;
    }

    protected void toStringEnd(ToStringBuilder builder)
    {
        builder.append("createUser", createUser);
        builder.append("createDate", createDate);
        builder.append("modifyUser", modifyUser);
        builder.append("modifyDate", modifyDate);
    }
    
    @Transient
    public Set<String> getPropertiesToIgnoreOnDirtyCheck()
    {
        HashSet<String> ignores = new HashSet<String>();
        
        ignores.add("createUser");
        ignores.add("createDate");
        ignores.add("modifyUser");
        ignores.add("modifyDate");
        
        return ignores;
    }
    
    public void onInsertOrUpdate(UserVO user)
    {
        Date now = new Date();
        int userId = -1;
        if(user != null) {
            userId = user.getId();
        } else if(log.isDebugEnabled()) {
            log.debug("insert or update entity without user: "+this);
        }
        
        if(getId() == null) {
            setCreateDate(now);
            setCreateUser(userId);
        }
        
        setModifyDate(now);
        setModifyUser(userId);
    }
}
