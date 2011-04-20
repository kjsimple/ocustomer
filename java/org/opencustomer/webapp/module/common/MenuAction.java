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

package org.opencustomer.webapp.module.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.panel.PanelStack;
import org.opencustomer.framework.webapp.struts.Action;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.util.menu.Menu;
import org.opencustomer.webapp.util.menu.MenuItem;

public final class MenuAction extends Action<MenuForm>
{
    private final static Logger log = Logger.getLogger(MenuAction.class);
    
    @Override
    public ActionForward execute(ActionMapping mapping, MenuForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        Menu menu = (Menu)request.getSession().getAttribute(Globals.MENU_KEY);
        
        MenuItem menuItem = null;
        
        if(menu == null)
        {
            if(log.isDebugEnabled())
                log.debug("no menu found in session");
            
            // not protected by rights -> write error and go to login manually
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.common.error.notLoggedIn"));
            saveErrors(request, errors);
        }
        else
        {
            menuItem = menu.activate(form.getMenuId());

            if(menuItem == null)
            {
                menuItem = menu.getActiveItem();
                if(log.isDebugEnabled())
                    log.debug("could not activate menu item / use active item: "+menuItem);
            }
            else if(log.isDebugEnabled())
                log.debug("activate item: "+menuItem);
            
            // do always delete the stack when click the menu
            PanelStack panelStack = Panel.getPanelStack(request);
            panelStack.clear();
            
            return Panel.getForward(menuItem.getAction(), request);
        }
        
        return mapping.findForward("login");
    }
}