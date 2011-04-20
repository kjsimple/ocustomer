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
 * Copyright (C) 2006 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.framework.webapp.taglib.panel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

public class EditableTag extends TagSupport
{
    private static final long serialVersionUID = 3257567287094620467L;

    private final static Logger log = Logger.getLogger(EditableTag.class);

    private String condition;

    private String scope = "page";
    
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
        condition = null;
    }

    private boolean condition()
    {
        boolean test = Boolean.parseBoolean(condition);
        
        if("page".equals(scope)) {
            return test == (Boolean)pageContext.getAttribute("i_page_editable");
        } else if("panel".equals(scope)) {
            return test == (Boolean)pageContext.getAttribute("i_panel_editable");
        }
        
        return false;
    }

    public String getCondition()
    {
        return condition;
    }

    public void setCondition(String condition)
    {
        if(condition != null)
            this.condition = condition.toLowerCase();
        else
            this.condition = null;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
