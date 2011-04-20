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
 * Software-Ingenieurb�ro). Portions created by the Initial Developer are
 * Copyright (C) 2005 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.webapp.module.crm.person;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.framework.webapp.action.JumpBean;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditLoadForm;

public final class LoadForm extends EditLoadForm
{
    private static final long serialVersionUID = 3617577098072306737L;

    @Override
    protected void analyzeRequest(ActionMapping mapping, ActionMessages error, HttpServletRequest request)
    {
        // �bernehme die companyId von dem Aufruf �ber die Person
        Integer extPersonId = (Integer) request.getAttribute("external_person_id");
        if (extPersonId != null)
            setId(extPersonId.intValue());
        
        if(request.getAttribute(Globals.JUMP_KEY) != null) {
            JumpBean jump = (JumpBean) request.getAttribute(Globals.JUMP_KEY);
            if(jump.getEntity() instanceof PersonVO) {
                setId(jump.getEntity().getId());
            }
        }
    }
}