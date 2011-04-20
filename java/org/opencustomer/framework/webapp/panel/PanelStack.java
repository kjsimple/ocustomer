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

import java.util.Stack;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

public class PanelStack
{
    private final static Logger log = Logger.getLogger(PanelStack.class);
    
    private Stack<Panel> panels = new Stack<Panel>();
    
    public Panel push(Panel panel) {
        if (log.isDebugEnabled())
            log.debug("push panel");
        
        int index = panels.indexOf(panel);
                
        if(index != -1) {
            if(log.isDebugEnabled())
                log.debug("found duplicate panel at position: "+index);
            
            panels.setSize(index);
        }
        
        return panels.push(panel);
    }
    
    public Panel peek() {
        if (log.isDebugEnabled())
            log.debug("peek panel");
        
        if(panels.size() > 0)
            return panels.peek();
        else
            return null;
    }
    
    public Panel pop() {
        if (log.isDebugEnabled())
            log.debug("pop panel");
        
        return panels.pop();
    }
    
    public void clear() {
        panels.clear();
    }

    public boolean isEmpty() {
        return panels.isEmpty();
    }
    
    public int getSize() {
        return panels.size();
    }
    
    /**
     * 
     * @param nr the nr of element from the end to peek (starting with 1).
     * @return
     */
    public Panel peek(int nr) {
        int pos = panels.size()-nr;
        if(pos >= 0)
            return panels.get(pos);
        else
            return null;
    }
    
    public Panel pop(int nr) {
        Panel lastPop = null;
        
        for(int i=0; i<nr; i++) {
            if(!panels.isEmpty())
                lastPop = panels.pop();
        }
        
        return lastPop;
    }
    
    public boolean isTypeOnTop(Panel.Type type) {
        boolean isOnTop = false;
        
        if (!panels.isEmpty() && type.equals(panels.peek().getType()))
            isOnTop = true;
        
        return isOnTop;
    }
    
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("panels", panels);
        
        return builder.toString();
    }
}
