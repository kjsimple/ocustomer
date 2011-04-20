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
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.taglib.TagUtils;

public class LabelTag extends BodyTagSupport
{
    private static final long serialVersionUID = 3545800987517203766L;

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LabelTag.class);

    private String text;

    private String property;

    private String style;

    private String styleClass;

    private String styleId;

    private String errorStyle;

    private String errorStyleClass;

    private String errorStyleId;

    public int doStartTag() throws JspTagException
    {
        this.text = null;
        return EVAL_BODY_BUFFERED;
    }

    public int doAfterBody() throws JspException
    {
        if (bodyContent != null)
        {
            String value = bodyContent.getString().trim();
            if (value.length() > 0)
                text = value;
        }
        return SKIP_BODY;

    }

    public int doEndTag() throws JspException
    {
        ActionMessages errors = null;
        try
        {
            errors = TagUtils.getInstance().getActionMessages(pageContext, Globals.ERROR_KEY);
        }
        catch (JspException e)
        {
            log.error(e);
        }
        StringBuffer results = new StringBuffer();

        results.append("<label for=\"").append(property).append("\"");
        if (errors != null && !errors.isEmpty() && errors.get(property).hasNext())
        {
            if (errorStyle != null && errorStyle.length() > 0)
                style = errorStyle;
            if (errorStyleClass != null && errorStyleClass.length() > 0)
                styleClass = errorStyleClass;
            if (errorStyleId != null && errorStyleId.length() > 0)
                styleId = errorStyleId;
        }
        if (styleId != null && styleId.length() > 0)
            results.append(" id=\"").append(styleId).append("\"");
        if (styleClass != null && styleClass.length() > 0)
            results.append(" class=\"").append(styleClass).append("\"");
        if (style != null && style.length() > 0)
            results.append(" style=\"").append(style).append("\"");

        if (text != null)
        {
            results.append(">");
            results.append(text);
            results.append("</label>");
        }
        else
            results.append("/>");

        TagUtils.getInstance().write(pageContext, results.toString());

        release();

        return EVAL_PAGE;
    }

    public void release()
    {
        super.release();
        text = null;
        property = null;
        style = null;
        styleClass = null;
        styleId = null;
        errorStyle = null;
        errorStyleClass = null;
        errorStyleId = null;
    }

    /**
     * @return Returns the errorStyle.
     */
    public String getErrorStyle()
    {
        return errorStyle;
    }

    /**
     * @param errorStyle The errorStyle to set.
     */
    public void setErrorStyle(String errorStyle)
    {
        this.errorStyle = errorStyle;
    }

    /**
     * @return Returns the errorStyleClass.
     */
    public String getErrorStyleClass()
    {
        return errorStyleClass;
    }

    /**
     * @param errorStyleClass The errorStyleClass to set.
     */
    public void setErrorStyleClass(String errorStyleClass)
    {
        this.errorStyleClass = errorStyleClass;
    }

    /**
     * @return Returns the errorStyleId.
     */
    public String getErrorStyleId()
    {
        return errorStyleId;
    }

    /**
     * @param errorStyleId The errorStyleId to set.
     */
    public void setErrorStyleId(String errorStyleId)
    {
        this.errorStyleId = errorStyleId;
    }

    /**
     * @return Returns the property.
     */
    public String getProperty()
    {
        return property;
    }

    /**
     * @param property The property to set.
     */
    public void setProperty(String property)
    {
        this.property = property;
    }

    /**
     * @return Returns the style.
     */
    public String getStyle()
    {
        return style;
    }

    /**
     * @param style The style to set.
     */
    public void setStyle(String style)
    {
        this.style = style;
    }

    /**
     * @return Returns the styleClass.
     */
    public String getStyleClass()
    {
        return styleClass;
    }

    /**
     * @param styleClass The styleClass to set.
     */
    public void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }

    /**
     * @return Returns the styleId.
     */
    public String getStyleId()
    {
        return styleId;
    }

    /**
     * @param styleId The styleId to set.
     */
    public void setStyleId(String styleId)
    {
        this.styleId = styleId;
    }
}
