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

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.crm.JobDAO;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditLoadAction;
import org.opencustomer.webapp.action.EditLoadForm;
import org.opencustomer.webapp.auth.Right;
import org.opencustomer.webapp.module.crm.company.DeleteAction;

public class LoadAction extends EditLoadAction<EditLoadForm>
{
    private static Logger log = Logger.getLogger(LoadAction.class);

    public EditPanel createPanel(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        JobVO job = (JobVO)attributes.get("job");

        EditPanel panel = new EditPanel(Right.CRM_JOBS_WRITE, job);
        
        if(job.getId() == null)
            panel.setTitle(MessageUtil.message(request, "module.crm.job.create.title"));
        else
        {
            panel.setTitle(MessageUtil.message(request, "module.crm.job.edit.title", job.getSubject()));
            
            panel.setAttribute(DeleteAction.TEXT_TITLE, MessageUtil.message(request, "module.crm.job.delete.title", job.getSubject()));
            panel.setAttribute(DeleteAction.TEXT_QUESTION, MessageUtil.message(request, "module.crm.job.delete.question", job.getSubject()));
        }
        
        panel.addAction(Action.Type.SAVE, "/crm/job/save");
        panel.addAction(Action.Type.DELETE, "/crm/job/delete");
        
        panel.addPage("STANDARD", "/crm/job/pageStandard", "module.generic.panel.tab.standard");
        panel.addPage("SYSTEM",   "/crm/job/pageSystem",   "module.generic.panel.tab.system");
        
        return panel;
    }
    
    @Override
    public void createEntity(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        
        JobVO job = new JobVO();
        job.setStatus(JobVO.Status.PLANNED);
        job.setPriority(JobVO.Priority.MEDIUM);
        
        job.setAccessUser(EntityAccess.Access.WRITE_SYSTEM);
        job.setOwnerUser(activeUser.getId());
        job.setAccessGroup(EntityAccess.Access.NONE);
        job.setOwnerGroup(activeUser.getProfile().getDefaultUsergroup().getId());
        job.setAccessGlobal(EntityAccess.Access.WRITE_SYSTEM);
        
        if(Panel.getPanelStack(request).peek() instanceof EditPanel) {
            EditPanel lastPanel = (EditPanel)Panel.getPanelStack(request).peek();
            
            if(lastPanel.getEntity() instanceof CompanyVO) {
                if (log.isDebugEnabled())
                    log.debug("contact for company");
    
                CompanyVO company = (CompanyVO) lastPanel.getEntity();
                job.setReferencedCompany(company);
            } else if(lastPanel.getEntity() instanceof PersonVO) {
                if (log.isDebugEnabled())
                    log.debug("contact for person");
    
                PersonVO person = (PersonVO)lastPanel.getEntity();
                job.setReferencedPerson(person);
                job.setReferencedCompany(person.getCompany());
            } else {
                log.warn("unknown entity found in panel: "+lastPanel);
            }    
        }
        
        job.setAssignedUser(activeUser);
                
        attributes.put("job", job);
    }
    
    @Override
    public void loadEntity(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            JobVO job = new JobDAO().getById(form.getId());
            Hibernate.initialize(job.getReferencedCompany());
            Hibernate.initialize(job.getReferencedPerson());
            
            attributes.put("job", job);
        }
        catch (HibernateException e)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));

            log.debug("problems loading job (ID:" + form.getId() + ")");
        }
    }
}
