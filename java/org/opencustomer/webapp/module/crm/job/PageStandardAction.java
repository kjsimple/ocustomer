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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.framework.util.EnumUtility;
import org.opencustomer.framework.webapp.action.JumpBean;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditPageAction;

public class PageStandardAction extends EditPageAction<PageStandardForm>
{
    private static Logger log = Logger.getLogger(PageStandardAction.class);

    @Override
    public void readForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        if (log.isDebugEnabled())
            log.debug("read form");

        JobVO job = (JobVO) getPanel().getEntity();

        job.setSubject(form.getSubject());
        job.setInfo(form.getInfo());
        job.setPriority(EnumUtility.valueOf(JobVO.Priority.class, form.getPriority()));
        job.setStatus(EnumUtility.valueOf(JobVO.Status.class, form.getStatus()));

        if(form.getDueDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.dateTime"));
            try {
                job.setDueDate(sdf.parse(form.getDueDate()));
            } catch (ParseException e) {
                log.error("problems parsing contact timestamp", e);
            }
        } else {
            job.setDueDate(null);
        }
    }
    
    @Override
    public void writeForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        if (log.isDebugEnabled())
            log.debug("write form");
        
        JobVO job = (JobVO)getPanel().getEntity();

        form.setSubject(job.getSubject());
        form.setInfo(job.getInfo());
        if (job.getPriority() != null)
            form.setPriority(job.getPriority().toString());
        if (job.getStatus() != null)
            form.setStatus(job.getStatus().toString());

        if (job.getDueDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.dateTime"));
            form.setDueDate(sdf.format(job.getDueDate()));
        } else {
            form.setDueDate(null);
        }
    }
    
    @Override
    protected ActionForward handleCustomAction(ActionMapping mapping, PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        if(form.getDoAddReferencedPerson().isSelected()) {
            if (log.isDebugEnabled())
                log.debug("add person");
            
            return mapping.findForward("addPerson");
        } else if(form.getDoAddReferencedCompany().isSelected()) {
            if (log.isDebugEnabled())
                log.debug("add company");
            
            return mapping.findForward("addCompany");
        } else if(form.getDoRemoveReference().isSelected()) {
            if (log.isDebugEnabled())
                log.debug("remove reference");
            
            JobVO job = (JobVO)getPanel().getEntity();
            job.setReferencedCompany(null);
            job.setReferencedPerson(null);
            
            return Panel.getForward(getPanel().getActivePage().getAction(), request);
        } else if(form.getDoAddUser().isSelected()) {
            if (log.isDebugEnabled())
                log.debug("add user");
            
            return mapping.findForward("addUser");
        } else if(form.getDoJumpReference().isSelected()) {
            if (log.isDebugEnabled())
                log.debug("jump reference");

            JobVO job = (JobVO)getPanel().getEntity();
            JumpBean jump = null;
            
            if(job.getReferencedPerson() != null) {
                jump = new JumpBean(job.getReferencedPerson(), new ActionForward(Panel.getForward("/crm/person/edit", request)));
            } else if(job.getReferencedCompany() != null) {
                jump = new JumpBean(job.getReferencedCompany(), new ActionForward(Panel.getForward("/crm/company/edit", request)));
            }

            if(jump != null) {
                request.setAttribute(Globals.JUMP_KEY, jump);
                return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
            }
            
        } else {
            if (log.isDebugEnabled())
                log.debug("no custom action found");
        }
        
        return null;
    }
    
}
