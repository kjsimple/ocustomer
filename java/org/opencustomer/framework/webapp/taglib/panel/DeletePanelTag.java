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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;
import org.opencustomer.framework.webapp.panel.DeletePanel;
import org.opencustomer.framework.webapp.panel.Panel;

public class DeletePanelTag extends FormPanelTag
{
    private static final Logger log = Logger.getLogger(EditPanelTag.class);
    
    private static final long serialVersionUID = -2488736893082549514L;

    @Override
    public int doStartTag() throws JspException
    {
        DeletePanel panel = (DeletePanel)Panel.getPanelStack((HttpServletRequest)pageContext.getRequest()).peek();
        
        action = panel.getPath();
        styleClass = getProperty("form");

        int process = super.doStartTag();
        
        StringBuilder html = new StringBuilder();
        
        html.append("<div class=\"").append(getProperty("page")).append("\">");
        html.append("<fieldset class=\"").append(getProperty("block.noFrame")).append("\">");
        
        TagUtils.getInstance().write(pageContext, html.toString());
        
        AttributesTag.copyAttributesToPage(pageContext, panel);

        return process;
    }
    
    @Override
    public int doEndTag() throws JspException
    {        
        DeletePanel panel = (DeletePanel)Panel.getPanelStack((HttpServletRequest)pageContext.getRequest()).peek();

        StringBuilder html = new StringBuilder();
        
        html.append("</fieldset>");
        html.append("<fieldset class=\"").append(getProperty("block.action")).append("\">");

        renderButton(html, "doCancel", "button.cancel", "button.cancel.title", null, 'c', false);        

        if(panel.getEntity().getId() == null)
            renderButton(html,  "doDelete", "button.delete", "button.delete.title.inactive", null, null, true);
        else
            renderButton(html, "doDelete", "button.delete", "button.delete.title.active", null, 'd', false);
        
        html.append("</fieldset>");
        
        html.append("</div>");
        
        TagUtils.getInstance().write(pageContext, html.toString());
                
        return super.doEndTag();
    }
}
