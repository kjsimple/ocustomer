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

package org.opencustomer.webapp.struts;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.opencustomer.webapp.auth.AuthenticatorUtility;
import org.opencustomer.webapp.auth.Right;

public class RequestProcessor extends TilesRequestProcessor
{
    private static final Logger log = Logger.getLogger(RequestProcessor.class);

    @Override
    protected boolean processRoles(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException
    {
        boolean access = false;
        boolean loggedIn = false;

        if (log.isDebugEnabled())
            log.debug("authenticate against authenticator");

        if (AuthenticatorUtility.isLoggedIn(request))
            loggedIn = true;

        Right[] rights = new Right[mapping.getRoleNames().length];

        for (int i = 0; i < mapping.getRoleNames().length; i++)
        {
            try
            {
                rights[i] = Right.parseRight(mapping.getRoleNames()[i].trim());
            }
            catch (IllegalArgumentException e)
            {
                log.warn("found invalid right for '" + mapping.getPath() + "' : " + mapping.getRoleNames()[i]);
            }
        }

        access = AuthenticatorUtility.getInstance().authenticate(request, rights);

        if (log.isDebugEnabled())
            log.debug("access granted for '" + mapping.getPath() + "' : " + access);

        if (!access)
        {
            String forwardURL = mapping.findForward("login").getPath();
            if (loggedIn)
                forwardURL = mapping.findForward("error").getPath();

            ActionMessages errors = new ActionMessages();
            if (loggedIn)
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.common.error.missingRight"));
            else
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.common.error.notLoggedIn"));
            request.setAttribute(org.apache.struts.Globals.ERROR_KEY, errors);

            RequestDispatcher dispatcher = request.getRequestDispatcher(forwardURL);
            try
            {
                dispatcher.forward(request, response);
            }
            catch (ServletException e)
            {
                log.error("Could not forward to '" + forwardURL + "'", e);
            }
            catch (IOException e)
            {
                log.error("Generating internal forward failed to '" + forwardURL + "'", e);
            }
        }
        else
        {
            // setzte Menu
            //Menu menu = (Menu) request.getSession().getAttribute(Globals.MENU_KEY);
            //if (menu != null)
            //    menu.activate(mapping.getPath());
        }

        return access;
    }
}
