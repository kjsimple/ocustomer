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
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;

@MappedSuperclass
public abstract class UndeletableVO extends VersionedVO
{
    private boolean deleted;

    /**
     * @hibernate.property column="deleted"
     */
    @Column(name = "deleted")
    public boolean isDeleted()
    {
        return this.deleted;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    protected void toStringEnd(ToStringBuilder builder)
    {
        super.toStringEnd(builder);
        builder.append("deleted", deleted);
    }

}
