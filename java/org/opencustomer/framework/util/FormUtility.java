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

package org.opencustomer.framework.util;

import org.apache.log4j.Logger;

/**
 * Utility for reading and interpreting formular inputs.
 * 
 * @author Thomas Bader
 * @version 1.0
 */
public final class FormUtility
{
    private static Logger log = Logger.getLogger(FormUtility.class);

    /**
     * Empty Constructor - instances are not necessary for this class.
     */
    private FormUtility()
    {
    }

    /**
     * Adjusts the parameter value. The value will be trimmed and cut
     * automatically if too long.
     * 
     * @param value the parameter value.
     * @param length the maximum length of this parameter.
     * @return the adjusted paramer or null if the parameter has no length after
     *         trimming.
     */
    public static String adjustParameter(String value, int length)
    {
        // if(log.isDebugEnabled())
        // log.debug("adjust parameter '"+value+"' to length "+length);

        String back = null;

        if (value != null)
        {
            back = value.trim();

            int size = back.length();
            if (size == 0)
                back = null;
            else if (size > length)
                back = back.substring(0, length);

            // if(back != null)
            // back = StringUtility.unicode(back);
        }

        return back;
    }

    /**
     * Adjusts the parameter value. The value will be trimmed automatically.
     * 
     * @param value the parameter value.
     * @return the adjusted parameter value or null if the parameter has no
     *         length after trimming.
     */
    public static String adjustParameter(String value)
    {
        // if(log.isDebugEnabled())
        // log.debug("adjust parameter '"+value+"'");

        String back = null;

        if (value != null)
        {
            back = value.trim();
            int size = back.length();

            if (size == 0)
                back = null;
        }
        
        return back;
    }
}