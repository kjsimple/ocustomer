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

package org.opencustomer.webapp.module.system.settings.list;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.system.ListConfigurationVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditPageAction;
import org.opencustomer.webapp.auth.AuthenticatorUtility;
import org.opencustomer.webapp.auth.Right;

public class PageStandardAction extends EditPageAction<PageStandardForm>
{
    private static Logger log = Logger.getLogger(PageStandardAction.class);

    @Override
    public void readForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        UserVO user                           = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        ListConfigurationVO listConfiguration = (ListConfigurationVO) getPanel().getEntity();

        listConfiguration.setName(form.getName());
        listConfiguration.setDescription(form.getDescription());
        if(AuthenticatorUtility.getInstance().authenticate(request, Right.ADMINISTRATION_LIST_GLOBALLIST)) {
            if(form.isGlobal()) 
                listConfiguration.setUser(null);
            else
                listConfiguration.setUser(user);
        }
        listConfiguration.setDefault(form.isDefault());
    }
    
    @Override
    public void writeForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        ListConfigurationVO listConfiguration = (ListConfigurationVO) getPanel().getEntity();

        form.setName(listConfiguration.getName());
        form.setDescription(listConfiguration.getDescription());
        if(listConfiguration.getUser() == null)
            form.setGlobal(true);
        else
            form.setGlobal(false);
        form.setDefault(listConfiguration.isDefault());
    }
}
