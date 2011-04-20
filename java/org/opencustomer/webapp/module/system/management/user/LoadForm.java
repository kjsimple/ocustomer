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

package org.opencustomer.webapp.module.system.management.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.webapp.action.EditLoadForm;

public final class LoadForm extends EditLoadForm
{
    private static final long serialVersionUID = 3257845493685236278L;
    
    private static Logger log = Logger.getLogger(LoadForm.class);
    
    @Override 
    public void analyzeRequest(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        Integer extUserId = (Integer) request.getAttribute("external_user_id");
        if (extUserId != null) {
            if(log.isDebugEnabled())
                log.debug("extern call for id: "+extUserId);
            
            setId(extUserId.intValue());
        }
    }
}
