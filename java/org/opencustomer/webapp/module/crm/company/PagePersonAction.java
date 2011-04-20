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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.dao.crm.PersonDAO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.webapp.action.EditPageAction;

public class PagePersonAction extends EditPageAction<PagePersonForm>
{
    private static Logger log = Logger.getLogger(PagePersonAction.class);

    protected static final int STATUS_JUMPPERSON = 10;

    @Override
    protected void writeForm(PagePersonForm form, ActionMessages errors, HttpServletRequest request)
    {
        CompanyVO company = (CompanyVO)getPanel().getEntity();
        
        if(company.getId() != null) {
            getPanel().setAttribute("persons", new PersonDAO().getForCompany(company));
        }
    }
    
    @Override
    public ActionForward handleCustomAction(ActionMapping mapping, PagePersonForm form, ActionMessages errors, HttpServletRequest request)
    {
        if(log.isDebugEnabled())
            log.debug("handleCustomAction");
        
        if (form.getDoJumpPerson() > 0)
        {
            if(log.isDebugEnabled())
                log.debug("jump to person with id: "+form.getDoJumpPerson());
            
            request.setAttribute("external_person_id", form.getDoJumpPerson());
            
            return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
        }
        else
        {
            if(log.isDebugEnabled())
                log.debug("no custom action found");
        }
        
        return null;
    }
}
