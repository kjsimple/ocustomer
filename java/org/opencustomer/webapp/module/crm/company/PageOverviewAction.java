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

package org.opencustomer.webapp.module.crm.company;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.crm.AddressVO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.webapp.action.EditPageAction;

public class PageOverviewAction extends EditPageAction<PageOverviewForm> {

    private static Logger log = Logger.getLogger(PageOverviewAction.class);

    @Override
    protected void readForm(PageOverviewForm form, ActionMessages errors, HttpServletRequest request) {
        writeForm(form, errors, request);
    }

    @Override
    protected void writeForm(PageOverviewForm form, ActionMessages errors, HttpServletRequest request) {
        CompanyVO company = (CompanyVO)getPanel().getEntity();
        
        AddressVO mainAddress   = null;
        AddressVO postalAddress = null;
        
        for(AddressVO address : company.getAddresses()) {
            if(AddressVO.Type.MAIN.equals(address.getType())) {
                mainAddress = address;
            } else if(AddressVO.Type.POSTAL.equals(address.getType())) {
                postalAddress = address;
            }
        }
        
        if(mainAddress != null)
            getPanel().setAttribute("mainAddress", mainAddress);
        else 
            getPanel().removeAttribute("mainAddress");
        
        if(postalAddress != null)
            getPanel().setAttribute("postalAddress", postalAddress);
        else 
            getPanel().removeAttribute("postalAddress");
    }
    
}