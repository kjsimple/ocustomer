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

package org.opencustomer.webapp.module.system.settings.configuration;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.system.ConfigurationVO;
import org.opencustomer.util.configuration.ConfigurationUtil;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.webapp.action.EditPageAction;

public class PageStandardAction extends EditPageAction<PageStandardForm> {
    
    private static Logger log = Logger.getLogger(PageStandardAction.class);

    @Override
    public void readForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request) {
        List<ConfigurationVO> configurations = (List<ConfigurationVO>)getPanel().getAttribute("configurations");

        ConfigurationVO maxFailedLogins = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.MAX_FAILED_LOGINS, configurations);
        maxFailedLogins.setValue(String.valueOf(form.getMaxFailedLogins()));
        
        ConfigurationVO defaultTimeLock = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.DEFAULT_TIMELOCK, configurations);
        defaultTimeLock.setValue(String.valueOf(form.getDefaultTimeLock()));
        
        ConfigurationVO defaultIpPattern = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.DEFAULT_IPPATTERN, configurations);
        defaultIpPattern.setValue(form.getDefaultIpPattern());
        
        ConfigurationVO mailServer = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.MAIL_SMTP_SERVER, configurations);
        mailServer.setValue(form.getMailSmtpServer());

        ConfigurationVO showOverview = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.SHOW_OVERVIEW, configurations);
        showOverview.setValue(String.valueOf(form.isShowOverview()));
        
        ConfigurationVO multipleLogins = ConfigurationUtil.getConfiguration(SystemConfiguration.Key.MULTIPLE_LOGINS, configurations);
        multipleLogins.setValue(String.valueOf(form.isMultipleLogins()));
    }
    
    @Override
    public void writeForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request) {
        List<ConfigurationVO> configurations = (List<ConfigurationVO>)getPanel().getAttribute("configurations");
        
        form.setMaxFailedLogins(ConfigurationUtil.getIntValue(SystemConfiguration.Key.MAX_FAILED_LOGINS, configurations));
        form.setDefaultTimeLock(ConfigurationUtil.getIntValue(SystemConfiguration.Key.DEFAULT_TIMELOCK, configurations));
        form.setDefaultIpPattern(ConfigurationUtil.getStringValue(SystemConfiguration.Key.DEFAULT_IPPATTERN, configurations));                
        form.setMailSmtpServer(ConfigurationUtil.getStringValue(SystemConfiguration.Key.MAIL_SMTP_SERVER, configurations));
        form.setShowOverview(ConfigurationUtil.getBooleanValue(SystemConfiguration.Key.SHOW_OVERVIEW, configurations));
        form.setMultipleLogins(ConfigurationUtil.getBooleanValue(SystemConfiguration.Key.MULTIPLE_LOGINS, configurations));
    }
}
