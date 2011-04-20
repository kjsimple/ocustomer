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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.crm.CategoryDAO;
import org.opencustomer.db.dao.crm.CompanyStateDAO;
import org.opencustomer.db.dao.crm.CompanyTypeDAO;
import org.opencustomer.db.dao.crm.LegalFormDAO;
import org.opencustomer.db.dao.crm.RatingDAO;
import org.opencustomer.db.dao.crm.SectorDAO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.framework.db.vo.BaseVO;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.webapp.action.EditPageAction;

public class PageStandardAction extends EditPageAction<PageStandardForm>
{
    private static Logger log = Logger.getLogger(PageStandardAction.class);

    @Override
    public void writeForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        CompanyVO company = (CompanyVO)getPanel().getAttribute("company");

        form.setCompanyName(company.getCompanyName());
        form.setUrl(company.getUrl());
        form.setPhone(company.getPhone());
        form.setFax(company.getFax());
        form.setEmail(company.getEmail());
        form.setComment(company.getComment());
        if (company.getStaffCount() != null)
            form.setStaffCount(company.getStaffCount().toString());

        if (company.getSector() != null)
            form.setSectorId(company.getSector().getId());
        if (company.getLegalForm() != null)
            form.setLegalFormId(company.getLegalForm().getId());
        if (company.getCompanyState() != null)
            form.setCompanyStateId(company.getCompanyState().getId());
        if (company.getCompanyType() != null)
            form.setCompanyTypeId(company.getCompanyType().getId());
        if (company.getCategory() != null)
            form.setCategoryId(company.getCategory().getId());
        if (company.getRating() != null)
            form.setRatingId(company.getRating().getId());
    }
    
    @Override
    public void readForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        CompanyVO company = (CompanyVO) getPanel().getAttribute("company");

        if (log.isDebugEnabled())
            log.debug("save button pressed");

        company.setCompanyName(form.getCompanyName());
        company.setUrl(form.getUrl());
        company.setPhone(form.getPhone());
        company.setFax(form.getFax());
        company.setEmail(form.getEmail());
        company.setComment(form.getComment());
        if (form.getStaffCount() != null)
            company.setStaffCount(new Integer(form.getStaffCount()));
        else
            company.setStaffCount(null);

        try
        {
            if(form.getSectorId() > 0) {
                if(isEntityChanged(form.getSectorId(), company.getSector()))
                    company.setSector(new SectorDAO().getById(form.getSectorId()));
            } else {
                company.setSector(null);
            }
            
            if(form.getLegalFormId() > 0) {
                if(isEntityChanged(form.getLegalFormId(), company.getLegalForm()))
                    company.setLegalForm(new LegalFormDAO().getById(form.getLegalFormId()));
            } else {
                company.setLegalForm(null);
            }
            
            if(form.getCompanyTypeId() > 0) {
                if(isEntityChanged(form.getCompanyTypeId(), company.getCompanyType()))
                    company.setCompanyType(new CompanyTypeDAO().getById(form.getCompanyTypeId()));
            } else {
                company.setCompanyType(null);
            }
            
            if(form.getCompanyStateId() > 0) {
                if(isEntityChanged(form.getCompanyStateId(), company.getCompanyState()))
                    company.setCompanyState(new CompanyStateDAO().getById(form.getCompanyStateId()));
            } else {
                company.setCompanyState(null);
            }
            
            if(form.getCategoryId() > 0) {
                if(isEntityChanged(form.getCategoryId(), company.getCategory()))
                    company.setCategory(new CategoryDAO().getById(form.getCategoryId()));
            } else {
                company.setCategory(null);
            }
            
            if(form.getRatingId() > 0) {
                if(isEntityChanged(form.getRatingId(), company.getRating()))
                    company.setRating(new RatingDAO().getById(form.getRatingId()));
            } else {
                company.setRating(null);
            }

        }
        catch (HibernateException e)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalid"));
        }
    }

    private boolean isEntityChanged(int id, BaseVO vo) {
        boolean changed = false;
        
        if(id <= 0 && vo == null) {
            changed = false;
        } else if(id > 0 && vo == null) {
            changed = true;
        } else if (id <= 0 && vo != null) {
            changed = true;
        } else if (id != vo.getId()) {
            changed = true;
        }
        
        return changed;
    }
    
    @Override
    protected ActionForward handleCustomAction(ActionMapping mapping, PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        if(form.getDoAddUser().isSelected()) {
            if (log.isDebugEnabled())
                log.debug("add user");
            
            return mapping.findForward("addUser");
        } else if(form.getDoRemoveUser().isSelected()) {
            if (log.isDebugEnabled())
                log.debug("remove user");
            
            CompanyVO company = (CompanyVO)getPanel().getEntity();
            company.setAssignedUser(null);
            
            return Panel.getForward(getPanel().getActivePage().getAction(), request);
        } else {
            return null;
        }
    }
}