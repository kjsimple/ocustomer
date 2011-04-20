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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

/**
 * 
 * @author thbader
 */
public abstract class ActionForm extends org.apache.struts.action.ActionForm
{
    private final static Logger log = Logger.getLogger(ActionForm.class);

    public ActionForm()
    {
    }

    /**
     * Return <code>true</code> if there is a transaction token stored in the
     * user's current session, and the value submitted as a request parameter
     * with this action matches it. Returns <code>false</code>
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match the transaction
     * token in the user's session</li>
     * </ul>
     * 
     * @param request The servlet request we are processing
     */
    protected boolean isTokenValid(HttpServletRequest request)
    {

        // Retrieve the current session for this request
        HttpSession session = request.getSession(false);
        if (session == null)
            return (false);

        synchronized (session)
        {

            // Retrieve the transaction token from this session
            String saved = (String) session.getAttribute(org.apache.struts.Globals.TRANSACTION_TOKEN_KEY);
            if (saved == null)
                return (false);

            // Retrieve the transaction token included in this request
            String token = request.getParameter(org.apache.struts.taglib.html.Constants.TOKEN_KEY);
            if (token == null)
                return (false);

            // Do the values match?
            return (saved.equals(token));

        }
    }
    
    public final ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();

        boolean doValidate = process(mapping, errors, request);
        analyzeRequest(mapping, errors, request);
        if(doValidate)
            validate(mapping, errors, request);
        
        if (log.isDebugEnabled())
            log.debug("found " + errors.size() + " errors during validation");

        return errors;
    }

    public boolean process(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        return true;
    }
    
    public abstract void validate(ActionMapping mapping, ActionMessages error, HttpServletRequest request);

    protected void analyzeRequest(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {

    }
}
