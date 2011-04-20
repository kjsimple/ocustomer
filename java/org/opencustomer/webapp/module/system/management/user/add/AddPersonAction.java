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

package org.opencustomer.webapp.module.system.management.user.add;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.crm.PersonDAO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.webapp.panel.EntityPanel;
import org.opencustomer.webapp.module.generic.add.AddPersonForm;

public final class AddPersonAction extends org.opencustomer.webapp.module.generic.add.AddPersonAction<AddPersonForm>
{
    private static Logger log = Logger.getLogger(AddPersonAction.class);

    @Override
    public String getPath() {
        return "/system/management/user/addPerson";
    }
    
    @Override
    protected void chooseEntity(EntityPanel panel, AddPersonForm form, ActionMessages errors, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            UserVO user = (UserVO) panel.getEntity();
            
            if(!user.getProfile().getRole().isAdmin()) { 
                PersonVO person = new PersonDAO().getById(form.getId());
                user.setPerson(person);
            }
        }
        catch (HibernateException e)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", form.getId()));

            log.error("problems loading person (ID:" + form.getId() + ")");
        }
    }

    @Override
    protected List<PersonVO> getIgnoredPersons(EntityPanel panel){
        UserVO user = (UserVO) panel.getEntity();
        
        List<PersonVO> ignoredPersons = new ArrayList<PersonVO>();
        for(PersonVO person : new PersonDAO().getWithUser(user.getPerson()))
            ignoredPersons.add(person);
        
        return ignoredPersons;
    }
}