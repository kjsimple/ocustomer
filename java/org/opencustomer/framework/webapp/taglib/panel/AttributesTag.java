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
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.opencustomer.framework.webapp.panel.DeletePanel;
import org.opencustomer.framework.webapp.panel.EntityPanel;
import org.opencustomer.framework.webapp.panel.Panel;

public class AttributesTag extends TagSupport
{
    private final static Logger log = Logger.getLogger(AttributesTag.class);
    
    private static final long serialVersionUID = 428305407335964656L;
    
    @Override
    public int doStartTag() throws JspException
    {
        Panel panel = Panel.getPanelStack((HttpServletRequest)pageContext.getRequest()).peek();

        copyAttributesToPage(pageContext, panel);
        
        return super.doStartTag();
    }
    
    public static void copyAttributesToPage(PageContext pageContext,Panel panel) 
    {
        for(String name : panel.getAttributeNames()) 
            pageContext.setAttribute("panel_"+name, panel.getAttribute(name));
        
        pageContext.setAttribute("panel_path", panel.getPath());
        pageContext.setAttribute("panel_title", panel.getTitle());

        if(panel instanceof EntityPanel) {
            EntityPanel entityPanel = (EntityPanel)panel;
            pageContext.setAttribute("panel_entity", entityPanel.getEntity());
        }

        if(panel instanceof DeletePanel) {
            DeletePanel deletePanel = (DeletePanel)panel;
            pageContext.setAttribute("panel_question", deletePanel.getQuestion());
        }
    }
}
