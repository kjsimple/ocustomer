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

package org.opencustomer.framework.webapp.panel;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Page implements Serializable
{
    private static final long serialVersionUID = 660080361499534338L;

    /**
     * Constant to identify the page
     */
    private String name;

    /**
     * The message key;
     */
    private String messageKey;

    private String action;
    
    private boolean selectable;
    
    private Boolean editable;

    public Page(String name, String action, String messageKey, boolean selectable, Boolean editable) 
    {
        this.name = name;
        this.action = action;
        this.messageKey = messageKey;
        this.selectable = selectable;
        this.editable = editable;
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getMessageKey()
    {
        return messageKey;
    }

    public void setMessageKey(String messageKey)
    {
        this.messageKey = messageKey;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public boolean isSelectable()
    {
        return selectable;
    }

    public void setSelectable(boolean selectable)
    {
        this.selectable = selectable;
    }

    public Boolean getEditable()
    {
        return editable;
    }

    public void setEditable(Boolean editable)
    {
        this.editable = editable;
    }
    
    @Override
    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("name", name);
        builder.append("messageKey", messageKey);
        builder.append("action", action);
        builder.append("selectable", selectable);
        builder.append("editable", editable);
        
        return builder.toString();
    }    
}
