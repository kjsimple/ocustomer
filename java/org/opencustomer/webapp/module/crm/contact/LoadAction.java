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

package org.opencustomer.webapp.module.crm.contact;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.crm.ContactDAO;
import org.opencustomer.db.dao.system.UsergroupDAO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.crm.ContactVO;
import org.opencustomer.db.vo.crm.PersonContactVO;
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
        ContactVO contact = (ContactVO)attributes.get("contact");

        EditPanel panel = new EditPanel(Right.CRM_CONTACTS_WRITE, contact);
        
        if(contact.getId() == null)
            panel.setTitle(MessageUtil.message(request, "module.crm.contact.headLine.create"));
        else
        {
            panel.setTitle(MessageUtil.message(request, "module.crm.contact.headLine.edit", contact.getSubject()));
            
            panel.setAttribute(DeleteAction.TEXT_TITLE, MessageUtil.message(request, "module.crm.contact.delete.headLine", contact.getSubject()));
            panel.setAttribute(DeleteAction.TEXT_QUESTION, MessageUtil.message(request, "module.crm.contact.delete.question", contact.getSubject()));
        }
        
        EditPanel lastPanel = (EditPanel)Panel.getPanelStack(request).peek();
        
        panel.addAction(Action.Type.SAVE, "/crm/contact/save");
        panel.addAction(Action.Type.DELETE, "/crm/contact/delete");
        
        panel.addPage("STANDARD", "/crm/contact/pageStandard", "module.generic.panel.tab.standard");
        panel.addPage("SYSTEM",   "/crm/contact/pageSystem",   "module.generic.panel.tab.system");
        
        return panel;
    }
    
    @Override
    public void createEntity(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        
        ContactVO contact = new ContactVO();
        contact.setContactTimestamp(new Date());
        
        contact.setAccessUser(EntityAccess.Access.WRITE_SYSTEM);
        contact.setOwnerUser(activeUser.getId());
        contact.setAccessGroup(EntityAccess.Access.NONE);
        contact.setOwnerGroup(activeUser.getProfile().getDefaultUsergroup().getId());
        contact.setAccessGlobal(EntityAccess.Access.WRITE_SYSTEM);
        
        EditPanel lastPanel = (EditPanel)Panel.getPanelStack(request).peek();
        
        if(lastPanel.getEntity() instanceof CompanyVO)
        {
            if (log.isDebugEnabled())
                log.debug("contact for company");

            CompanyVO company = (CompanyVO) lastPanel.getEntity();
            contact.setCompany(company);
        }
        else if(lastPanel.getEntity() instanceof PersonVO)
        {
            if (log.isDebugEnabled())
                log.debug("contact for person");

            PersonVO person = (PersonVO)lastPanel.getEntity();
            contact.getPersonContacts().add(new PersonContactVO(person, contact));
        }
        else
        {
            log.error("unknown entity foundin panel: "+lastPanel);
        }    
        
        // add own user by default
        if(activeUser.getPerson() != null) {
            PersonVO person = activeUser.getPerson();
            boolean containsPerson = false;
            for(PersonContactVO personContact : contact.getPersonContacts()) {
                if(person.equals(personContact.getPerson())) {
                    containsPerson = true;
                }
            }
            
            if(!containsPerson) {
                contact.getPersonContacts().add(new PersonContactVO(person, contact));
            }
        }
        
        attributes.put("contact", contact);
    }
    
    @Override
    public void loadEntity(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            ContactVO contact = new ContactDAO().getById(form.getId());
            Hibernate.initialize(contact.getPersonContacts());
            
            attributes.put("contact", contact);
        }
        catch (HibernateException e)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));

            log.debug("problems loading contact (ID:" + form.getId() + ")");
        }
    }
    
    @Override
    public void loadEnvironment(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        attributes.put("usergroups", new UsergroupDAO().getAll());
    }
}
