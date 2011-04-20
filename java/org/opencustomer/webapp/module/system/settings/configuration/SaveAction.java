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

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.ConfigurationDAO;
import org.opencustomer.db.vo.system.ConfigurationVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditSaveAction;

public class SaveAction extends EditSaveAction {
    private static Logger log = Logger.getLogger(SaveAction.class);

    @Override
    protected void saveEntity(EditPanel panel, ActionMessages errors, HttpServletRequest request) {
        UserVO user                          = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        Iterator<ConfigurationVO> configurations = ((List<ConfigurationVO>)panel.getAttribute("configurations")).iterator();
        
        try {
            HibernateContext.beginTransaction();
            
            ConfigurationDAO dao = new ConfigurationDAO();
            while(configurations.hasNext()) {
                ConfigurationVO configuration = configurations.next();
                
                if (configuration.getId() == null) {
                    if(configuration.getValue() != null) { 
                        if (log.isDebugEnabled())
                            log.debug("create configuration");

                        dao.insert(configuration, user);
                    }
                } else {
                    if(configuration.getValue() != null) {
                        if (log.isDebugEnabled())
                            log.debug("save configuration (ID:" + configuration.getId() + ")");
                        
                        dao.update(configuration, user);
                    } else {
                        if (log.isDebugEnabled())
                            log.debug("delete configuration (ID:" + configuration.getId() + ")");
                        
                        dao.delete(configuration);
                        configurations.remove();
                    }
                }
            }
            
            HibernateContext.commitTransaction();
            
            log.debug("refresh system settings");
            HibernateContext.getSession().clear();
            SystemConfiguration.getInstance().refresh();
        } catch (HibernateException e) {
            HibernateContext.rollbackTransaction();
            
            log.error("could not save configurations", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.rollback"));
        }
    }
}