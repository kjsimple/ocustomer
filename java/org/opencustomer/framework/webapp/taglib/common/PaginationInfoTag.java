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

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;
import org.opencustomer.framework.util.PropertyUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.framework.webapp.util.html.Pagination;

public class PaginationInfoTag extends TagSupport {
    private static final long serialVersionUID = 3256442512469342256L;

    private static Properties properties = null;
    
    private String name;

    private String property;

    private String scope;

    private String textKey;

    public PaginationInfoTag() {
        if(properties == null)
            properties = PropertyUtility.getProperties(PaginationInfoTag.class);
    }
    
    public int doEndTag() throws JspException {
        StringBuffer results = new StringBuffer();

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        Pagination pagination = (Pagination) TagUtils.getInstance().lookup(pageContext, name, property, scope);

        long to = 0;
        long from = 0;
        long off = 0;

        if (pagination != null) {
            to = pagination.getPage().getLastEntry();
            from = pagination.getPage().getFirstEntry();
            off = pagination.getCount();
        }

        if (to > off)
            to = off;
        if (from > off)
            from = off;

        String paginationText = properties.getProperty("text.pagination");
        if (paginationText != null && textKey != null) {
            results.append(MessageUtil.message(request, paginationText, new Object[] {MessageUtil.message(request, textKey), new Long(from), new Long(to), new Long(off) }));
        } else {
            results.append(from);
            results.append("\u00A0-\u00A0");
            results.append(to);
            results.append("\u00A0/\u00A0");
            results.append(off);
        }

        TagUtils.getInstance().write(pageContext, results.toString());

        release();

        return EVAL_PAGE;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getScope() {
        return scope;
    }

    public final void setScope(String scope) {
        this.scope = scope;
    }

    public final String getTextKey() {
        return textKey;
    }

    public final void setTextKey(String textKey) {
        this.textKey = textKey;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
