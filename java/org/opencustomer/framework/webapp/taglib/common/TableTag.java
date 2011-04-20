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

import java.util.Locale;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;
import org.opencustomer.framework.webapp.util.html.Table;
import org.opencustomer.framework.webapp.util.html.renderer.RendererContext;
import org.opencustomer.framework.webapp.util.html.renderer.TableRenderer;

public final class TableTag extends TagSupport
{
    private static final long serialVersionUID = 3544387011285628472L;

    private TableRenderer renderer = new TableRenderer();
    
    private String name;

    private String property;

    private String scope;

    private String style;
    
    private String onclick;
    
    public TableTag() {
    }
    
    public int doEndTag() throws JspException
    {
        StringBuilder results = new StringBuilder();

        Table table          = (Table)TagUtils.getInstance().lookup(pageContext, name, property, scope);

        Properties settings  = (Properties) pageContext.getSession().getServletContext().getAttribute(org.opencustomer.webapp.Globals.TAG_SETTINGS_KEY);
        MessageResources res = (MessageResources) pageContext.getSession().getServletContext().getAttribute(Globals.MESSAGES_KEY);
        Locale locale        = (Locale) pageContext.getSession().getAttribute(Globals.LOCALE_KEY);

        TagUtils.getInstance().write(pageContext, renderer.createHtml(new RendererContext(res, settings, locale), style, table, onclick));
        
        release();

        return EVAL_PAGE;
    }

    @Override
    public void release() {
        super.release();
        
        this.name     = null;
        this.property = null;
        this.scope    = null;
        this.style    = null;
        this.onclick  = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }
}
