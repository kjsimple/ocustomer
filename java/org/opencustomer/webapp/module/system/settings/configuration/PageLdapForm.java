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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageLdapForm extends EditPageForm {
    private static final long serialVersionUID = 3258690996551430965L;

    private final static Logger log = Logger.getLogger(PageLdapForm.class);

    private boolean authenticationEnabled;
    
    private boolean addressExportEnabled;
    
    private boolean addressInitialExport;
    
    private String server;
    
    private String port;

    private String adminUser;

    private String adminPassword;

    private String baseDn;

    private String addressPrefix;

    private String addressCompanyPrefix;

    private String addressPersonPrefix;

    private String userPrefix;

    private String groupPrefix;

    private String systemUser;
    
    @Override
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request) {
        server               = FormUtility.adjustParameter(server);
        adminUser            = FormUtility.adjustParameter(adminUser);
        adminPassword        = FormUtility.adjustParameter(adminPassword);
        baseDn               = FormUtility.adjustParameter(baseDn);
        addressPrefix        = FormUtility.adjustParameter(addressPrefix);
        addressCompanyPrefix = FormUtility.adjustParameter(addressCompanyPrefix);
        addressPersonPrefix  = FormUtility.adjustParameter(addressPersonPrefix);
        userPrefix           = FormUtility.adjustParameter(userPrefix);
        groupPrefix          = FormUtility.adjustParameter(groupPrefix);
        systemUser           = FormUtility.adjustParameter(systemUser);

        if(!addressExportEnabled)
            addressInitialExport = false;
        
        
        if(authenticationEnabled || addressExportEnabled) {
            if (server == null)
                errors.add("server", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.server")));
            if (port == null) {
                errors.add("port", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.port")));
            } else if (!port.matches("\\d+")) {
                errors.add("port", new ActionMessage("default.error.invalidFormat.unsignedInteger", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.port")));
            }
            if (baseDn == null)
                errors.add("baseDn", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.baseDn")));
        } else {
            server = null;
            port   = null;
            baseDn = null;
        }
        
        if(authenticationEnabled) {
            if (systemUser == null)
                errors.add("systemUser", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.systemUser")));
            if (userPrefix == null)
                errors.add("userPrefix", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.addressUserPrefix")));
            if (groupPrefix == null)
                errors.add("groupPrefix", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.addressGroupPrefix")));
        } else {
            systemUser  = null;
            userPrefix  = null;
            groupPrefix = null;
        }
        
        if(addressExportEnabled) {
            if (adminUser == null)
                errors.add("adminUser", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.adminUser")));
            if (adminPassword == null)
                errors.add("adminPassword", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.adminPassword")));
            if (addressPrefix == null)
                errors.add("addressPrefix", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.addressPrefix")));
            if (addressCompanyPrefix == null)
                errors.add("addressCompanyPrefix", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.addressCompanyPrefix")));
            if (addressPersonPrefix == null)
                errors.add("addressPersonPrefix", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.ldap.addressPersonPrefix")));
        } else {
            adminUser            = null;
            adminPassword        = null;
            addressPrefix        = null;
            addressCompanyPrefix = null;
            addressPersonPrefix  = null;
        }
    }

    public String getAddressCompanyPrefix() {
        return addressCompanyPrefix;
    }

    public void setAddressCompanyPrefix(String addressCompanyPrefix) {
        this.addressCompanyPrefix = addressCompanyPrefix;
    }

    public boolean isAddressExportEnabled() {
        return addressExportEnabled;
    }

    public void setAddressExportEnabled(boolean addressExportEnabled) {
        this.addressExportEnabled = addressExportEnabled;
    }

    public boolean isAddressInitialExport() {
        return addressInitialExport;
    }

    public void setAddressInitialExport(boolean addressInitialExport) {
        this.addressInitialExport = addressInitialExport;
    }

    public String getAddressPersonPrefix() {
        return addressPersonPrefix;
    }

    public void setAddressPersonPrefix(String addressPersonPrefix) {
        this.addressPersonPrefix = addressPersonPrefix;
    }

    public String getAddressPrefix() {
        return addressPrefix;
    }

    public void setAddressPrefix(String addressPrefix) {
        this.addressPrefix = addressPrefix;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public boolean isAuthenticationEnabled() {
        return authenticationEnabled;
    }

    public void setAuthenticationEnabled(boolean authenticationEnabled) {
        this.authenticationEnabled = authenticationEnabled;
    }

    public String getBaseDn() {
        return baseDn;
    }

    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public String getGroupPrefix() {
        return groupPrefix;
    }

    public void setGroupPrefix(String groupPrefix) {
        this.groupPrefix = groupPrefix;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(String systemUser) {
        this.systemUser = systemUser;
    }

    public String getUserPrefix() {
        return userPrefix;
    }

    public void setUserPrefix(String userPrefix) {
        this.userPrefix = userPrefix;
    }

}
