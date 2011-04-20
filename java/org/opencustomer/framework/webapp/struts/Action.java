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

package org.opencustomer.framework.webapp.struts;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Thomas Bader
 * @version 1.0
 */
public abstract class Action<E extends org.opencustomer.framework.webapp.struts.ActionForm> extends org.apache.struts.action.Action {
    private final static Logger log = Logger.getLogger(Action.class);

    /**
     * Overwrites the standard perform method to verify the user and his rights.
     * 
     * @param servlet The ActionServlet instance owning this Action
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The servlet request we are processing
     * @param response The servlet response we are processing
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    @SuppressWarnings("unchecked")
    public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ActionForward forwardAction = null;

        try {
            forwardAction = execute(mapping, (E) form, request, response);
        } catch (Throwable e) {
            log.error("unexpected error", e);
            forwardAction = mapping.findForward("error");
        }

        if(log.isDebugEnabled())
            log.debug("save new token");
        this.saveToken(request);

        return forwardAction;
    }

    /**
     * Method which have to be overwritten to handle the request.
     * 
     * @param servlet The ActionServlet instance owning this Action
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The servlet request we are processing
     * @param response The servlet response we are processing
     * @param log the log to save messages
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public abstract ActionForward execute(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

}