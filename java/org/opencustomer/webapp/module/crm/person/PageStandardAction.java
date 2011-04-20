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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.framework.util.EnumUtility;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageAction;

public class PageStandardAction extends EditPageAction<PageStandardForm>
{
    private static Logger log = Logger.getLogger(PageStandardAction.class);

    @Override
    public void writeForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        PersonVO personVO = (PersonVO) getPanel().getEntity();

        if(personVO.getType() != null)
            form.setType(personVO.getType().toString());
        
        form.setFirstName(personVO.getFirstName());
        form.setLastName(personVO.getLastName());
        if(personVO.getGender() != null)
            form.setGender(personVO.getGender().toString());
        form.setNameAffix(personVO.getNameAffix());
        form.setTitle(personVO.getTitle());


        form.setEmail(personVO.getEmail());
        form.setPhone(personVO.getPhone());
        form.setMobile(personVO.getMobile());
        form.setFax(personVO.getFax());
        form.setUrl(personVO.getUrl());
        
        form.setComment(personVO.getComment());

        form.setDegree(personVO.getDegree());
        form.setFunction(personVO.getFunction());
        
        if(personVO.getDayOfBirth() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.date"));
            form.setDayOfBirth(sdf.format(personVO.getDayOfBirth()));
        }
    }
    
    @Override
    public void readForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        PersonVO personVO = (PersonVO) getPanel().getEntity();

        personVO.setType(EnumUtility.valueOf(PersonVO.Type.class, form.getType()));
        
        personVO.setFirstName(form.getFirstName());
        personVO.setLastName(form.getLastName());
        personVO.setGender(EnumUtility.valueOf(PersonVO.Gender.class, form.getGender()));
        personVO.setNameAffix(form.getNameAffix());
        personVO.setTitle(form.getTitle());

        personVO.setEmail(form.getEmail());
        personVO.setPhone(form.getPhone());
        personVO.setMobile(form.getMobile());
        personVO.setFax(form.getFax());
        personVO.setUrl(form.getUrl());

        personVO.setComment(form.getComment());
        
        personVO.setDegree(form.getDegree());
        personVO.setFunction(form.getFunction());

        if(form.getDayOfBirth() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.date"));
            try {
                personVO.setDayOfBirth(sdf.parse(form.getDayOfBirth()));
            } catch (ParseException e) {
                log.error("problems parsing day of birth", e);
            }
        }
    }
    
    @Override
    public ActionForward handleCustomAction(ActionMapping mapping, PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        if (form.getDoAddCompany().isSelected())
        {
            if(log.isDebugEnabled())
                log.debug("add company");
            
            return mapping.findForward("addCompany");
        }
        else if (form.getDoJumpCompany().isSelected())
        {
            CompanyVO company = ((PersonVO) getPanel().getEntity()).getCompany();

            if(log.isDebugEnabled())
                log.debug("jump to company with id: "+company.getId());
            
            request.setAttribute("external_company_id", company.getId());
            
            return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
        }
        else if (form.getDoRemoveCompany().isSelected())
        {
            if (log.isDebugEnabled())
                log.debug("remove company");

            PersonVO person = (PersonVO)getPanel().getEntity();
            person.setCompany(null);
            
            return Panel.getForward(getPanel().getActivePage().getAction(), request);
        }
        else
        {
            if(log.isDebugEnabled())
                log.debug("no custom action found");
        }
        
        return null;
    }
}
