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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.crm.CompanyDAO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.webapp.panel.DeletePanel;
import org.opencustomer.webapp.Globals;

public final class DeleteAction extends org.opencustomer.framework.webapp.action.DeleteAction
{
    private static Logger log = Logger.getLogger(DeleteAction.class);
    
    @Override
    protected void deleteEntity(DeletePanel panel, HttpServletRequest request, HttpServletResponse response, ActionMessages errors)
    {
        UserVO user = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);

        try
        {
            new CompanyDAO().delete((CompanyVO)panel.getEntity());
        }
        catch (HibernateException e)
        {
            log.error("could not delete company", e);

            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.couldNotDeleteEntity", panel.getEntity().getId()));
        }
    }
}
