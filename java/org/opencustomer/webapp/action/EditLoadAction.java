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

package org.opencustomer.webapp.action;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.AdminAccess;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.panel.PanelStack;
import org.opencustomer.framework.webapp.struts.Action;
import org.opencustomer.webapp.Globals;

public abstract class EditLoadAction<E extends EditLoadForm> extends Action<E>
{
    private final static Logger log = Logger.getLogger(EditLoadAction.class);
    
    @Override
    public final ActionForward execute(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UserVO user = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        
        ActionMessages errors = new ActionMessages();
        Hashtable<String, Object> attributes = new Hashtable<String, Object>();
        
        preOperation(errors, form, attributes, request, response);
        
        if(form != null && form.getId() == 0) {
            if (log.isDebugEnabled())
                log.debug("create entity");

            createEntity(errors, form, attributes, request, response);
        } else {
            if (log.isDebugEnabled())
                log.debug("load entity");
            
            loadEntity(errors, form, attributes, request, response);
        }

        loadEnvironment(errors, form, attributes, request, response);

        EditPanel panel = createPanel(errors, form, attributes, request, response);
        for(String name : attributes.keySet())
            panel.setAttribute(name, attributes.get(name));
        panel.setEditable(isEditable(panel, request));
        
        if(!validateAccess(request, panel))
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", panel.getEntity().getId()));
        
        PanelStack panelStack = Panel.getPanelStack(request);
        panelStack.push(panel);
        
        if(!errors.isEmpty())
        {
            this.saveErrors(request, errors);
            
            Panel.getPanelStack(request).pop();            
            return Panel.getForward(Panel.getPanelStack(request).peek().getPath(), request);
        }
        else
            return Panel.getForward(panel.getActivePage().getAction(), request);
    }
    
    public void preOperation(ActionMessages errors, E form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
    
    public void loadEnvironment(ActionMessages errors, E form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
    
    public abstract void loadEntity(ActionMessages errors, E form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    public abstract void createEntity(ActionMessages errors, E form, Hashtable<String, Object> attributes,HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    public abstract EditPanel createPanel(ActionMessages errors, E form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
    
    protected boolean validateAccess(HttpServletRequest request, EditPanel panel) {
        if(panel.getEntity() instanceof EntityAccess) {
            UserVO user = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
            
            if(!user.getProfile().getRole().isAdmin() 
                    && panel.getEntity() instanceof AdminAccess 
                    && ((AdminAccess)panel.getEntity()).isAdmin()) {
                return false;
            }
                
            return EntityAccessUtility.isAccessGranted(user, (EntityAccess)panel.getEntity(), EntityAccess.Access.READ);
        } else {
            return true;
        }
    }
    
    protected boolean isEditable(EditPanel panel, HttpServletRequest request) {
        return EditPanel.checkEditable(request, panel);
    }
}
