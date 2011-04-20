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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;

public final class TagUtility
{
    private final static Logger log = Logger.getLogger(TagUtility.class);

    public final static String lookupString(PageContext page, String name, String property, String scope)
    {
        return lookupString(page, name, property, scope, false);
    }

    public final static String lookupString(PageContext page, String name, String property, String scope, boolean nullValue)
    {
        String text = null;
        if (!nullValue)
            text = "";

        try
        {
            Object obj = TagUtils.getInstance().lookup(page, name, property, scope);
            if (obj != null)
                text = TagUtils.getInstance().filter(obj.toString());
        }
        catch (JspException e)
        {
            log.warn("could not perform lookup", e);
        }
        return text;
    }

    public final static Object lookup(PageContext page, String name, String property, String scope)
    {
        Object obj = null;

        try
        {
            obj = TagUtils.getInstance().lookup(page, name, property, scope);
        }
        catch (JspException e)
        {
            log.warn("could not perform lookup", e);
        }
        return obj;
    }
}