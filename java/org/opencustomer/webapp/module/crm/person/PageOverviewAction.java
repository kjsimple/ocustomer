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

package org.opencustomer.webapp.module.crm.person;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.hibernate.Hibernate;
import org.opencustomer.db.vo.crm.AddressVO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.webapp.action.EditPageAction;

public class PageOverviewAction extends EditPageAction<PageOverviewForm> {

    private static Logger log = Logger.getLogger(PageOverviewAction.class);

    @Override
    protected void readForm(PageOverviewForm form, ActionMessages errors, HttpServletRequest request) {
        writeForm(form, errors, request);
    }

    @Override
    protected void writeForm(PageOverviewForm form, ActionMessages errors, HttpServletRequest request) {
        PersonVO person = (PersonVO)getPanel().getEntity();
        
        AddressVO mainAddress   = null;
        AddressVO postalAddress = null;
        
        // get person addresses
        for(AddressVO address : person.getAddresses()) {
            if(AddressVO.Type.MAIN.equals(address.getType()) && mainAddress == null) {
                mainAddress = address;
            } else if(AddressVO.Type.POSTAL.equals(address.getType()) && postalAddress == null) {
                postalAddress = address;
            }
        }

        // get company addresses
        if(person.getCompany() != null && Hibernate.isInitialized(person.getCompany().getAddresses())) {
            for(AddressVO address : person.getCompany().getAddresses()) {
                if(AddressVO.Type.MAIN.equals(address.getType()) && mainAddress == null) {
                    mainAddress = address;
                } else if(AddressVO.Type.POSTAL.equals(address.getType()) && postalAddress == null) {
                    postalAddress = address;
                }
            }
        }
        
        if(mainAddress != null) {
            getPanel().setAttribute("mainAddress", mainAddress);
            if(mainAddress.getCompany() != null)
                getPanel().setAttribute("mainAddressCompany", Boolean.TRUE);
            else
                getPanel().removeAttribute("mainAddressCompany");
        } else {
            getPanel().removeAttribute("mainAddress");
            getPanel().removeAttribute("mainAddressCompany");
        }
        
        if(postalAddress != null) {
            getPanel().setAttribute("postalAddress", postalAddress);
            if(postalAddress.getCompany() != null)
                getPanel().setAttribute("postalAddressCompany", Boolean.TRUE);
            else
                getPanel().removeAttribute("postalAddressCompany");
        } else {
            getPanel().removeAttribute("postalAddress");
            getPanel().removeAttribute("postalAddressCompany");
        }
    }
    

    @Override
    public ActionForward handleCustomAction(ActionMapping mapping, PageOverviewForm form, ActionMessages errors, HttpServletRequest request) {
        if (form.getDoJumpCompany().isSelected()) {
            CompanyVO company = ((PersonVO) getPanel().getEntity()).getCompany();

            if(log.isDebugEnabled())
                log.debug("jump to company with id: "+company.getId());
            
            request.setAttribute("external_company_id", company.getId());
            
            return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
        }
        
        return null;
    }

    
}