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
package org.opencustomer.framework.webapp.panel;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.opencustomer.framework.db.vo.BaseVO;

public class EntityPanel extends Panel
{   
    private static final long serialVersionUID = -144443123399273020L;
    
    private BaseVO entity = null;

    public EntityPanel(Type type) {
        super(type);
    }
    
    public EntityPanel(Type type, BaseVO entity) {
        this(type);
        setEntity(entity);
    }
    
    public final BaseVO getEntity()
    {
        return entity;
    }

    public final void setEntity(BaseVO entity)
    {
        this.entity = entity;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        boolean isEqual = false;

        if (obj == null)
            isEqual = false;
        else if (this == obj)
            isEqual = true;
        else if (!(obj instanceof EditPanel))
            isEqual = false;
        else
        {
            EntityPanel castObj = (EntityPanel) obj;

            EqualsBuilder builder = new EqualsBuilder();

            builder.append(this.getType(), castObj.getType());
            builder.append(this.getEntity(), castObj.getEntity());
            
            isEqual = builder.isEquals();
        }

        return isEqual;
    }
}
