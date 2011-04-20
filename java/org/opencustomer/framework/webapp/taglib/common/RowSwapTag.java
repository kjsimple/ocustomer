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

package org.opencustomer.framework.webapp.taglib.common;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;

public class RowSwapTag extends TagSupport
{
    private static final long serialVersionUID = 3978702887499412017L;

    private static final Logger log = Logger.getLogger(RowSwapTag.class);

    private String name;

    private String number;

    private String odd;

    private String even;

    public int doEndTag() throws JspException
    {
        int position = ((Integer) TagUtils.getInstance().lookup(pageContext, name, "page")).intValue();
        int change = 1;
        if (number != null)
        {
            try
            {
                change = Integer.parseInt(number);
            }
            catch (NumberFormatException e)
            {
                log.error("invalid number given: " + number);
            }
        }

        if ((position / change) % 2 == 0)
            TagUtils.getInstance().write(pageContext, odd);
        else
            TagUtils.getInstance().write(pageContext, even);

        release();

        return EVAL_PAGE;
    }

    public final String getEven()
    {
        return even;
    }

    public final void setEven(String even)
    {
        this.even = even;
    }

    public final String getName()
    {
        return name;
    }

    public final void setName(String name)
    {
        this.name = name;
    }

    public final String getNumber()
    {
        return number;
    }

    public final void setNumber(String number)
    {
        this.number = number;
    }

    public final String getOdd()
    {
        return odd;
    }

    public final void setOdd(String odd)
    {
        this.odd = odd;
    }

}
