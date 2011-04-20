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

package org.opencustomer.webapp.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.webapp.struts.ActionForm;

public abstract class EditLoadForm extends ActionForm
{

    private static final long serialVersionUID = 4260401830039415652L;

    private int id;

    @Override
    public final void validate(ActionMapping mapping, ActionMessages error, HttpServletRequest request)
    {
        if(id == 0) 
            analyzeRequest(mapping, error, request);
    }
    
    protected void analyzeRequest(ActionMapping mapping, ActionMessages error, HttpServletRequest request)
    {
    }
    
    public final int getId()
    {
        return id;
    }

    public final void setId(int id)
    {
        this.id = id;
    }    
}
