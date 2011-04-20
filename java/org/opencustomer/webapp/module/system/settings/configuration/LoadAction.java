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

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.ConfigurationDAO;
import org.opencustomer.db.vo.system.ConfigurationVO;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditLoadAction;
import org.opencustomer.webapp.auth.Right;

public class LoadAction extends EditLoadAction<LoadForm> {
    private static Logger log = Logger.getLogger(LoadAction.class);

    @Override
    public EditPanel createPanel(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        EditPanel panel = new EditPanel(Right.ADMINISTRATION_CONFIGURATION_WRITE, null);
        
        panel.setTitle(MessageUtil.message(request, "module.system.configuration.title"));
        
        panel.addAction(Action.Type.SAVE, "/system/settings/configuration/save");
        
        panel.addPage("STANDARD", "/system/settings/configuration/pageStandard", "module.generic.panel.tab.standard");
        panel.addPage("LDAP",     "/system/settings/configuration/pageLdap",     "module.system.configuration.pageLdap");
        
        return panel;
    }
    
    @Override
    public void createEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        loadEntity(errors, form, attributes, request, response);
    }
    
    @Override
    public void loadEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            List<ConfigurationVO> configurations = new ConfigurationDAO().getForSystem();
            
            attributes.put("configurations", configurations);
        } catch (HibernateException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.noEntityAccess"));

            log.error("could not load system configurations");
        }
    }
}
