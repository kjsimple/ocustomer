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
package org.opencustomer.framework.webapp.taglib.panel;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.html.FormTag;
import org.opencustomer.framework.util.PropertyUtility;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.util.MessageUtil;

public abstract class FormPanelTag extends FormTag
{
    private EditPanel panel;

    private static Properties properties = null;
    
    public FormPanelTag()
    {
        if(properties == null)
            properties = PropertyUtility.getProperties(FormPanelTag.class);
    }
    
    @Override
    public int doStartTag() throws JspException
    {
        return super.doStartTag();
    }
    
    public int doEndTag() throws JspException
    {
        return super.doEndTag();
    }

    protected final String renderButton(StringBuilder html, String property, String key, String titleKey, String styleClass, Character accessKey, boolean disabled)  throws JspException 
    {
        html.append("<input type=\"submit\"");
        html.append(" name=\""+property+"\"");
        html.append(" value=\""+MessageUtil.message(getRequest(), key)+"\"");
        if(titleKey != null)
            html.append(" title=\""+MessageUtil.message(getRequest(), titleKey)+"\"");
        if(accessKey != null)
            html.append(" accesskey=\"").append(accessKey).append("\"");
        if(styleClass != null)
            html.append(" class=\""+styleClass+"\"");
        if(disabled)
            html.append(" disabled=\"disabled\"");
        html.append("/>");
        
        return html.toString();
    }     
    
    protected HttpServletRequest getRequest() 
    {
        return (HttpServletRequest)pageContext.getRequest();
    }
    
    public void release()
    {
        super.release();
        
        this.panel = null;
    }
    
    protected String getProperty(String key)
    {
        return properties.getProperty(key);
    }
}
