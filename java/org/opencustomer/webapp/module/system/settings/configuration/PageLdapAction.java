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
 * Copyright (C) 2007 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.webapp.module.system.settings.configuration;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.system.ConfigurationVO;
import org.opencustomer.util.configuration.ConfigurationUtil;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.webapp.action.EditPageAction;

public class PageLdapAction extends EditPageAction<PageLdapForm> {
    
    private static Logger log = Logger.getLogger(PageLdapAction.class);

    @Override
    public void readForm(PageLdapForm form, ActionMessages errors, HttpServletRequest request) {
        List<ConfigurationVO> configurations = (List<ConfigurationVO>)getPanel().getAttribute("configurations");

        ConfigurationVO authenticationEnabled = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_AUTHENTICATION_ENABLED, configurations);
        authenticationEnabled.setValue(String.valueOf(form.isAuthenticationEnabled()));
        
        ConfigurationVO addressExportEnabled = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED, configurations);
        addressExportEnabled.setValue(String.valueOf(form.isAddressExportEnabled()));
        
        ConfigurationVO addressInitalExport = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_ADDRESS_INITIAL_EXPORT, configurations);
        addressInitalExport.setValue(String.valueOf(form.isAddressInitialExport()));
        
        ConfigurationVO server = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_SERVER, configurations);
        server.setValue(form.getServer());
        
        ConfigurationVO port = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_PORT, configurations);
        port.setValue(form.getPort());
        
        ConfigurationVO adminUser = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_ADMIN_USER, configurations);
        adminUser.setValue(form.getAdminUser());

        ConfigurationVO adminPassword = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_ADMIN_PASSWORD, configurations);
        adminPassword.setValue(form.getAdminPassword());

        ConfigurationVO baseDn = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_BASE_DN, configurations);
        baseDn.setValue(form.getBaseDn());

        ConfigurationVO addressPrefix = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_ADDRESS_PREFIX, configurations);
        addressPrefix.setValue(form.getAddressPrefix());

        ConfigurationVO addressCompanyPrefix = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_ADDRESS_COMPANY_PREFIX, configurations);
        addressCompanyPrefix.setValue(form.getAddressCompanyPrefix());

        ConfigurationVO addressPersonPrefix = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_ADDRESS_PERSON_PREFIX, configurations);
        addressPersonPrefix.setValue(form.getAddressPersonPrefix());

        ConfigurationVO userPrefix = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_USER_PREFIX, configurations);
        userPrefix.setValue(form.getUserPrefix());

        ConfigurationVO groupPrefix = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_GROUP_PREFIX, configurations);
        groupPrefix.setValue(form.getGroupPrefix());

        ConfigurationVO systemUser = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.LDAP_SYSTEM_USER, configurations);
        systemUser.setValue(form.getSystemUser());
    }
    
    @Override
    public void writeForm(PageLdapForm form, ActionMessages errors, HttpServletRequest request) {
        List<ConfigurationVO> configurations = (List<ConfigurationVO>)getPanel().getAttribute("configurations");
        
        form.setAuthenticationEnabled(ConfigurationUtil.getBooleanValue(SystemConfiguration.Key.LDAP_AUTHENTICATION_ENABLED, configurations));
        form.setAddressExportEnabled(ConfigurationUtil.getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED, configurations));
        form.setAddressInitialExport(ConfigurationUtil.getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_INITIAL_EXPORT, configurations));
        
        form.setServer(ConfigurationUtil.getStringValue(SystemConfiguration.Key.LDAP_SERVER, configurations));
        if(ConfigurationUtil.getIntValue(SystemConfiguration.Key.LDAP_PORT, configurations) > 0)
            form.setPort(Integer.toString(ConfigurationUtil.getIntValue(SystemConfiguration.Key.LDAP_PORT, configurations)));
        form.setAdminUser(ConfigurationUtil.getStringValue(SystemConfiguration.Key.LDAP_ADMIN_USER, configurations));
        form.setAdminPassword(ConfigurationUtil.getStringValue(SystemConfiguration.Key.LDAP_ADMIN_PASSWORD, configurations));
        form.setBaseDn(ConfigurationUtil.getStringValue(SystemConfiguration.Key.LDAP_BASE_DN, configurations));
        form.setAddressPrefix(ConfigurationUtil.getStringValue(SystemConfiguration.Key.LDAP_ADDRESS_PREFIX, configurations));
        form.setAddressCompanyPrefix(ConfigurationUtil.getStringValue(SystemConfiguration.Key.LDAP_ADDRESS_COMPANY_PREFIX, configurations));
        form.setAddressPersonPrefix(ConfigurationUtil.getStringValue(SystemConfiguration.Key.LDAP_ADDRESS_PERSON_PREFIX, configurations));
        
        form.setUserPrefix(ConfigurationUtil.getStringValue(SystemConfiguration.Key.LDAP_USER_PREFIX, configurations));
        form.setGroupPrefix(ConfigurationUtil.getStringValue(SystemConfiguration.Key.LDAP_GROUP_PREFIX, configurations));
        form.setSystemUser(ConfigurationUtil.getStringValue(SystemConfiguration.Key.LDAP_SYSTEM_USER, configurations));
    }
}