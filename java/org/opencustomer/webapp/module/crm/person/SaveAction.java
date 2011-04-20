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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.crm.PersonDAO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.webapp.action.JumpBean;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditSaveAction;

public class SaveAction extends EditSaveAction {
    private static Logger log = Logger.getLogger(SaveAction.class);

    @Override
    protected void saveEntity(EditPanel panel, ActionMessages errors, HttpServletRequest request) {
        UserVO user = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        
        try {
            PersonVO person = (PersonVO)panel.getEntity();
            
            if (person.getId() == null) {
                if (log.isDebugEnabled())
                    log.debug("create person");

                new PersonDAO().insert(person);
            } else {
                if (log.isDebugEnabled())
                    log.debug("save person (ID:" + person.getId() + ")");

                new PersonDAO().update(person);
            }
        } catch (HibernateException e) {
            log.error("problems saving person", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.rollback"));
        }
    }
    
    @Override
    protected ActionForward getActionForward(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
        if(request.getAttribute("external_company_id") != null) {
            return mapping.findForward("jumpCompany");
        } else if(request.getAttribute("external_person_id") != null) {
            return mapping.findForward("jumpPerson");
        } else if(request.getAttribute(Globals.JUMP_KEY) != null) {
            JumpBean jump = (JumpBean)request.getAttribute(Globals.JUMP_KEY);
            return jump.getForward();
        } else {
            return null;
        }
    }
    
    @Override
    protected boolean validateData(EditPanel panel, ActionMessages errors, HttpServletRequest request) {
        PersonVO person = (PersonVO)panel.getEntity();

        if (person.getFirstName() == null)
            errors.add("firstName", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.person.firstName")));
        if (person.getLastName() == null)
            errors.add("lastName", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.person.lastName")));

        if (person.getFirstName() != null && person.getLastName() != null) {
            try {
                PersonDAO dao = new PersonDAO();

                PersonVO foundPerson = dao.getByName(person.getFirstName(), person.getLastName());

                if (foundPerson != null && !foundPerson.getId().equals(person.getId()))
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.crm.person.error.nameExists"));

                HibernateContext.getSession().evict(foundPerson);
            } catch (HibernateException e) {
                log.error("could not check existence of firstName and lastName", e);
            }
        }

        // Abfangen der unmöglichen Fehler
        if (!PersonVO.Gender.MALE.equals(person.getGender()) && !PersonVO.Gender.FEMALE.equals(person.getGender()) && !PersonVO.Gender.UNKNOWN.equals(person.getGender())) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalid"));
        }
        
        if (errors.isEmpty())
            return true;
        else
            return false;
    }
    
    @Override
    protected Panel getPanel(HttpServletRequest request) {
        if(request.getAttribute("external_company_id") != null
                || request.getAttribute("external_person_id") != null
                || request.getAttribute(Globals.JUMP_KEY) != null) {
            EditPanel panel = (EditPanel)Panel.getPanelStack(request).peek();
            if(panel.getEntity() instanceof PersonVO)
                return panel;
            else
                return Panel.getPanelStack(request).peek(2);
        } else {
            return super.getPanel(request);
        }
    }
}