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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.opencustomer.framework.util.PropertyUtility;
import org.opencustomer.framework.webapp.util.html.Pagination;

public final class PaginationMenuTag extends TagSupport {
    private static final long serialVersionUID = 3544387011285628472L;

    private static Properties properties = null;
   
    private String paramId = "page";

    private String name;

    private String property;

    private String scope;

    private String styleClass;
    public PaginationMenuTag() {
        if(properties == null)
            properties = PropertyUtility.getProperties(PaginationInfoTag.class);
    }
    
    public int doEndTag() throws JspException {
        StringBuilder results = new StringBuilder();

        // ist dieses Tag in einem Formular?
        if (pageContext.getAttribute(Constants.FORM_KEY, PageContext.REQUEST_SCOPE) == null)
            throw new JspException("element only allowed in forms");

        Pagination pagination = (Pagination) TagUtils.getInstance().lookup(pageContext, name, property, scope);

        long pages = 0;
        long pageNr = 0;

        if (pagination != null) {
            pages = pagination.getCount() / pagination.getPage().getStep();
            if ((pagination.getCount() % pagination.getPage().getStep()) > 0)
                pages++;
            pageNr = pagination.getPage().getPage();
        }

        if (pages > 1) {
            if("true".equalsIgnoreCase(properties.getProperty("simpleType"))) {
                for (int i = 1; i <= pages; i++) {
                    results.append("<input type=\"submit\"");
                    results.append(" name=\"");
                    results.append(paramId);
                    results.append("\"");

                    if (i == pageNr)
                        results.append(" disabled=\"disabled\"");

                    if (styleClass != null)
                        results.append(" class=\"").append(styleClass).append("\"");

                    results.append(" value=\"");
                    results.append(i);
                    results.append("\"/>");                    
                } 
            } else {
                results.append("<script type=\"text/javascript\">");
                results.append("function changePage(i) {");
                results.append("button = document.getElementById(\""+paramId+"\");");
                results.append("button.value=i;");
                results.append("button.form.submit();");
                results.append("}");
                results.append("</script>");
                
                long firstPage = 1;
                long finalPage = pages;
                long lastPage = pageNr-1;
                if(lastPage < firstPage)
                    lastPage = firstPage;
                long nextPage = pageNr+1;
                if(nextPage > finalPage)
                    nextPage = finalPage;
                
                createInput(results, paramId, "&lt;&lt;", firstPage, firstPage == pageNr);
                createInput(results, paramId, "&lt;", lastPage, firstPage == pageNr);
                
                results.append("<select ");
                results.append(" name=\"").append(paramId).append("\"");
                results.append(" id=\"").append(paramId).append("\"");
                results.append(" onchange=\"submit()\"");
                results.append(">");
                for (int i = 1; i <= pages; i++) {
                    results.append("<option ");
                    results.append("value=\"").append(i).append("\""); 
                    if (i == pageNr)
                        results.append(" selected=\"selected\"");
                    results.append(">");
                    results.append(i);
                    results.append("</option>");
                } 
                results.append("</select>");
                
                createInput(results, paramId, "&gt;", nextPage, finalPage == pageNr);
                createInput(results, paramId, "&gt;&gt;", finalPage, finalPage == pageNr);
            }
        } else
            results.append("<!-- only one page / do not show -->");

        TagUtils.getInstance().write(pageContext, results.toString());

        release();

        return EVAL_PAGE;
    }

    private void createInput(StringBuilder results, String paramName, String value, long page, boolean disabled) {
        results.append("<input type=\"button\"");
        if (disabled)
            results.append(" disabled=\"disabled\"");
        results.append(" value=\"").append(value).append("\"");
        results.append(" onclick=\"changePage(").append(page).append(")\"");
        results.append("/>");
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

    public final String getParamId() {
        return paramId;
    }

    public final void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public final String getStyleClass() {
        return styleClass;
    }

    public final void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

}
