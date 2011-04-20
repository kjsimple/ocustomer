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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.framework.webapp.util.RequestUtility;

public final class SortTag extends TagSupport
{
    private static final long serialVersionUID = 3257571702304223793L;

    public static final Logger log = Logger.getLogger(SortTag.class);

    private static PropertyBean propertyBean;

    private String messageKey;

    private String property;

    private String propertyPrefix;

    private String propertyPostfixAsc;

    private String propertyPostfixDesc;

    private String descIconURL;

    private String descIconTitle;

    private String ascIconURL;

    private String ascIconTitle;

    private String styleClass;

    public int doStartTag() throws JspException
    {
        if (pageContext.getAttribute(Constants.FORM_KEY, PageContext.REQUEST_SCOPE) == null)
            throw new JspException("element only allowed in forms");

        merge();
        validate();

        return super.doStartTag();
    }

    public int doEndTag() throws JspException
    {
        StringBuffer results = new StringBuffer();

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        results.append("<table");
        results.append(" class=\"").append(styleClass).append("\"");
        results.append("><tr><td>");
        results.append(MessageUtil.message(request, messageKey));
        results.append("</td><td>");

        results.append("<input type=\"image\"");
        results.append(" name=\"").append(propertyPrefix + RequestUtility.DIVIDER + property + RequestUtility.DIVIDER + propertyPostfixDesc).append("\"");
        results.append(" src=\"").append(response.encodeURL(request.getContextPath() + MessageUtil.message(request, this.descIconURL))).append("\"");
        if (descIconTitle != null)
        {
            results.append(" title=\"").append(MessageUtil.message(request, this.descIconTitle)).append("\"");
            results.append(" alt=\"").append(MessageUtil.message(request, this.descIconTitle)).append("\"");
        }
        results.append("/>");

        results.append("<input type=\"image\"");
        results.append(" name=\"").append(propertyPrefix + RequestUtility.DIVIDER + property + RequestUtility.DIVIDER + propertyPostfixAsc).append("\"");
        results.append(" src=\"").append(response.encodeURL(request.getContextPath() + MessageUtil.message(request, this.ascIconURL))).append("\"");
        if (ascIconTitle != null)
        {
            results.append(" title=\"").append(MessageUtil.message(request, this.ascIconTitle)).append("\"");
            results.append(" alt=\"").append(MessageUtil.message(request, this.ascIconTitle)).append("\"");
        }
        results.append("/>");

        results.append("</td></tr></table>");

        TagUtils.getInstance().write(pageContext, results.toString());

        release();

        return EVAL_PAGE;
    }

    private void merge()
    {
        if (propertyPrefix == null)
            propertyPrefix = propertyBean.propertyPrefix;
        if (propertyPostfixAsc == null)
            propertyPostfixAsc = propertyBean.propertyPostfixAsc;
        if (propertyPostfixDesc == null)
            propertyPostfixDesc = propertyBean.propertyPostfixDesc;
        if (descIconURL == null)
            descIconURL = propertyBean.descIconURL;
        if (descIconTitle == null)
            descIconTitle = propertyBean.descIconTitle;
        if (ascIconURL == null)
            ascIconURL = propertyBean.ascIconURL;
        if (ascIconTitle == null)
            ascIconTitle = propertyBean.ascIconTitle;
        if (styleClass == null)
            styleClass = propertyBean.styleClass;
    }

    private void validate() throws JspException
    {
        if (propertyPrefix == null)
            throw new JspException("missing field: propertyPrefix");
        if (propertyPostfixAsc == null)
            throw new JspException("missing field: propertyPostfixAsc");
        if (propertyPostfixDesc == null)
            throw new JspException("missing field: propertyPostfixDesc");
        if (descIconURL == null)
            throw new JspException("missing field: descIconURL");
        if (ascIconURL == null)
            throw new JspException("missing field: ascIconURL");
    }

    public static void load(Properties properties)
    {
        if (log.isInfoEnabled())
            log.info("load properties");

        String identifier = SortTag.class.getName() + ".";

        propertyBean = new PropertyBean();
        propertyBean.propertyPrefix = properties.getProperty(identifier + "propertyPrefix");
        propertyBean.propertyPostfixAsc = properties.getProperty(identifier + "propertyPostfixAsc");
        propertyBean.propertyPostfixDesc = properties.getProperty(identifier + "propertyPostfixDesc");
        propertyBean.descIconURL = properties.getProperty(identifier + "descIconURL");
        propertyBean.descIconTitle = properties.getProperty(identifier + "descIconTitle");
        propertyBean.ascIconURL = properties.getProperty(identifier + "ascIconURL");
        propertyBean.ascIconTitle = properties.getProperty(identifier + "ascIconTitle");
        propertyBean.styleClass = properties.getProperty(identifier + "styleClass");
    }

    private static class PropertyBean
    {
        public String propertyPrefix;

        public String propertyPostfixAsc;

        public String propertyPostfixDesc;

        public String descIconURL;

        public String descIconTitle;

        public String ascIconURL;

        public String ascIconTitle;

        public String styleClass;
    }

    /**
     * @return Returns the ascIconTitle.
     */
    public final String getAscIconTitle()
    {
        return ascIconTitle;
    }

    /**
     * @param ascIconTitle The ascIconTitle to set.
     */
    public final void setAscIconTitle(String ascIconTitle)
    {
        this.ascIconTitle = ascIconTitle;
    }

    /**
     * @return Returns the ascIconURL.
     */
    public final String getAscIconURL()
    {
        return ascIconURL;
    }

    /**
     * @param ascIconURL The ascIconURL to set.
     */
    public final void setAscIconURL(String ascIconURL)
    {
        this.ascIconURL = ascIconURL;
    }

    /**
     * @return Returns the descIconTitle.
     */
    public final String getDescIconTitle()
    {
        return descIconTitle;
    }

    /**
     * @param descIconTitle The descIconTitle to set.
     */
    public final void setDescIconTitle(String descIconTitle)
    {
        this.descIconTitle = descIconTitle;
    }

    /**
     * @return Returns the descIconURL.
     */
    public final String getDescIconURL()
    {
        return descIconURL;
    }

    /**
     * @param descIconURL The descIconURL to set.
     */
    public final void setDescIconURL(String descIconURL)
    {
        this.descIconURL = descIconURL;
    }

    /**
     * @return Returns the messageKey.
     */
    public final String getMessageKey()
    {
        return messageKey;
    }

    /**
     * @param messageKey The messageKey to set.
     */
    public final void setMessageKey(String messageKey)
    {
        this.messageKey = messageKey;
    }

    /**
     * @return Returns the property.
     */
    public final String getProperty()
    {
        return property;
    }

    /**
     * @param property The property to set.
     */
    public final void setProperty(String property)
    {
        this.property = property;
    }

    /**
     * @return Returns the propertyPostfixAsc.
     */
    public final String getPropertyPostfixAsc()
    {
        return propertyPostfixAsc;
    }

    /**
     * @param propertyPostfixAsc The propertyPostfixAsc to set.
     */
    public final void setPropertyPostfixAsc(String propertyPostfixAsc)
    {
        this.propertyPostfixAsc = propertyPostfixAsc;
    }

    /**
     * @return Returns the propertyPostfixDesc.
     */
    public final String getPropertyPostfixDesc()
    {
        return propertyPostfixDesc;
    }

    /**
     * @param propertyPostfixDesc The propertyPostfixDesc to set.
     */
    public final void setPropertyPostfixDesc(String propertyPostfixDesc)
    {
        this.propertyPostfixDesc = propertyPostfixDesc;
    }

    /**
     * @return Returns the propertyPrefix.
     */
    public final String getPropertyPrefix()
    {
        return propertyPrefix;
    }

    /**
     * @param propertyPrefix The propertyPrefix to set.
     */
    public final void setPropertyPrefix(String propertyPrefix)
    {
        this.propertyPrefix = propertyPrefix;
    }

    /**
     * @return Returns the styleClass.
     */
    public final String getStyleClass()
    {
        return styleClass;
    }

    /**
     * @param styleClass The styleClass to set.
     */
    public final void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }

}
