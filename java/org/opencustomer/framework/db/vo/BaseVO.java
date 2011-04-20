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

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

@MappedSuperclass
public abstract class BaseVO extends AbstractVO implements Comparable
{
    private final static Logger log = Logger.getLogger(BaseVO.class);

    private Integer id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    protected void toStringStart(ToStringBuilder builder)
    {
        builder.append("id", getId());
    }

    protected void toStringEnd(ToStringBuilder builder)
    {
    }

    protected abstract void toString(ToStringBuilder builder);

    @Override
    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        toStringStart(builder);
        toString(builder);
        toStringEnd(builder);

        return builder.toString();
    }

    public int hashCode()
    {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public int compareTo(Object obj)
    {
        int compare = -1;

        if (obj == null)
            compare = -1;
        else if (this == obj)
            compare = 0;
        else if (!(obj instanceof BaseVO))
            compare = -1;
        else if (!this.getClass().equals(obj.getClass()))
            compare = -1;
        else
        {
            BaseVO castObj = (BaseVO) obj;

            CompareToBuilder builder = new CompareToBuilder();

            builder.append(this.getId(), castObj.getId());

            compare = builder.toComparison();
        }

        return compare;
    }

    public boolean equals(Object obj)
    {
        boolean isEqual = false;

        if (obj == null) {
            isEqual = false;
        }
        else if (this == obj) {
            isEqual = true;
        }
        else if (!(obj instanceof BaseVO)) {
            isEqual = false;
        }
        else if (!this.getClass().equals(obj.getClass())) {
            isEqual = false;
        }
        else
        {
            BaseVO castObj = (BaseVO) obj;

            EqualsBuilder builder = new EqualsBuilder();

            builder.append(this.getId(), castObj.getId());

            isEqual = builder.isEquals();
        }

        return isEqual;
    }
}
