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

package org.opencustomer.webapp.module.system.management.role.add;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.UsergroupDAO;
import org.opencustomer.db.vo.system.RoleVO;
import org.opencustomer.db.vo.system.UsergroupVO;
import org.opencustomer.framework.webapp.panel.EntityPanel;
import org.opencustomer.webapp.module.generic.add.AddUsergroupForm;

public class AddUsergroupAction extends org.opencustomer.webapp.module.generic.add.AddUsergroupAction<AddUsergroupForm>
{
    private static Logger log = Logger.getLogger(AddUsergroupAction.class);

    @Override
    protected String getPath() {
        return "/system/management/role/addUsergroup";
    }
    
    @Override
    protected void chooseEntity(EntityPanel panel, AddUsergroupForm form, ActionMessages errors, HttpServletRequest request, HttpServletResponse response)
    {
        if (log.isDebugEnabled())
            log.debug("add usergroup with ID: " + form.getId());

        try
        {
            RoleVO role = (RoleVO) panel.getEntity();

            UsergroupVO usergroup = new UsergroupDAO().getById(form.getId());
            
            role.setDefaultUsergroup(usergroup);
        }
        catch (HibernateException e)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));

            log.error("problems loading usergroup (ID:" + form.getId() + ")");
        }
    }
    
    @Override
    protected boolean isAdmin(EntityPanel panel) {
        RoleVO role = (RoleVO) panel.getEntity();

        return role.isAdmin();
    }
}
