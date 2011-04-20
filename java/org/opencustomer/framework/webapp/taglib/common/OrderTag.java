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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.framework.webapp.util.RequestUtility;

public class OrderTag extends TagSupport
{
    private static final long serialVersionUID = 3906366013179311159L;

    private String messageKey;

    private String url;

    private String paramName;

    private String upParam;

    private String upTextKey;

    private String upPageKey;

    private String downParam;

    private String downTextKey;

    private String downPageKey;

    private String styleClass;

    private String form;

    public int doEndTag() throws JspException
    {
        boolean isForm = false;
        if ("true".equalsIgnoreCase(form))
            isForm = true;

        StringBuffer results = new StringBuffer();

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        results.append("<table");
        results.append(" class=\"").append(styleClass).append("\"");
        results.append("><tr><td>");
        results.append(MessageUtil.message(request, messageKey));
        results.append("</td><td>");
        // kann man das auch mit forms machen?
        // <input type="image" name="doCancel"
        // src="/phoenix/images/icon/16/garbage.png" title="Suche bereinigen"
        // alt="Suche bereinigen" />
        if (isForm)
        {
            results.append("<input type=\"image\"");
            results.append(" name=\"").append(paramName + RequestUtility.DIVIDER + downParam).append("\"");
            results.append(" src=\"").append(response.encodeURL(request.getContextPath() + MessageUtil.message(request, downPageKey))).append("\"");
            results.append(" title=\"").append(MessageUtil.message(request, downTextKey)).append("\"");
            results.append(" alt=\"").append(MessageUtil.message(request, downTextKey)).append("\"");
            results.append("/>");
            results.append("<input type=\"image\"");
            results.append(" name=\"").append(paramName + RequestUtility.DIVIDER + upParam).append("\"");
            results.append(" src=\"").append(response.encodeURL(request.getContextPath() + MessageUtil.message(request, upPageKey))).append("\"");
            results.append(" title=\"").append(MessageUtil.message(request, upTextKey)).append("\"");
            results.append(" alt=\"").append(MessageUtil.message(request, upTextKey)).append("\"");
            results.append("/>");
        }
        else
        {
            results.append("<a");
            results.append(" href=\"").append(response.encodeURL(request.getContextPath() + url + "?" + paramName + "=" + downParam)).append("\"");
            results.append(" title=\"").append(MessageUtil.message(request, downTextKey)).append("\"");
            results.append(">");
            results.append("<img");
            results.append(" src=\"").append(response.encodeURL(request.getContextPath() + MessageUtil.message(request, downPageKey))).append("\"");
            results.append(" title=\"").append(MessageUtil.message(request, downTextKey)).append("\"");
            results.append(" alt=\"").append(MessageUtil.message(request, downTextKey)).append("\"");
            results.append("/>");
            results.append("</a>");
            results.append("<a");
            results.append(" href=\"").append(response.encodeURL(request.getContextPath() + url + "?" + paramName + "=" + upParam)).append("\"");
            results.append(" title=\"").append(MessageUtil.message(request, upTextKey)).append("\"");
            results.append(">");
            results.append("<img");
            results.append(" src=\"").append(response.encodeURL(request.getContextPath() + MessageUtil.message(request, upPageKey))).append("\"");
            results.append(" title=\"").append(MessageUtil.message(request, upTextKey)).append("\"");
            results.append(" alt=\"").append(MessageUtil.message(request, upTextKey)).append("\"");
            results.append("/>");
            results.append("</a>");
        }
        results.append("</td><tr></table>");

        TagUtils.getInstance().write(pageContext, results.toString());

        release();

        return EVAL_PAGE;
    }

    public final String getDownPageKey()
    {
        return downPageKey;
    }

    public final void setDownPageKey(String downPageKey)
    {
        this.downPageKey = downPageKey;
    }

    public final String getDownParam()
    {
        return downParam;
    }

    public final void setDownParam(String downParam)
    {
        this.downParam = downParam;
    }

    public final String getDownTextKey()
    {
        return downTextKey;
    }

    public final void setDownTextKey(String downTextKey)
    {
        this.downTextKey = downTextKey;
    }

    public final String getParamName()
    {
        return paramName;
    }

    public final void setParamName(String paramName)
    {
        this.paramName = paramName;
    }

    public final String getUpPageKey()
    {
        return upPageKey;
    }

    public final void setUpPageKey(String upPageKey)
    {
        this.upPageKey = upPageKey;
    }

    public final String getUpParam()
    {
        return upParam;
    }

    public final void setUpParam(String upParam)
    {
        this.upParam = upParam;
    }

    public final String getUpTextKey()
    {
        return upTextKey;
    }

    public final void setUpTextKey(String upTextKey)
    {
        this.upTextKey = upTextKey;
    }

    public final String getUrl()
    {
        return url;
    }

    public final void setUrl(String url)
    {
        this.url = url;
    }

    public final String getMessageKey()
    {
        return messageKey;
    }

    public final void setMessageKey(String messageKey)
    {
        this.messageKey = messageKey;
    }

    public final String getStyleClass()
    {
        return styleClass;
    }

    public final void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }

    /**
     * @return Returns the form.
     */
    public final String getForm()
    {
        return form;
    }

    /**
     * @param form The form to set.
     */
    public final void setForm(String form)
    {
        this.form = form;
    }
}
