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

package org.opencustomer.webapp.module.crm.company.add;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.webapp.panel.EntityPanel;
import org.opencustomer.webapp.module.generic.add.AddUserForm;

public final class AddUserAction extends org.opencustomer.webapp.module.generic.add.AddUserAction<AddUserForm>
{
    private static Logger log = Logger.getLogger(AddUserAction.class);

    protected String getPath() {
        return "/crm/company/addUser";
    }
    
    @Override
    protected void chooseEntity(EntityPanel panel, AddUserForm form, ActionMessages errors, HttpServletRequest request, HttpServletResponse response)
    {
        if (log.isDebugEnabled())
            log.debug("add user with ID: " + form.getId());

        UserVO user = new UserDAO().getById(form.getId());

        if (user == null)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));

            log.error("problems loading user (ID:" + form.getId() + ")");
        }
        else
        {
            CompanyVO company = (CompanyVO) panel.getEntity();
            
            if(!EntityAccessUtility.isAccessGranted(user, company, EntityAccess.Access.WRITE))
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.generic.pageSystem.error.missingWriteAccessForAssignedUser", user.getUserName()));
            else
                company.setAssignedUser(user);
        }
    }
}
