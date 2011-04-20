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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.ListConfigurationDAO;
import org.opencustomer.db.vo.system.ListConfigurationVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.webapp.action.ListAction;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.panel.PanelStack;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditSaveAction;

public class SaveAction extends EditSaveAction {
    private static Logger log = Logger.getLogger(SaveAction.class);

    @Override
    protected void saveEntity(EditPanel panel, ActionMessages errors, HttpServletRequest request) {
        ListConfigurationVO listConfiguration = (ListConfigurationVO) panel.getEntity();

        try {
            ListConfigurationDAO dao = new ListConfigurationDAO();
            HibernateContext.beginTransaction();
            
            if(listConfiguration.isDefault())
                dao.resetDefaultValue(listConfiguration.getType(), (listConfiguration.getUser() == null));
            
            if (listConfiguration.getId() == null) {
                if (log.isDebugEnabled())
                    log.debug("create list configuration");
                dao.insert(listConfiguration);
            } else {
                if (log.isDebugEnabled())
                    log.debug("save list configuration (ID:" + listConfiguration.getId() + ")");
                dao.update(listConfiguration);
            }
            
            HibernateContext.commitTransaction();
            
            PanelStack stack = Panel.getPanelStack(request);
            if(stack.getSize() > 1) {
                Panel lastPanel = stack.peek(2);
                lastPanel.removeAttribute("listConfigurations");
                lastPanel.removeAttribute(ListAction.TABLE_KEY);
            }
        } catch (HibernateException e) {
            log.error("problems saving list configuration", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.rollback"));
            
            HibernateContext.rollbackTransaction();
        }
    }
    
    @Override
    protected boolean validateData(EditPanel panel, ActionMessages errors, HttpServletRequest request) {
        UserVO user                           = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        ListConfigurationVO listConfiguration = (ListConfigurationVO) panel.getEntity();
        
        if (listConfiguration.getName() != null) {
            try {
                ListConfigurationDAO dao = new ListConfigurationDAO();

                ListConfigurationVO editListConfiguration = null;
                if(listConfiguration.getUser() == null)
                    editListConfiguration = dao.getForSystemByTypeAndName(listConfiguration.getType(), listConfiguration.getName());
                else
                    editListConfiguration = dao.getForUserByTypeAndName(listConfiguration.getType(), listConfiguration.getName(), user);
                
                if (editListConfiguration != null && !editListConfiguration.getId().equals(listConfiguration.getId())) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.system.list.error.nameExists"));
                }

                HibernateContext.getSession().evict(editListConfiguration);
            } catch (HibernateException e) {
                log.error("could not read list configuration for name: " + listConfiguration.getName());
            }
        }
        
        if(listConfiguration.getColumns() == null) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.system.list.error.columnsNeeded"));
        }

        if (errors.isEmpty())
            return true;
        else
            return false;
    }
}