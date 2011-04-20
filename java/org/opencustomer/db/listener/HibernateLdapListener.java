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
 *                 Felix Breske <felix.breske@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.db.listener;

import java.util.Set;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.event.DeleteEvent;
import org.hibernate.event.DeleteEventListener;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.SaveOrUpdateEventListener;
import org.opencustomer.connector.ldap.address.CompanyLdap;
import org.opencustomer.connector.ldap.address.PersonLdap;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.util.configuration.SystemConfiguration;

/**
 * This class listen to saved or deleted PersonVO and CompanyVO. If an entity is saved r delted, the corresponding
 * ldap entry is also changed.
 * @author fbreske
 *
 */
public class HibernateLdapListener implements SaveOrUpdateEventListener, DeleteEventListener {
    private static final long serialVersionUID = -7683592594778753948L;
    
    private static final Logger log = Logger.getLogger(HibernateLdapListener.class);

    public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
        try {
            if(event.getEntity() instanceof PersonVO && SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED)) {
                PersonVO person = (PersonVO) event.getEntity();
                PersonLdap.getInstance().updateLdapPerson((PersonVO) event.getEntity());
            } else if(event.getEntity() instanceof CompanyVO && SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED)) {
                CompanyLdap.getInstance().updateLdapCompany((CompanyVO)event.getEntity());
            }
        }catch(NamingException e) {
            log.error("unable to update ldap address",e);
        }        
    }

    public void onDelete(DeleteEvent event) throws HibernateException {
        onDelete(event, null);
    }
    
    public void onDelete(DeleteEvent event, Set transientEntities) throws HibernateException {
        try {
            if(event.getObject() instanceof PersonVO && SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED)) {
                PersonLdap.getInstance().deleteLdapPerson((PersonVO) event.getObject());
            } else if(event.getObject() instanceof CompanyVO && SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED)) {
                CompanyLdap.getInstance().updateLdapCompany((CompanyVO)event.getObject());
            }
        } catch(NamingException e) {
            log.error("unable to delte ldap address",e);
        }
    }
}
