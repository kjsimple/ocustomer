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
 * Copyright (C) 2006 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */
package org.opencustomer.framework.webapp.action;

import java.io.IOException;

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
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.webapp.panel.DeletePanel;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.struts.Action;
import org.opencustomer.webapp.Globals;

public abstract class DeleteAction extends Action<DeleteForm> 
{
    private static Logger log = Logger.getLogger(DeleteAction.class);
    
    public final static String TEXT_TITLE = "delete_text_title"; 
    
    public final static String TEXT_QUESTION = "delete_text_question"; 

    private enum Status
    {
        NONE,
        BACK,
        DELETED;
    }
    
    @Override
    public final ActionForward execute(ActionMapping mapping, DeleteForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        DeletePanel panel = null;
        
        if(Panel.getPanelStack(request).isTypeOnTop(Panel.Type.DELETE)) {
            panel = (DeletePanel)Panel.getPanelStack(request).peek();
        } else if(Panel.getPanelStack(request).isTypeOnTop(Panel.Type.EDIT)) {
            EditPanel editPanel = (EditPanel)Panel.getPanelStack(request).peek();
            
            panel = new DeletePanel();
            panel.setTitle((String)editPanel.getAttribute(TEXT_TITLE));
            panel.setQuestion((String)editPanel.getAttribute(TEXT_QUESTION));
            panel.setEntity(editPanel.getEntity());
            
            panel.setPath(editPanel.getAction(org.opencustomer.framework.webapp.panel.Action.Type.DELETE).getAction());
            
            preparePanel(editPanel, panel);
            
            Panel.getPanelStack(request).push(panel);
        } else {
            log.error("missing valid panel: "+Panel.getPanelStack(request).peek());
        }
        
        Status status = Status.NONE;
        
        if(this.isTokenValid(request) && form.getDoDelete() != null)
        {
            ActionMessages errors = new ActionMessages();
            if (log.isDebugEnabled())
                log.debug("delete entity (ID:" + panel.getEntity().getId() + ")");

            if (panel.getEntity().getId() != null) {
                boolean writeAllowed = true;
                
                if(panel.getEntity() instanceof EntityAccess) {
                    UserVO user = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
                    writeAllowed = EntityAccessUtility.isAccessGranted(user, (EntityAccess)panel.getEntity(), EntityAccess.Access.WRITE);
                    
                    if(log.isDebugEnabled())
                        log.debug("delete is allowed on entity for user: "+writeAllowed);
                }
                
                if(writeAllowed) {
                    deleteEntity(panel, request, response, errors);
                } else if(log.isDebugEnabled()) {
                    log.debug("user is not allowed to delete entity "+panel.getEntity());
                }
                    
            } else {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", panel.getEntity().getId()));
            }
            
            if (errors.isEmpty())
                status = Status.DELETED;
            else
                saveErrors(request, errors);
        }
        else if (form.getDoCancel() != null)
        {
            if (log.isDebugEnabled())
                log.debug("cancel button pressed");
            
            status = Status.BACK;
        }

        ActionForward forward = null;
        if(Status.DELETED.equals(status))
        {
            Panel.getPanelStack(request).pop(2);
            forward = Panel.getForward(Panel.getPanelStack(request).peek().getPath(), request);
        }
        else if(Status.BACK.equals(status)) {
            Panel.getPanelStack(request).pop();
            forward = Panel.getForward(Panel.getPanelStack(request).peek().getPath(), request);
        }
        else
            forward = mapping.getInputForward();
        
        return forward;
    }
    
    protected void preparePanel(EditPanel editPanel, DeletePanel deletePanel) {
        
    }
    
    protected abstract void deleteEntity(DeletePanel panel, HttpServletRequest request, HttpServletResponse response, ActionMessages errors);
}
