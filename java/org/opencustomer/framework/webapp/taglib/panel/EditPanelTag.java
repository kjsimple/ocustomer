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
import org.apache.struts.Globals;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Page;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.webapp.auth.AuthenticatorUtility;
import org.opencustomer.webapp.auth.Right;

public class EditPanelTag extends FormPanelTag
{
    private static final Logger log = Logger.getLogger(EditPanelTag.class);
    
    private static final long serialVersionUID = -2488736893082549514L;

    private boolean panelEditable = false;
    
    private boolean pageEditable = false;
    
    @Override
    public int doStartTag() throws JspException
    {
        EditPanel panel = (EditPanel)Panel.getPanelStack((HttpServletRequest)pageContext.getRequest()).peek();

        panelEditable = panel.isEditable();
        pageEditable = panelEditable;
        if(panel.getActivePage().getEditable() != null) {
            pageEditable = panel.getActivePage().getEditable();
        }
        if(log.isDebugEnabled())
            log.debug("panelEditable="+panelEditable+", pageEditable="+pageEditable);
        
        pageContext.setAttribute("i_panel_editable", panelEditable);
        pageContext.setAttribute("i_page_editable", pageEditable);
        
        action = panel.getActivePage().getAction();
        styleClass = getProperty("form");
        setDisabled(!pageEditable);
        
        int process = super.doStartTag();
        
        StringBuilder html = new StringBuilder();
        
        if(panel.getPages().size() > 1)
        {
            html.append("<div class=\"").append(getProperty("slider")).append("\">");
            for(Page tab : panel.getPages())
            {
                if(isTabRightsValid(tab))
                {
                    if(tab.equals(panel.getActivePage()))
                        renderButton(html, "doPage_"+tab.getName(), tab.getMessageKey(), null, getProperty("slider.active"), null, true);
                    else
                    {
                        if(tab.isSelectable())
                            renderButton(html, "doPage_"+tab.getName(), tab.getMessageKey(), null, null, null, false);
                        else
                            renderButton(html, "doPage_"+tab.getName(), tab.getMessageKey(), null, getProperty("slider.inactive"), null, true);
                    }
                }
            }
            html.append("</div>");
        
            html.append("<div class=\"").append(getProperty("page.tabbed")).append("\">");
        }
        else
            html.append("<div class=\"").append(getProperty("page")).append("\">");
        
        TagUtils.getInstance().write(pageContext, html.toString());
        
        AttributesTag.copyAttributesToPage(pageContext, panel);
        
        return process;
    }
    
    @Override
    public int doEndTag() throws JspException
    {        
        EditPanel panel = (EditPanel)Panel.getPanelStack((HttpServletRequest)pageContext.getRequest()).peek();

        StringBuilder html = new StringBuilder();
        
        html.append("<fieldset class=\"").append(getProperty("block.action")).append("\">");
        
        if(panelEditable)
        {
            Action deleteAction = panel.getAction(Action.Type.DELETE);
            if(deleteAction != null)
            {
                if(panel.getEntity().getId() == null || !deleteAction.isSelectable())
                    renderButton(html,  "doDelete", "button.delete", "button.delete.title.inactive", getProperty("block.action.leftButton"), null, true);
                else
                    renderButton(html, "doDelete", "button.delete", "button.delete.title.active", getProperty("block.action.leftButton"), 'd', false);
            }
            
            renderButton(html, "doCancel", "button.cancel", "button.cancel.title", null, 'c', false);
            
            if(panel.getAction(Action.Type.SAVE) != null)
                renderButton(html, "doSave", "button.save", "button.save.title", null, 's', false);
        }
        else
            renderButton(html, "doCancel", "button.back", "button.cancel.title", null, 'c', false);
        
        html.append("</fieldset>");
        
        html.append("</div>");
        
        TagUtils.getInstance().write(pageContext, html.toString());
                
        return super.doEndTag();
    }
    
    private boolean isTabRightsValid(Page tab)
    {
        boolean valid = false;
        
        ModuleConfig moduleConfig = (ModuleConfig)pageContext.getServletContext().getAttribute(Globals.MODULE_KEY);
        ActionConfig actionConfig = moduleConfig.findActionConfig(tab.getAction());
        
        if(actionConfig != null)
        {
            Right[] rights = new Right[actionConfig.getRoleNames().length];
    
            for (int i = 0; i < actionConfig.getRoleNames().length; i++)
            {
                try
                {
                    rights[i] = Right.parseRight(actionConfig.getRoleNames()[i].trim());
                }
                catch (IllegalArgumentException e)
                {
                    log.warn("found invalid right for '" + actionConfig.getPath() + "' : " + actionConfig.getRoleNames()[i]);
                }
            }
            
            valid = AuthenticatorUtility.getInstance().authenticate((HttpServletRequest)pageContext.getRequest(), rights);
        }
        else
            log.error("no action can be found for: "+tab.getAction());
        
        return valid;
    }
    
    @Override
    public void release()
    {
        super.release();
        
        this.panelEditable = true;
    }
}
