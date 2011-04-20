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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.crm.CompanyDAO;
import org.opencustomer.db.vo.crm.CompanyVO;
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

        CompanyVO company = (CompanyVO) panel.getEntity();

        try {
            // speicher die Firma
            if (company.getId() == null)
                new CompanyDAO().insert(company);
            else
                new CompanyDAO().update(company);
        } catch (HibernateException e) {
            log.error("problems saving company (id:" + company.getId() + ")", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.rollback"));
        }
    }
    
    @Override
    protected ActionForward getActionForward(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
        if(request.getAttribute("external_person_id") != null)
            return mapping.findForward("jumpPerson");
        else if(request.getAttribute("external_company_id") != null)
            return mapping.findForward("jumpCompany");
        else if(request.getAttribute(Globals.JUMP_KEY) != null) {
            JumpBean jump = (JumpBean)request.getAttribute(Globals.JUMP_KEY);
            return jump.getForward();
        } else
            return null;
    }

    @Override
    protected boolean validateData(EditPanel panel, ActionMessages errors, HttpServletRequest request) {
        CompanyVO company = (CompanyVO) panel.getEntity();

        // Abfangen der möglichen Fehler
        if (company.getCompanyName() == null) {
            errors.add("companyName", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.company.companyName")));
        } else {
            try {
                CompanyDAO dao = new CompanyDAO();

                CompanyVO namedCompany = dao.getByName(company.getCompanyName());
                if (namedCompany != null && !namedCompany.getId().equals(company.getId()))
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.crm.company.error.nameExists"));

                HibernateContext.getSession().evict(namedCompany);
            } catch (HibernateException e) {
                log.error("could not read company for name: " + company.getCompanyName());
            }
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
            if(panel.getEntity() instanceof CompanyVO)
                return panel;
            else
                return Panel.getPanelStack(request).peek(2);
        } else {
            return super.getPanel(request);
        }
    }    
}
