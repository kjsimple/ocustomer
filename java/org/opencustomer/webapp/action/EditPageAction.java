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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.config.ActionConfig;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Page;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.panel.PanelStack;
import org.opencustomer.framework.webapp.struts.Action;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.util.menu.Menu;

public abstract class EditPageAction<E extends EditPageForm> extends Action<E>
{
    private final static Logger log = Logger.getLogger(EditPageAction.class);
    
    private EditPanel panel;
    
    @Override
    public final ActionForward execute(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if(!Panel.validate(request)) {
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.backbutton"));
            saveErrors(request, errors);
            
            return Panel.getForward(Panel.getPanelStack(request).peek().getPath(), request);
        }
        
        ActionForward forward = mapping.getInputForward();
        ActionMessages errors = new ActionMessages();
        
        boolean removePanel = false;
        
        if(!Panel.getPanelStack(request).isEmpty())
        {
            panel = (EditPanel)Panel.getPanelStack(request).peek();
            
            preOperations(form, errors, request);
            
            if(log.isDebugEnabled())
                log.debug("token is valid: "+this.isTokenValid(request));
            
            ActionForward customForward = null;
            if (this.isTokenValid(request) && form.getDoSave() != null)
            {
                if (log.isDebugEnabled())
                    log.debug("button: save");
    
                if(isEditable(getPanel()))
                    readForm(form, errors, request);
                
                forward = Panel.getForward(panel.getAction(org.opencustomer.framework.webapp.panel.Action.Type.SAVE).getAction(), request);
            }
            else if (this.isTokenValid(request) && form.getDoTab() != null)
            {
                if (log.isDebugEnabled())
                    log.debug("button: tab");
    
                if(isEditable(getPanel()))
                    readForm(form, errors, request);
                
                forward = Panel.getForward(form.getDoTab().getAction(), request);
            }
            else if (this.isTokenValid(request) && form.getDoCancel() != null)
            {
                if (log.isDebugEnabled())
                    log.debug("button: cancel");
    
                if(Panel.getPanelStack(request).getSize() > 1) {
                    Panel.getPanelStack(request).pop();            
                    forward = Panel.getForward(Panel.getPanelStack(request).peek().getPath(), request);
                } else {
                    forward = Panel.getForward(panel.getActivePage().getAction(), request);
                }
            }
            else if (this.isTokenValid(request) && form.getDoDelete() != null)
            {
                if (log.isDebugEnabled())
                    log.debug("button: delete");
                
                forward = Panel.getForward(panel.getAction(org.opencustomer.framework.webapp.panel.Action.Type.DELETE).getAction(), request);
            }
            else if (this.isTokenValid(request) && (customForward = handleCustomAction(mapping, form, errors, request)) != null)
            {
                if (log.isDebugEnabled())
                    log.debug("button: custom (???)");
                
                if(isEditable(getPanel()))
                    readForm(form, errors, request);
    
                forward = customForward;
            }
            else 
            {
                if (log.isDebugEnabled())
                    log.debug("write form "+form);
                
                writeForm(form, errors, request);
                preSearch(mapping, form, request, response);
            }
            
            if(removePanel) 
            {
                if(log.isDebugEnabled())
                    log.debug("leave panel with action > pop stack");
                    
                PanelStack panelStack = Panel.getPanelStack(request);
                panelStack.pop();
            }
            
            // fix backbutton problem of browser
            ActionConfig actionConfig = mapping.getModuleConfig().findActionConfig(panel.getActivePage().getAction());
            if(!this.isTokenValid(request) && !actionConfig.getInput().equals(mapping.getInput())) {
                if(log.isDebugEnabled())
                    log.debug("found invalid tab position");
                
                for(Page tab : panel.getPages()) 
                {
                    if(tab.getAction().equals(mapping.getPath()))
                        panel.activatePage(tab);
                }
                
                if(log.isDebugEnabled())
                    log.debug("force activation: "+panel.getActivePage().getName());
                
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.backbutton"));
            }
            
            if(this.isTokenValid(request) && panel != null && form.getDoTab() != null && getErrors(request).isEmpty())
            {
                if(log.isDebugEnabled())
                    log.debug("activate tab: "+form.getDoTab());
    
                panel.activatePage(form.getDoTab());
            }   
            
            postOperations(form, errors, request);
        }
        else
        {
            Menu menu = (Menu)request.getSession().getAttribute(Globals.MENU_KEY);
            forward = Panel.getForward(menu.getActiveItem().getAction(), request);
            
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.backbutton"));
        }
        
        if(!errors.isEmpty())
            this.saveErrors(request, errors);

        if(log.isDebugEnabled())
            log.debug("use forward: "+forward);
        
        return forward;
    }

    private boolean isEditable(EditPanel panel) {
        boolean editable = panel.isEditable();
        
        if(panel.getActivePage().getEditable() != null) {
            editable = panel.getActivePage().getEditable();
        }
        
        return editable;
    }
    
    public final ActionForward findPageForward(ActionMapping mapping, E form, HttpServletRequest request)
    {
        if(form.getDoTab() != null)
            return Panel.getForward(form.getDoTab().getAction(), request);

        return mapping.getInputForward();
    }
    
    protected ActionForward handleCustomAction(ActionMapping mapping, E form, ActionMessages errors, HttpServletRequest request)
    {
        return null;
    }
    
    protected void readForm(E form, ActionMessages errors, HttpServletRequest request)
    {
        
    }
    
    protected void writeForm(E form, ActionMessages errors, HttpServletRequest request)
    {
        
    }
    
    protected void postOperations(E form, ActionMessages errors, HttpServletRequest request)
    {
        
    }
    
    protected void preOperations(E form, ActionMessages errors, HttpServletRequest request)
    {
        
    }
    
    protected void preSearch(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response) {
        
    }
    
    public final EditPanel getPanel()
    {
        return panel;
    }
}
