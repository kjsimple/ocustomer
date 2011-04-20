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

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.ListConfigurationDAO;
import org.opencustomer.db.vo.system.ListConfigurationVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.webapp.action.ListAction;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.panel.PanelStack;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.framework.webapp.util.html.Table;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditLoadAction;
import org.opencustomer.webapp.auth.Right;
import org.opencustomer.webapp.module.crm.company.DeleteAction;

public class LoadAction extends EditLoadAction<LoadForm>
{
    private static Logger log = Logger.getLogger(LoadAction.class);

    @Override
    public EditPanel createPanel(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        ListConfigurationVO listConfiguration = (ListConfigurationVO)attributes.get("listConfiguration");
        String name = "list";
        
        if(log.isDebugEnabled())
            log.debug("listConfiguration (in panel): "+listConfiguration);
        
        EditPanel panel = null;
        if(listConfiguration.getUser() == null) {
            panel = new EditPanel(Right.ADMINISTRATION_LIST_GLOBALLIST, listConfiguration);
        } else {
            panel = new EditPanel(Right.ADMINISTRATION_LIST_WRITE, listConfiguration);
        }
        
        if(listConfiguration.getId() == null)
            panel.setTitle(MessageUtil.message(request, "module.system.list.create.title"));
        else {
            panel.setTitle(MessageUtil.message(request, "module.system.list.edit.title", listConfiguration.getName()));

            panel.setAttribute(DeleteAction.TEXT_TITLE, MessageUtil.message(request, "module.system.list.delete.title", listConfiguration.getName()));
            panel.setAttribute(DeleteAction.TEXT_QUESTION, MessageUtil.message(request, "module.system.list.delete.question", listConfiguration.getName()));
        }
        
        panel.addAction(Action.Type.DELETE, "/system/settings/"+name+"/delete");
        panel.addAction(Action.Type.SAVE, "/system/settings/"+name+"/save");
        
        panel.addPage("STANDARD", "/system/settings/"+name+"/pageStandard", "module.generic.panel.tab.standard");
        panel.addPage("COLUMNS", "/system/settings/"+name+"/pageColumns", "module.system.list.page.columns");
        
        return panel;
    }
    
    @Override
    public void loadEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (log.isDebugEnabled())
            log.debug("load list configuration (ID:" + form.getId() + ")");

        try {
            ListConfigurationVO listConfiguration = new ListConfigurationDAO().getById(form.getId());
            attributes.put("listConfiguration", listConfiguration);
        } catch (HibernateException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));

            log.debug("problems loading list configuration (ID:" + form.getId() + ")");
        }
    }
    
    @Override
    public void createEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (log.isDebugEnabled())
            log.debug("create list configuration (type:"+attributes.get("defaultType")+")");

        UserVO user   = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        
        ListConfigurationVO listConfiguration = new ListConfigurationVO();
        
        ListConfigurationVO.Type type = (ListConfigurationVO.Type)attributes.get("defaultType");
        
        listConfiguration.setType((ListConfigurationVO.Type)attributes.get("defaultType"));
        listConfiguration.setUser(user);
        
        attributes.put("listConfiguration", listConfiguration);
    }
    
    @Override
    public void preOperation(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PanelStack stack = Panel.getPanelStack(request);
        if(!stack.isEmpty()) {
            Panel lastPanel = Panel.getPanelStack(request).peek();
            if(lastPanel.getAttribute(ListAction.TABLE_KEY) != null) {
                Table table = (Table)lastPanel.getAttribute(ListAction.TABLE_KEY);
                ListConfigurationVO.Type type = null;
                for(ListConfigurationVO.Type existingType : ListConfigurationVO.Type.values()) {
                    if(existingType.getName().equals(table.getName())) {
                        type = existingType;
                        break;
                    }
                }
                
                if(log.isDebugEnabled())
                    log.debug("create user list for type: "+type);
                
                attributes.put("defaultType", type);
            }
        }
    }
}
