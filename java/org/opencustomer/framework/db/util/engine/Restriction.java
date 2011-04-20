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
 * Copyright (C) 2007 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.framework.db.util.engine;

import org.apache.commons.lang.builder.ToStringBuilder;


public final class Restriction {

    private boolean group;
    
    private String hql;
    
    private Object[] values;
    
    public Restriction(String hql) {
        this(hql, false);
    }
    
    public Restriction(String hql, boolean group) {
        this(hql, group, (Object[])null);
    }
    
    public Restriction(String hql, Object... values) {
        this(hql, false, values);
    }
    
    public Restriction(String hql, boolean group, Object... values) {
        this.hql    = hql;
        this.values = values;
        this.group  = group;
    }

    public String getHql() {
        return hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object... values) {
        this.values = values;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("hql",    hql);
        builder.append("group",  group);
        builder.append("values", values);
        
        return builder.toString();
    }
}
