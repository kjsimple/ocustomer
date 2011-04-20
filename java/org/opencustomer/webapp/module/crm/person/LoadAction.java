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

package org.opencustomer.webapp.module.crm.person;

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
import org.opencustomer.db.dao.crm.PersonDAO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditLoadAction;
import org.opencustomer.webapp.auth.Right;
import org.opencustomer.webapp.module.crm.company.DeleteAction;

public class LoadAction extends EditLoadAction<LoadForm> {
    private static Logger log = Logger.getLogger(LoadAction.class);

    @Override
    public EditPanel createPanel(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PersonVO person = (PersonVO)attributes.get("person");

        EditPanel panel = new EditPanel(Right.CRM_PERSONS_WRITE, person);
        boolean contactSelectable = true;
        boolean deletable = true;
        
        if(person.getId() == null) {
            panel.setTitle(MessageUtil.message(request, "module.crm.person.headLine.create"));
            contactSelectable = false;
        } else {
            String name = new String(person.getFirstName()+" "+person.getLastName());
            if(person.getTitle() != null)
                name = person.getTitle()+" "+name;
            if(person.getNameAffix() != null)
                name = name+" "+person.getNameAffix();
            
            panel.setTitle(MessageUtil.message(request, "module.crm.person.headLine.edit", name));

            panel.setAttribute(DeleteAction.TEXT_TITLE, MessageUtil.message(request, "module.crm.person.delete.headLine", person.getLastName(), person.getFirstName()));
            panel.setAttribute(DeleteAction.TEXT_QUESTION, MessageUtil.message(request, "module.crm.person.delete.question", person.getLastName(), person.getFirstName()));
        }
        
        if(person.getPersonContacts().size() > 0 || person.getJobs().size() > 0)
            deletable = false;
        
        panel.addAction(Action.Type.DELETE, "/crm/person/delete", deletable);
        panel.addAction(Action.Type.SAVE, "/crm/person/save");
        
        if(SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.SHOW_OVERVIEW)) {
            panel.addPage("OVERVIEW", "/crm/person/pageOverview",  "module.generic.panel.tab.overview");
        }
        panel.addPage("STANDARD", "/crm/person/pageStandard", "module.crm.person.pageStandard");
        panel.addPage("ADDRESS",  "/crm/person/pageAddress", "module.crm.person.pageAddress");
        panel.addPage("CONTACT",  "/crm/person/pageContact", "module.crm.person.pageContact", contactSelectable, true);
        panel.addPage("JOBS",     "/crm/person/pageJob", "module.generic.panel.tab.crm.jobs", contactSelectable, true);
        panel.addPage("SYSTEM",   "/crm/person/pageSystem",   "module.generic.panel.tab.system");
        
        if(person.getId() == null) {
            panel.activatePage(panel.findPage("STANDARD"));
        }
        
        return panel;
    }
    
    @Override
    public void loadEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PersonDAO dao = new PersonDAO();
        try {
            PersonVO person = dao.getById(form.getId());
            Hibernate.initialize(person.getCompany());
            if(person.getCompany() != null)
                Hibernate.initialize(person.getCompany().getAddresses());
            Hibernate.initialize(person.getAddresses());
            
            attributes.put("person", person);
        } catch (HibernateException e) {
            log.debug("problems loading person (ID:" + form.getId() + ")");

            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));
        }
    }
    
    @Override
    public void createEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        Panel lastPanel = null;
        if(!Panel.getPanelStack(request).isEmpty())
            lastPanel = Panel.getPanelStack(request).peek();
        
        PersonVO person = new PersonVO();
        
        if(lastPanel != null && lastPanel instanceof EditPanel && ((EditPanel)lastPanel).getEntity() instanceof CompanyVO) {
            person.setCompany((CompanyVO)((EditPanel)lastPanel).getEntity());
        }
        
        person.setAccessUser(EntityAccess.Access.WRITE_SYSTEM);
        person.setOwnerUser(activeUser.getId());
        person.setAccessGroup(EntityAccess.Access.NONE);
        person.setOwnerGroup(activeUser.getProfile().getDefaultUsergroup().getId());
        person.setAccessGlobal(EntityAccess.Access.WRITE_SYSTEM);
        
        attributes.put("person", person);
    }
}
