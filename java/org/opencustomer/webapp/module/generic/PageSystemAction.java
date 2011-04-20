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

package org.opencustomer.webapp.module.generic;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.dao.system.UsergroupDAO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.UsergroupVO;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.db.vo.StampedVO;
import org.opencustomer.framework.db.vo.VersionedVO;
import org.opencustomer.framework.util.EnumUtility;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Page;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditPageAction;

public class PageSystemAction<E extends PageSystemForm> extends EditPageAction<E>
{
    private static Logger log = Logger.getLogger(PageSystemAction.class);

    @Override
    protected void preOperations(E form, ActionMessages errors, HttpServletRequest request)
    {
        UserVO activeUser = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        
        EditPanel panel = getPanel();
        
        EntityAccess ea = getEntityAccess(request);
        
        if(panel.getAttribute("system_entityAccess") == null) {
            panel.setAttribute("system_entityAccess", ea);

            List<UsergroupVO> usergroups = new UsergroupDAO().getList(null, UsergroupDAO.AdminSelect.ALL, new Sort(UsergroupDAO.SORT_NAME, true), null, activeUser);
            
            // load a set usergroup where you do not have rights
            boolean usergroupAccess = false;
            for(UsergroupVO vo : usergroups) {
                if(ea.getOwnerGroup().equals(vo.getId())) {
                    usergroupAccess = true;
                    break;
                }
            }
            if(!usergroupAccess) {
                UsergroupVO vo = new UsergroupDAO().getById(ea.getOwnerGroup());
                usergroups.add(0, vo);
            }
            Collections.sort(usergroups, new Comparator<UsergroupVO>() {
                public int compare(UsergroupVO usergroup1, UsergroupVO usergroup2)
                {
                    return usergroup1.getName().compareTo(usergroup2.getName());
                }
            });            
            panel.setAttribute("system_usergroups", usergroups);
            
            if(ea != null) {
                UserVO user = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
                
                Page activePage = panel.getActivePage();
                
                activePage.setEditable(isEditable(panel, user, ea, request));
            }
            
            UserVO ownerUser = null;
    
            if(panel.getAttribute("system_ownerUser") == null && ea.getOwnerUser() > 0) {
                if(log.isDebugEnabled())
                    log.debug("load user for owner of entity access");
                
                ownerUser = new UserDAO().getById(ea.getOwnerUser());
                panel.setAttribute("system_ownerUser", ownerUser);
            } 
            
            if(ea instanceof StampedVO) {
                StampedVO vo = (StampedVO)ea;
                panel.setAttribute("system_isStamped", Boolean.TRUE);
                
                if(panel.getAttribute("system_createUser") == null && vo.getCreateUser() != null && vo.getCreateUser() > 0) {
                    UserVO user = null;
                    if(vo.getCreateUser().equals(ea.getOwnerUser())) {
                        user = ownerUser; 
                    } else {
                        user = new UserDAO().getById(vo.getCreateUser()); 
                    }
                    panel.setAttribute("system_createUser", user);
                }
    
                if(panel.getAttribute("system_modifyUser") == null && vo.getModifyUser() != null && vo.getModifyUser() > 0) {
                    UserVO user = null;
                    if(vo.getModifyUser().equals(ea.getOwnerUser())) {
                        user = ownerUser; 
                    } else if(vo.getModifyUser().equals(vo.getCreateUser())) {
                        user = ownerUser; 
                    } else {
                        user = new UserDAO().getById(vo.getModifyUser()); 
                    }
                    panel.setAttribute("system_modifyUser", user);
                }
            }
            
            if(ea instanceof VersionedVO) {
                panel.setAttribute("system_isVersioned", Boolean.TRUE);
            }
        }
    }
    
    @Override
    public final void writeForm(E form, ActionMessages errors, HttpServletRequest request)
    {
        EntityAccess entity = getEntityAccess(request);

        form.setGlobalAccess(entity.getAccessGlobal().toString());
        form.setGroup(entity.getOwnerGroup());
        form.setGroupAccess(entity.getAccessGroup().toString());
    }
    
    @Override
    public final void readForm(E form, ActionMessages errors, HttpServletRequest request)
    {
        EntityAccess entity = getEntityAccess(request);

        entity.setAccessGlobal(EnumUtility.valueOf(EntityAccess.Access.class, form.getGlobalAccess()));
        entity.setOwnerGroup(form.getGroup());
        entity.setAccessGroup(EnumUtility.valueOf(EntityAccess.Access.class, form.getGroupAccess()));
    }

    protected EntityAccess getEntityAccess(HttpServletRequest request) {
        return (EntityAccess)getPanel().getEntity();
    }
    
    protected boolean isEditable(EditPanel panel, UserVO user, EntityAccess entity, HttpServletRequest request) {
        return EntityAccessUtility.isAccessGranted(user, entity, EntityAccess.Access.WRITE_SYSTEM);
    }
}
