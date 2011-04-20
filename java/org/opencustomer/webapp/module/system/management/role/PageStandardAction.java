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

package org.opencustomer.webapp.module.system.management.role;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.system.RoleVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.UsergroupVO;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditPageAction;

public class PageStandardAction extends EditPageAction<PageStandardForm>
{
    private static Logger log = Logger.getLogger(PageStandardAction.class);

    @Override
    public void readForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        RoleVO role       = (RoleVO)getPanel().getEntity();

        role.setName(form.getName());
        if(role.getId() == null && activeUser.getProfile().getRole().isAdmin())
            role.setAdmin(form.isAdmin());
    }
    
    @Override
    public void writeForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        RoleVO role = (RoleVO) getPanel().getEntity();

        form.setName(role.getName());
        form.setAdmin(role.isAdmin());
    }
    
    @Override
    public ActionForward handleCustomAction(ActionMapping mapping, PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        if(log.isDebugEnabled())
            log.debug("handleCustomAction");
        
        if (form.getDoAddUsergroup().isSelected())
            return mapping.findForward("addUsergroup");
        else if (form.getDoJumpUser() > 0) {
            if(log.isDebugEnabled())
                log.debug("jump to user with id: "+form.getDoJumpUser());
            
            request.setAttribute("external_user_id", form.getDoJumpUser());
            
            return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
        } else if (form.getDoJumpUsergroup().isSelected()) {
            if(log.isDebugEnabled())
                log.debug("jump to usergroup");
            
            UsergroupVO usergroup = ((RoleVO)getPanel().getEntity()).getDefaultUsergroup();
            
            request.setAttribute("external_usergroup_id", usergroup.getId());
            
            return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
        } else {
            if(log.isDebugEnabled())
                log.debug("no custom action found");
        }
        
        return null;
    }

}
