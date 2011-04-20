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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Page;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.struts.ActionForm;

public abstract class EditPageForm extends ActionForm
{
    private final static Logger log = Logger.getLogger(EditPageForm.class);
    
    private String doSave;

    private String doCancel;

    private String doDelete;
    
    private Page doTab;
    
    private EditPanel panel;
    
    private boolean disabled;
    
    @Override
    public final boolean process(ActionMapping mapping, ActionMessages error, HttpServletRequest request)
    {
        boolean validate = true;

        if(!Panel.getPanelStack(request).isEmpty() && Panel.validate(request)) 
        {
            panel = (EditPanel)Panel.getPanelStack(request).peek();

            boolean reset = true;
            if(doTab == null && panel != null)
            {
                Page newTab = panel.findPage(request);
                if(newTab != null)
                {
                    if(newTab.isSelectable())
                    {
                        doTab = newTab;
                        reset = false;
                    }    
                    else
                        error.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalid"));
                }
            }
            
            if(reset)
                doTab = null;
            
            // the buttons can only be used, if the actions are defined
            if(doSave != null && getPanel().getAction(Action.Type.SAVE) == null)
                doSave = null;
            if(doDelete != null && getPanel().getAction(Action.Type.DELETE) == null)
                doDelete = null;
            
            disabled = !panel.isEditable();
            
            if (!isTokenValid(request))
            {
                if (log.isDebugEnabled())
                    log.debug("token is not valid");
                
                validate = false;
            }
            else if(doCancel != null)
            {
                if (log.isDebugEnabled())
                    log.debug("use button: cancel");
                
                validate = false;
            }
            else if(doDelete != null)
            {
                if (log.isDebugEnabled())
                    log.debug("use button: delete");
                
                validate = false;
            }
            
            if(isEditable(panel))
            {
                if(forceValidation())
                    return true;
                else
                    return validate;
            }
            else
                return false;
        }
        else 
            return false;
    }

    private boolean isEditable(EditPanel panel) {
        boolean editable = panel.isEditable();
        
        if(panel.getActivePage().getEditable() != null) {
            editable = panel.getActivePage().getEditable();
        }
        
        return editable;
    }
    
    protected boolean forceValidation()
    {
        return false;
    }
    
    protected EditPanel getPanel()
    {
        return panel;
    }

    protected boolean isDisabled()
    {
        return disabled;
    }
       
    public Page getDoTab()
    {
        return doTab;
    }

    public void setDoTab(Page doTab)
    {
        this.doTab = doTab;
    }
    
    public String getDoCancel()
    {
        return doCancel;
    }

    public void setDoCancel(String doCancel)
    {
        this.doCancel = doCancel;
    }

    public String getDoDelete()
    {
        return doDelete;
    }

    public void setDoDelete(String doDelete)
    {
        this.doDelete = doDelete;
    }

    public String getDoSave()
    {
        return doSave;
    }

    public void setDoSave(String doSave)
    {
        this.doSave = doSave;
    }
    
}
