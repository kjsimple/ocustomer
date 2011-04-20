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

package org.opencustomer.webapp.module.crm.company;

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
import org.opencustomer.db.dao.crm.CategoryDAO;
import org.opencustomer.db.dao.crm.CompanyDAO;
import org.opencustomer.db.dao.crm.CompanyStateDAO;
import org.opencustomer.db.dao.crm.CompanyTypeDAO;
import org.opencustomer.db.dao.crm.LegalFormGroupDAO;
import org.opencustomer.db.dao.crm.RatingDAO;
import org.opencustomer.db.dao.crm.SectorDAO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditLoadAction;
import org.opencustomer.webapp.auth.Right;

public class LoadAction extends EditLoadAction<LoadForm> {
    private static Logger log = Logger.getLogger(LoadAction.class);

    @Override
    public EditPanel createPanel(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        CompanyVO company = (CompanyVO)attributes.get("company");

        EditPanel panel = new EditPanel(Right.CRM_COMPANIES_WRITE, company);
        
        boolean deletable = true;
        
        if(company.getId() == null)
            panel.setTitle(MessageUtil.message(request, "module.crm.company.headLine.create"));
        else {
            panel.setTitle(MessageUtil.message(request, "module.crm.company.headLine.edit", company.getCompanyName()));
            
            panel.setAttribute(DeleteAction.TEXT_TITLE, MessageUtil.message(request, "module.crm.company.delete.headLine", company.getCompanyName()));
            panel.setAttribute(DeleteAction.TEXT_QUESTION, MessageUtil.message(request, "module.crm.company.delete.question", company.getCompanyName()));
        }
        
        if(company.getPersons().size() > 0 || company.getPersonContacts().size() > 0 || company.getJobs().size() > 0)
            deletable = false;
        
        panel.addAction(Action.Type.DELETE, "/crm/company/delete", deletable);
        panel.addAction(Action.Type.SAVE, "/crm/company/save");
        
        if(SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.SHOW_OVERVIEW)) {
            panel.addPage("OVERVIEW", "/crm/company/pageOverview",  "module.generic.panel.tab.overview");
        }
        panel.addPage("STANDARD", "/crm/company/pageStandard",  "module.generic.panel.tab.standard");
        panel.addPage("INFO",     "/crm/company/pageInfo",      "module.crm.company.page2");
        panel.addPage("ADDRESS",  "/crm/company/pageAddress",   "module.crm.company.pageAddress");
        panel.addPage("PERSON",   "/crm/company/pagePerson",    "module.crm.company.page3", company.getId() != null);
        panel.addPage("CONTACT",  "/crm/company/pageContact",   "module.crm.company.page4", company.getId() != null, true);
        panel.addPage("JOB",      "/crm/company/pageJob",       "module.generic.panel.tab.crm.jobs", company.getId() != null, true);
        panel.addPage("SYSTEM",   "/crm/company/pageSystem",    "module.generic.panel.tab.system");
        
        if(company.getId() == null) {
            panel.activatePage(panel.findPage("STANDARD"));
        }
        
        return panel;
    }
    
    @Override
    public void createEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        if (log.isDebugEnabled())
            log.debug("new company");
        
        CompanyVO company = new CompanyVO();
        company.setAssignedUser(activeUser);
        
        company.setAccessUser(EntityAccess.Access.WRITE_SYSTEM);
        company.setOwnerUser(activeUser.getId());
        company.setAccessGroup(EntityAccess.Access.NONE);
        company.setOwnerGroup(activeUser.getProfile().getDefaultUsergroup().getId());
        company.setAccessGlobal(EntityAccess.Access.WRITE_SYSTEM);
        
        attributes.put("company", company);
    }
    
    @Override
    public void loadEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (log.isDebugEnabled())
            log.debug("load company (ID:" + form.getId() + ")");

        CompanyVO company = null;
        try {
            company = new CompanyDAO().getById(form.getId());

            Hibernate.initialize(company.getAddresses());
            Hibernate.initialize(company.getPersons());
            Hibernate.initialize(company.getSector());
            Hibernate.initialize(company.getLegalForm());
            Hibernate.initialize(company.getCompanyType());
            Hibernate.initialize(company.getCompanyState());
            Hibernate.initialize(company.getCategory());
            Hibernate.initialize(company.getRating());
        } catch (HibernateException e) {
            log.error("could not load company", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));
        }
        
        attributes.put("company", company);
    }
    
    @Override
    public void loadEnvironment(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        attributes.put("legalFormGroups", new LegalFormGroupDAO().getAll());
        
        attributes.put("sectors", new SectorDAO().getAll());
        attributes.put("companyTypes", new CompanyTypeDAO().getAll());
        attributes.put("companyStates", new CompanyStateDAO().getAll());
        attributes.put("categories", new CategoryDAO().getAll());
        attributes.put("ratings", new RatingDAO().getAll());

    }
}
