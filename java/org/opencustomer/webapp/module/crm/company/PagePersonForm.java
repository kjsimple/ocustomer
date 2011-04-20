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

package org.opencustomer.webapp.module.crm.company;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.webapp.util.RequestUtility;
import org.opencustomer.webapp.action.EditPageForm;

public final class PagePersonForm extends EditPageForm
{
    private static final long serialVersionUID = 3257003254759307570L;

    /**
     * person id 
     */
    private int doJumpPerson;
    
    @Override
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
    }

    @Override
    protected void analyzeRequest(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        doJumpPerson = RequestUtility.parseParamId(request, "doJumpPerson_");
    }

    public final int getDoJumpPerson()
    {
        return doJumpPerson;
    }

    public final void setDoJumpPerson(int doJumpPerson)
    {
        this.doJumpPerson = doJumpPerson;
    }
}