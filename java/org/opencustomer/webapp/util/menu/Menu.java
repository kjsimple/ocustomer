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

public final class Menu implements Cloneable {
    private final static Logger log = Logger.getLogger(Menu.class);
    
    private ArrayList<MenuItem> items = new ArrayList<MenuItem>();

    private MenuItem activeItem;
    
    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = new ArrayList<MenuItem>(items);
    }

    void initialize() {
        generateIds(this.items, 0);
        activate(0);
    }
    
    private static int generateIds(List<MenuItem> items, int id) {
        for(MenuItem item : items)  {
            item.setId(id++);
            id = generateIds(item.getChildItems(), id);
        }
        
        return id;
    }
    
    public MenuItem activate(int id) {
        activeItem = findActiveItem(items, id);
        
        if(activeItem != null) {
            // activate super
            MenuItem item = activeItem;
            while(item.getParentItem() != null) {
                item = item.getParentItem();
                item.setActive(true);
            }
            
            // activate sub
            item = activeItem;
            while(!item.getChildItems().isEmpty()) {
                item = item.getChildItems().get(0);
                item.setActive(true);
                activeItem = item; // move active item to an item with an action
            }
        } else if(activeItem == null && id == 0) {
            throw new RuntimeException("cannot activate id 0 / configuration error)");
        } else {
            if(log.isDebugEnabled())
                log.debug("tried to activate invalid id: "+id);
            activeItem = activate(0);
        }
        
        return activeItem;
    }
    
    private static MenuItem findActiveItem(List<MenuItem> items, int id) {
        MenuItem activeItem = null;
        for(MenuItem item : items) {
            MenuItem foundItem = findActiveItem(item.getChildItems(), id);
            if(foundItem != null)
                activeItem = foundItem;
            
            if(item.getId() == id) {
                item.setActive(true);
                activeItem = item;
            } else {
                item.setActive(false);
            }
        }
        
        return activeItem;
    }
    
    public MenuItem getActiveItem() {
        return activeItem;
    }
    
    public MenuItem findItem(String action) {
        return findItem(action, items);
    }
    
    private MenuItem findItem(String action, List<MenuItem> items) {
        MenuItem foundItem = null;
        
        for(MenuItem item : items) {
            if(action.equals(item.getAction())) {
                foundItem = item;
            } else {
                foundItem = findItem(action, item.getChildItems());
            }
            
            if(foundItem != null)
                break;
        }
        
        return foundItem;
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("activeItem", activeItem);
        builder.append("items(size)", items.size());
        
        return builder.toString();
    }
    
    @Override
    public Object clone() {
        try {
            Menu menu = (Menu)super.clone();
            menu.items = (ArrayList<MenuItem>)this.items.clone(); 
            menu.items.clear();

            for(MenuItem item : this.items) {
                menu.items.add((MenuItem)item.clone());
            }

            if(this.activeItem != null) {
                activate(this.activeItem.getId());
            }
            
            return menu;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
