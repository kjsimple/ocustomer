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

package org.opencustomer.db.vo.crm;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.vo.ListVO;

@Entity
@Table(name = "legal_form")
@AttributeOverride(name = "id", column = @Column(name = "legal_form_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class LegalFormVO extends ListVO {
    private static final long serialVersionUID = 3689066253222687026L;

    private LegalFormGroupVO legalFormGroup;

    @ManyToOne
    @JoinColumn(name = "legal_form_group_id")
    public LegalFormGroupVO getLegalFormGroup() {
        return legalFormGroup;
    }

    public void setLegalFormGroup(LegalFormGroupVO legalFormGroup) {
        this.legalFormGroup = legalFormGroup;
    }

    protected void toString(ToStringBuilder builder) {
    }

}
