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

package org.opencustomer.framework.webapp.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

/**
 * @author thbader
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class MessageUtil
{

    public static String message(HttpServletRequest request, String name)
    {
        return message(request, name, (Object[])null);
    }

    public static String message(HttpServletRequest request, String name, Object... args)
    {
        MessageResources res = (MessageResources) request.getSession().getServletContext().getAttribute(Globals.MESSAGES_KEY);

        if (args == null)
            return res.getMessage((Locale) request.getSession().getAttribute(Globals.LOCALE_KEY), name);
        else
            return res.getMessage((Locale) request.getSession().getAttribute(Globals.LOCALE_KEY), name, args);
    }

}
