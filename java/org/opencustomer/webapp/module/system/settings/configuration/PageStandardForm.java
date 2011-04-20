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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.util.validator.IpAddressPatternValidator;
import org.opencustomer.framework.util.validator.URLValidator;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageStandardForm extends EditPageForm
{
    private static final long serialVersionUID = 3258690996551430965L;

    private final static Logger log = Logger.getLogger(PageStandardForm.class);

    private int maxFailedLogins;
    
    private int defaultTimeLock;
    
    private String defaultIpPattern;
    
    private String mailSmtpServer;
    
    private boolean showOverview;
    
    private boolean multipleLogins;
    
    @Override
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        defaultIpPattern = FormUtility.adjustParameter(defaultIpPattern, 255);
        mailSmtpServer   = FormUtility.adjustParameter(mailSmtpServer, 255);
        
        if (maxFailedLogins <= 1 || maxFailedLogins > 5)
            errors.add("maxFailedLogins", new ActionMessage("default.error.invalidValue.integer.between", MessageUtil.message(request, "entity.system.configuration.key.system.maxFailedLogins"), 1, 5));

        if (defaultTimeLock <= 0 || defaultTimeLock > 60)
            errors.add("defaultTimeLock", new ActionMessage("default.error.invalidValue.integer.between", MessageUtil.message(request, "entity.system.configuration.key.system.defaultTimeLock"), 0, 60));
        
        if (defaultIpPattern == null)
            errors.add("defaultIpPattern", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.configuration.key.system.defaultIpPattern")));
        else if(!IpAddressPatternValidator.getInstance().validate(defaultIpPattern)) {
            errors.add("defaultIpPattern", new ActionMessage("default.error.invalidFormat.undefined", MessageUtil.message(request, "entity.system.configuration.key.system.defaultIpPattern")));
        }
        
        if(mailSmtpServer != null && !URLValidator.getInstance().validateServer(mailSmtpServer)) {
            errors.add("mailSmtpServer", new ActionMessage("default.error.invalidValue.url", MessageUtil.message(request, "entity.system.configuration.key.system.mailSmtpServer")));
        }
    }

    public String getDefaultIpPattern() {
        return defaultIpPattern;
    }

    public void setDefaultIpPattern(String defaultIpPattern) {
        this.defaultIpPattern = defaultIpPattern;
    }

    public int getDefaultTimeLock() {
        return defaultTimeLock;
    }

    public void setDefaultTimeLock(int defaultTimeLock) {
        this.defaultTimeLock = defaultTimeLock;
    }

    public String getMailSmtpServer() {
        return mailSmtpServer;
    }

    public void setMailSmtpServer(String mailSmtpServer) {
        this.mailSmtpServer = mailSmtpServer;
    }

    public int getMaxFailedLogins() {
        return maxFailedLogins;
    }

    public void setMaxFailedLogins(int maxFailedLogins) {
        this.maxFailedLogins = maxFailedLogins;
    }

    public boolean isShowOverview() {
        return showOverview;
    }

    public void setShowOverview(boolean showOverview) {
        this.showOverview = showOverview;
    }

    public boolean isMultipleLogins() {
        return multipleLogins;
    }

    public void setMultipleLogins(boolean multipleLogins) {
        this.multipleLogins = multipleLogins;
    }
    
}
