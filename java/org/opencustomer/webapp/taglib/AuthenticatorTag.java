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

package org.opencustomer.webapp.taglib;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.opencustomer.webapp.auth.AuthenticatorUtility;
import org.opencustomer.webapp.auth.Right;

public class AuthenticatorTag extends TagSupport
{
    private static final long serialVersionUID = 3257567287094620467L;

    private final static Logger log = Logger.getLogger(AuthenticatorTag.class);

    private static final String DELIMITER = ",";

    private String right;

    @Override
    public int doStartTag() throws JspException
    {
        if (condition())
            return EVAL_BODY_INCLUDE;
        else
            return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException
    {
        return EVAL_PAGE;
    }

    @Override
    public void release()
    {
        super.release();
        right = null;
    }

    private boolean condition()
    {
        boolean access = false;

        if (log.isDebugEnabled())
            log.debug("authenticate against authenticator (right: " + right + ")");

        if (right == null)
            access = true;
        else
        {
            StringTokenizer st = new StringTokenizer(right, DELIMITER, false);

            while (!access && st.hasMoreTokens())
            {
                String token = st.nextToken().trim();

                try
                {
                    if (AuthenticatorUtility.getInstance().authenticate((HttpServletRequest) pageContext.getRequest(), Right.parseRight(token.trim())))
                    {
                        access = true;

                        if (log.isDebugEnabled())
                            log.debug("right " + token + " valid: " + access);

                        break;
                    }
                }
                catch (IllegalArgumentException e)
                {
                    log.warn("found invalid right: " + token);
                }
            }

        }

        return access;
    }

    public String getRight()
    {
        return right;
    }

    public void setRight(String right)
    {
        this.right = right;
    }
}
