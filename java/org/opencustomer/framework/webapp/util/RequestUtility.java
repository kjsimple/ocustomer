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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class RequestUtility
{
    public static final char DIVIDER = '_';

    /**
     * Gets the parameter value of the sort parameter. The value is attached to
     * the param name.
     * 
     * @param request the request reading the parameters.
     * @param param the string with the existing parameter to set, if no
     *        parameter is in the request
     * @param paramName the name of the parameter searching for
     * 
     * @return the parameter value attached to the param name found in the
     *         request.
     */
    public final static String parseRequest(HttpServletRequest request, String param, String paramName)
    {
        String paramValue = null;

        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements())
        {
            String name = (String) paramNames.nextElement();
            if (name.startsWith(paramName + DIVIDER))
            {
                int end = name.lastIndexOf('.'); // abschneiden ab dem Punkt
                // bei ImageButtons
                if (end == -1)
                    end = name.length();

                paramValue = name.substring(paramName.length() + 1, end);

                break;
            }
        }

        if (paramValue == null)
            paramValue = param;

        return paramValue;
    }

    public static int parseParamId(HttpServletRequest request, String paramName)
    {
        int value = 0;

        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements())
        {
            String name = (String) params.nextElement();
            if (name.startsWith(paramName))
            {
                value = RequestUtility.parseParam(paramName, name);
                break;
            }
        }

        return value;
    }

    public static int parseParam(String prefix, String name)
    {
        int id = 0;

        int start = name.indexOf('_') + 1;
        int end = name.lastIndexOf('.');

        String foundId = name.substring(start, end);
        try
        {
            id = Integer.parseInt(foundId);
        }
        catch (NumberFormatException e)
        {
        }

        return id;
    }
}
