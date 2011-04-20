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

package org.opencustomer.webapp.module.crm.job;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.crm.JobDAO;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.webapp.action.JumpBean;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.panel.PanelStack;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditSaveAction;

public class SaveAction extends EditSaveAction
{
    private static Logger log = Logger.getLogger(SaveAction.class);

    @Override
    protected void saveEntity(EditPanel panel, ActionMessages errors, HttpServletRequest request)
    {
        try
        {
            JobVO job = (JobVO) panel.getEntity();

            if (job.getId() == null)
            {
                if (log.isDebugEnabled())
                    log.debug("create job " + job);
                new JobDAO().insert(job);
            }
            else
            {
                if (log.isDebugEnabled())
                    log.debug("save job " + job);
                new JobDAO().update(job);
            }

            HibernateContext.getSession().evict(job);
        }
        catch (HibernateException e)
        {
            HibernateContext.rollbackTransaction();
            
            log.error("could not save job", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.rollback"));
        }        
    }
    
    @Override
    protected ActionForward getActionForward(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getAttribute(Globals.JUMP_KEY) != null) {
            JumpBean jump = (JumpBean)request.getAttribute(Globals.JUMP_KEY);
            
            PanelStack panelStack = Panel.getPanelStack(request);
            if(panelStack.peek(2).getType().equals(Panel.Type.EDIT)) {
                EditPanel panel = (EditPanel)panelStack.peek(2);
                
                return Panel.getForward(panel.getAction(Action.Type.SAVE).getAction(), request);
            } else if(panelStack.peek(2).getType().equals(Panel.Type.LIST)) {
                
                return jump.getForward();
            }
            
        }

        
        if(request.getAttribute("external_person_id") != null 
                || request.getAttribute("external_company_id") != null)
        {
            PanelStack panelStack = Panel.getPanelStack(request);
            
            EditPanel panel = (EditPanel)panelStack.peek(2);
            
            return Panel.getForward(panel.getAction(Action.Type.SAVE).getAction(), request);
        }
        else
            return null;
    }
}
