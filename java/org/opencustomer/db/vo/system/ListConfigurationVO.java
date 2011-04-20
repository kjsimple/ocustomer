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

package org.opencustomer.db.vo.system;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.vo.BaseVO;

@Entity
@Table(name = "list_configuration")
@AttributeOverride(name = "id", column = @Column(name = "list_configuration_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class ListConfigurationVO extends BaseVO {
    private static final long serialVersionUID = 3258126955776849203L;

    public static enum Type {
        PERSON  ("person"),
        COMPANY ("company");
        
        private String name;
        
        private Type(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
    
    private Type type;

    private UserVO user;
    
    private String name;
    
    private String description;
    
    private Integer[] columns;
    
    private boolean isDefault;
    
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "default_list")
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    @Column(name = "list_type")
    @Enumerated(EnumType.STRING)
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    @Column(name = "column_list")
    protected String getColumnList() {
        if(columns != null) {
            StringBuilder builder = new StringBuilder();
            
            for(int i=0; i<columns.length; i++) {
                if(i != 0) {
                    builder.append(",");
                }
                
                builder.append(columns[i]);
            }
            
            return builder.toString();
        } else {
            return null;
        }
    }

    protected void setColumnList(String columnList) {
        if(columnList == null) {
            columns = null;
        } else {
            String[] cols = columnList.split(",");
            
            this.columns = new Integer[cols.length];
            for(int i=0; i<cols.length; i++) {
                columns[i] = Integer.parseInt(cols[i]);
            }
        }
    }

    @Transient
    public Integer[] getColumns() {
        return columns;
    }

    public void setColumns(Integer[] columns) {
        this.columns = columns;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    protected void toString(ToStringBuilder builder) {
        builder.append("name",        name);
        builder.append("description", description);
        builder.append("type",        type);
        builder.append("default",     isDefault);
        builder.append("columns",     columns);
    }
}
