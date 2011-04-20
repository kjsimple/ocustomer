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

package org.opencustomer.webapp.util.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.opencustomer.webapp.auth.Right;

public final class MenuItem implements Cloneable {
    
    private final static Logger log = Logger.getLogger(MenuItem.class);
    
    private int id;
    
    private String action; 
    
    private String imageKey;
    
    private String altKey;
    
    private String messageKey;
    
    private String titleKey;

    private MenuItem parentItem;
    
    private ArrayList<MenuItem> childItems = new ArrayList<MenuItem>();
    
    private Right[] rights = new Right[0];
    
    private boolean active = false;
    
    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getAltKey()
    {
        return altKey;
    }

    public void setAltKey(String altKey)
    {
        this.altKey = altKey;
    }

    public List<MenuItem> getChildItems()
    {
        return childItems;
    }

    public void setChildItems(List<MenuItem> childItems)
    {
        this.childItems = new ArrayList<MenuItem>(childItems);
    }

    public String getMessageKey()
    {
        return messageKey;
    }

    public void setMessageKey(String messageKey)
    {
        this.messageKey = messageKey;
    }

    public String getImageKey()
    {
        return imageKey;
    }

    public void setImageKey(String imageKey)
    {
        this.imageKey = imageKey;
    }

    public String getTitleKey()
    {
        return titleKey;
    }

    public void setTitleKey(String titleKey)
    {
        this.titleKey = titleKey;
    }

    public Right[] getRights()
    {
        return rights;
    }

    public void setRights(Right[] rights)
    {
        this.rights = rights;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public MenuItem getParentItem()
    {
        return parentItem;
    }

    public void setParentItem(MenuItem parentItem)
    {
        this.parentItem = parentItem;
    }

    @Override
    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("id", id);
        builder.append("active", active);
        builder.append("action", action);
        builder.append("imageKey", imageKey);
        builder.append("altKey", altKey);
        builder.append("messageKey", messageKey);
        builder.append("titleKey", titleKey);
        builder.append("rights", toString(rights));
        builder.append("parentItem", parentItem);
        builder.append("childItems(size)", childItems.size());
        
        return builder.toString();
    }
    
    private static String toString(Right[] rights) {
        StringBuilder builder = new StringBuilder();
        
        for(int i=0; i<rights.length; i++) {
            if(i>0) {
                builder.append(",");
            }
            builder.append(rights[i]);
        }
        
        return builder.toString();
    }
    
    @Override
    public Object clone()
    {
        try {
            MenuItem item = (MenuItem)super.clone();

            item.childItems = (ArrayList<MenuItem>)this.childItems.clone(); 
            item.childItems.clear();
            for(MenuItem childItem : this.childItems) {
                MenuItem newChildItem = (MenuItem)childItem.clone();
                newChildItem.parentItem = item;
                item.childItems.add(newChildItem);
            }
            item.rights = rights.clone();

            return item;
        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
