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
 * Software-Ingenieurb�ro). Portions created by the Initial Developer are
 * Copyright (C) 2005 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.webapp.module.system.management.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.system.RoleVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.UsergroupVO;
import org.opencustomer.framework.util.LocaleUtility;
import org.opencustomer.framework.util.SignatureUtility;
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
        UserVO user = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);

        UserVO editUser = (UserVO) getPanel().getEntity();
        boolean isActiveUser = false;
        if(getPanel().getAttribute("isActiveUser") != null)
            isActiveUser = (Boolean)getPanel().getAttribute("isActiveUser"); 
        boolean isLdapUser = (Boolean)getPanel().getAttribute("isLdapUser");
        
        if(!isLdapUser) {        
            editUser.setUserName(form.getUserName());
            if(!isActiveUser)
                editUser.getProfile().setLocked(form.isLocked());
            editUser.getProfile().setTimeLock(form.getTimeLock());
            editUser.getProfile().setIpPattern(form.getIpPattern());
            
            if(!isActiveUser) {
                SimpleDateFormat sdf = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.dateTime"));
                try
                {
                    if(form.getValidFrom() != null)
                        editUser.getProfile().setValidFrom(sdf.parse(form.getValidFrom()));
                    else
                        editUser.getProfile().setValidFrom(null);
                    if(form.getValidUntil() != null)
                        editUser.getProfile().setValidUntil(sdf.parse(form.getValidUntil()));
                    else
                        editUser.getProfile().setValidUntil(null);
                }
                catch (ParseException e)
                {
                    log.error("problems parsing event time validFrom/validUntil", e);
                }
            }
            if (form.getUserPassword() != null && !isActiveUser)
                editUser.setPassword(SignatureUtility.getInstance().createSignature(form.getUserPassword()));
    
            if (form.getMainUsergroupId() > 0)
            {
                Iterator<UsergroupVO> usergroups = editUser.getProfile().getUsergroups().iterator();
                while (usergroups.hasNext())
                {
                    UsergroupVO usergroup = usergroups.next();
                    if (usergroup.getId() == form.getMainUsergroupId())
                    {
                        editUser.getProfile().setDefaultUsergroup(usergroup);
                        break;
                    }
                }
            }
        }
        
        editUser.setLocale(LocaleUtility.parseLocale(form.getLocale()));
    }
    
    @Override
    public void writeForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        UserVO editUser = (UserVO) getPanel().getEntity();

        form.setUserName(editUser.getUserName());
        form.setLocked(editUser.getProfile().isLocked());
        form.setIpPattern(editUser.getProfile().getIpPattern());
        if (editUser.getProfile().getDefaultUsergroup() != null)
            form.setMainUsergroupId(editUser.getProfile().getDefaultUsergroup().getId());
        form.setTimeLock(editUser.getProfile().getTimeLock());
        form.setLocale(editUser.getLocale().toString());
        
        SimpleDateFormat sdf = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.dateTime"));
        if (editUser.getProfile().getValidFrom() != null)
            form.setValidFrom(sdf.format(editUser.getProfile().getValidFrom()));
        if (editUser.getProfile().getValidUntil() != null)
            form.setValidUntil(sdf.format(editUser.getProfile().getValidUntil()));
    }
    

    @Override
    public ActionForward handleCustomAction(ActionMapping mapping, PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        if (form.getDoAddPerson().isSelected())
            return mapping.findForward("addPerson");
        else if (form.getDoAddUsergroup().isSelected())
            return mapping.findForward("addUsergroup");
        else if (form.getDoAddRole().isSelected())
            return mapping.findForward("addRole");
        else if (form.getDoJumpRole().isSelected())
        {
            RoleVO role = ((UserVO)getPanel().getEntity()).getProfile().getRole();
            
            request.setAttribute("external_role_id", role.getId());
            
            return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
        }
        else if (form.getDoJumpUsergroup() > 0)
        {
            request.setAttribute("external_usergroup_id", form.getDoJumpUsergroup());            
            
            return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
        }
        else if (form.getDoRemoveUsergroup() > 0)
        {
            if (log.isDebugEnabled())
                log.debug("remove usergroup with id: "+form.getDoRemoveUsergroup());          
            
            UserVO user = (UserVO) getPanel().getEntity();

            Iterator<UsergroupVO> it = user.getProfile().getUsergroups().iterator();
            while(it.hasNext()) {
                if(it.next().getId().intValue() == form.getDoRemoveUsergroup()) {
                    it.remove();
                    break;
                }
            }
            
            return Panel.getForward(getPanel().getActivePage().getAction(), request);
        }
        else if (form.getDoRemovePerson().isSelected())
        {
            if (log.isDebugEnabled())
                log.debug("remove person");

            UserVO user = (UserVO)getPanel().getEntity();
            user.setPerson(null);
            
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
