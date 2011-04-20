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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.crm.ContactVO;
import org.opencustomer.db.vo.crm.PersonContactVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.util.EnumUtility;
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
        ContactVO contact = (ContactVO) getPanel().getEntity();

        contact.setSubject(form.getSubject());
        contact.setContent(form.getContent());
        contact.setContactName(form.getContactName());
        contact.setContactType(EnumUtility.valueOf(ContactVO.ContactType.class, form.getContactType()));
        contact.setBoundType(EnumUtility.valueOf(ContactVO.BoundType.class, form.getBoundType()));

        SimpleDateFormat sdf = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.dateTime"));
        try
        {
            contact.setContactTimestamp(sdf.parse(form.getContactTime()));
        }
        catch (ParseException e)
        {
            log.error("problems parsing contact timestamp", e);
        }
    }
    
    @Override
    public void writeForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        ContactVO contact = (ContactVO)getPanel().getEntity();

        form.setSubject(contact.getSubject());
        form.setContent(contact.getContent());
        form.setContactName(contact.getContactName());
        if (contact.getContactType() != null)
            form.setContactType(contact.getContactType().toString());
        if (contact.getBoundType() != null)
            form.setBoundType(contact.getBoundType().toString());

        if (contact.getContactTimestamp() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.dateTime"));
            form.setContactTime(sdf.format(contact.getContactTimestamp()));
        }
    }
    
    @Override
    public ActionForward handleCustomAction(ActionMapping mapping, PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        if (log.isDebugEnabled())
            log.debug("handleCustomAction");
        
        if (form.getDoAddOwnPerson().isSelected())
        {
            if (log.isDebugEnabled())
                log.debug("add own person");

            UserVO user = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
            ContactVO contact = (ContactVO) getPanel().getEntity();

            if (user.getPerson() != null)
            {
                PersonContactVO pc = new PersonContactVO(user.getPerson(), contact);

                if (!contact.getPersonContacts().contains(pc))
                    contact.getPersonContacts().add(pc);
            }

            return Panel.getForward(getPanel().getActivePage().getAction(), request);
        }
        else if(form.getDoAddPerson().isSelected())
        {
            if (log.isDebugEnabled())
                log.debug("add person");
            
            return mapping.findForward("addPerson");
        }
        else if(form.getDoAddCompany().isSelected())
        {
            if (log.isDebugEnabled())
                log.debug("add company");
            
            return mapping.findForward("addCompany");
        }
        else if(form.getDoRemovePerson() > 0)
        {
            if (log.isDebugEnabled())
                log.debug("remove person with id: "+form.getDoRemovePerson());          
            
            ContactVO contact = (ContactVO) getPanel().getEntity();
            
            if(contact.getPersonContacts().size() > 1 || contact.getCompany() != null)
            {
                Iterator<PersonContactVO> it = contact.getPersonContacts().iterator();
                while(it.hasNext()) {
                    if(it.next().getPerson().getId().intValue() == form.getDoRemovePerson()) {
                        it.remove();
                        break;
                    }
                }
            }
            else 
                log.info("tried to delete last person of contact / no company available");
            
            return Panel.getForward(getPanel().getActivePage().getAction(), request);
        }
        else if(form.getDoRemoveCompany().isSelected()) {
            if (log.isDebugEnabled())
                log.debug("remove company");
            
            ContactVO contact = (ContactVO) getPanel().getEntity();
            if (contact.getPersonContacts().size() > 0)
                contact.setCompany(null);
            else
                log.info("tried to delete company of contact / no persons available");
            
            return Panel.getForward(getPanel().getActivePage().getAction(), request);
        }
        else if(form.getJumpPersonId() > 0)
        {
            if (log.isDebugEnabled())
                log.debug("jump to person with id: "+form.getJumpPersonId());
            
            request.setAttribute("external_person_id", form.getJumpPersonId());
            
            return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
        }
        else if(form.getDoJumpCompany().isSelected())
        {
            CompanyVO company = ((ContactVO)getPanel().getAttribute("contact")).getCompany();
            
            if (log.isDebugEnabled())
                log.debug("jump to company with id: "+company.getId());
            
            request.setAttribute("external_company_id", company.getId());
            
            return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
        }
        else 
        {
            if (log.isDebugEnabled())
                log.debug("no custom action found");
            
            return null;
        }
    }
    
}
