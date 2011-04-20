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

package org.opencustomer.webapp.module.system.settings.list;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.system.ListConfigurationVO;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageStandardForm extends EditPageForm {
    private static final long serialVersionUID = 3258690996551430965L;

    private final static Logger log = Logger.getLogger(PageStandardForm.class);

    private String name;
    
    private String description;
    
    private boolean global;
    
    private boolean isDefault;
    
    @Override
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request) {
        ListConfigurationVO listConfiguration = (ListConfigurationVO)getPanel().getEntity();
        
        name        = FormUtility.adjustParameter(name, 255);
        description = FormUtility.adjustParameter(description);
        
        if (name == null)
            errors.add("name", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.listConfiguration.name")));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
}
