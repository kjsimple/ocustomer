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
 * Copyright (C) 2006 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */
package org.opencustomer.framework.webapp.panel;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.opencustomer.framework.webapp.struts.StrutsUtility;

public class Panel implements Serializable
{
    private static final long serialVersionUID = -5729352185697147403L;

    private final static Logger log = Logger.getLogger(Panel.class);
    
    private static final String PANEL_STACK_KEY = "org.opencustomer.framework.webapp.panel.PANEL_STACK";
    
    public static enum Type {
        LIST,
        EDIT,
        DELETE,
        CHOOSE;
    }
    
    private Type type;
    
    private String name;
    
    private String title;
    
    private Hashtable<String, Object> attributes = new Hashtable<String, Object>();

    private String path = null;
    
    public Panel(Type type) {
        this.type = type;
    }
    
    public final String getTitle()
    {
        return title;
    }

    public final void setTitle(String title)
    {
        this.title = title;
    }

    public final void setAttribute(String name, Object value)
    {
        attributes.put(name, value);
    }
    
    public final Object getAttribute(String name)
    {
        return attributes.get(name);
    }
    
    public final void removeAttribute(String name)
    {
        attributes.remove(name);
    }
    
    public final Set<String> getAttributeNames()
    {
        return attributes.keySet();
    }

    public final String getPath()
    {
        return path;
    }

    public final void setPath(String path)
    {
        this.path = path;
    }
    
    public Type getType()
    {
        return type;
    }
    
    public void setType(Type type)
    {
        this.type = type;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        boolean isEqual = false;

        if (obj == null)
            isEqual = false;
        else if (this == obj)
            isEqual = true;
        else if (!(obj instanceof Panel))
            isEqual = false;
        else
        {
            Panel castObj = (Panel) obj;

            EqualsBuilder builder = new EqualsBuilder();

            builder.append(this.getType(), castObj.getType());

            isEqual = builder.isEquals();
        }

        return isEqual;
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("type", type);
        builder.append("title", title);
        builder.append("path", path);
        builder.append("attributes.size", attributes.size());
        
        return builder.toString();
    }
    
    public static PanelStack getPanelStack(HttpServletRequest request) 
    {
        PanelStack panelStack = (PanelStack)request.getSession().getAttribute(PANEL_STACK_KEY);
        if(panelStack == null)
        {
            panelStack = new PanelStack();
            request.getSession().setAttribute(PANEL_STACK_KEY, panelStack);
        }
        
        return panelStack;
    }
    
    public static ActionForward getForward(String path, HttpServletRequest request) {
        return new ActionForward(StrutsUtility.buildContextPath(request, path));
    }
    
    public final String getName()
    {
        return name;
    }

    public final void setName(String name)
    {
        this.name = name;
    }
    
    public static boolean validate(HttpServletRequest request) {
        boolean valid = true;
        
        Panel panel = Panel.getPanelStack(request).peek();
        
        if(panel != null) {
            String expectedPath = request.getContextPath()+StrutsUtility.buildContextPath(request, panel.getPath());
            String realPath     = request.getRequestURI();

            if(!expectedPath.equals(realPath)) {
                valid = false;
                log.warn("path is not valid (expected:"+expectedPath+" / real: "+realPath+")");
            }
        }
        
        if(log.isDebugEnabled())
            log.debug("expected path is valid: "+valid);
        
        return valid;
    }
    
}
