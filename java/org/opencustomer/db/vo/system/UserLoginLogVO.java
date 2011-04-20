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

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.vo.BaseVO;

@Entity
@Table(name = "user_login_log")
@AttributeOverride(name = "id", column = @Column(name = "user_login_log_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class UserLoginLogVO extends BaseVO
{
    private static final long serialVersionUID = 3258407322534359863L;

    public static enum Type {
        WEBAPP,
        WEBDAV,
        WEBSERVICE;
    }
    
    public static enum Result {
        VALID,
        INVALID,
        VALID_REJECTED;
    }
    
    private String remoteHost;

    private Result result;

    private Date loginDate;

    private UserVO user;

    private Type type;
    
    @Column(name = "remote_host")
    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    @Column(name = "result")
    @Enumerated(EnumType.STRING)
    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Column(name = "login_date")
    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }
    
    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public void toString(ToStringBuilder builder)
    {
        builder.append("type", type);
        builder.append("remoteHost", remoteHost);
        builder.append("result", result);
        builder.append("loginDate", loginDate);
    }
}
