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
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.crm.AddressVO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.webapp.action.EditPageAction;

public class PageAddressAction extends EditPageAction<PageAddressForm>
{
    private static Logger log = Logger.getLogger(PageAddressAction.class);

    @Override
    public void writeForm(PageAddressForm form, ActionMessages errors, HttpServletRequest request)
    {
        PersonVO person = (PersonVO) getPanel().getEntity();

        for(AddressVO address : person.getAddresses()) {
            if(AddressVO.Type.MAIN.equals(address.getType())) {
                form.setMainAddressName(address.getName());
                form.setMainAddressStreet(address.getStreet());
                form.setMainAddressZip(address.getZip());
                form.setMainAddressCity(address.getCity());
            } else if(AddressVO.Type.POSTAL.equals(address.getType())) {
                form.setPostalAddressName(address.getName());
                form.setPostalAddressStreet(address.getStreet());
                form.setPostalAddressPostbox(address.getPostbox());
                form.setPostalAddressZip(address.getZip());
                form.setPostalAddressCity(address.getCity());
            }
        }        
    }
    
    @Override
    public void readForm(PageAddressForm form, ActionMessages errors, HttpServletRequest request)
    {
        PersonVO person = (PersonVO) getPanel().getEntity();

        if (log.isDebugEnabled())
            log.debug("save button pressed");

        AddressVO mainAddress = getAddress(person, AddressVO.Type.MAIN);
        mainAddress.setName(form.getMainAddressName());
        mainAddress.setStreet(form.getMainAddressStreet());
        mainAddress.setZip(form.getMainAddressZip());
        mainAddress.setCity(form.getMainAddressCity());
        
        manageAddress(person, mainAddress);
        
        AddressVO postalAddress = getAddress(person, AddressVO.Type.POSTAL);
        postalAddress.setName(form.getPostalAddressName());
        postalAddress.setStreet(form.getPostalAddressStreet());
        postalAddress.setPostbox(form.getPostalAddressPostbox());
        postalAddress.setZip(form.getPostalAddressZip());
        postalAddress.setCity(form.getPostalAddressCity());
        
        manageAddress(person, postalAddress);
    }

    private void manageAddress(PersonVO person, AddressVO address) {
        if(isAddressEmpty(address)) {
            if(person.getAddresses().contains(address)) {
                if(log.isDebugEnabled())
                    log.debug("remove empty address "+address.getType());
            
                person.getAddresses().remove(address);
            }
        } else {
            if(!person.getAddresses().contains(address)) {
                if(log.isDebugEnabled())
                    log.debug("add address "+address.getType());
            
                person.getAddresses().add(address);
            }
        }

    }
    
    private boolean isAddressEmpty(AddressVO address) {
        boolean isEmpty = false;

        if (address.getName() == null && address.getStreet() == null && address.getPostbox() == null && address.getZip() == null && address.getCity() == null)
            isEmpty = true;

        return isEmpty;
    }

    private AddressVO getAddress(PersonVO person, AddressVO.Type type)
    {
        AddressVO foundValue = null;

        for (AddressVO address : person.getAddresses())
        {
            if (type.equals(address.getType()))
            {
                if (log.isDebugEnabled())
                    log.debug("address found for " + type);

                foundValue = address;
                break;
            }
        }

        if (foundValue == null)
        {
            if (log.isDebugEnabled())
                log.debug("create address for " + type);

            foundValue = new AddressVO();
            foundValue.setType(type);
            foundValue.setPerson(person);
        }

        return foundValue;
    }
}
