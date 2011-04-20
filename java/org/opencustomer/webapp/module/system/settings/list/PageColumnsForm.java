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

package org.opencustomer.webapp.module.system.settings.list;

import java.util.Collection;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.db.util.engine.configuration.Property;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageForm;
import org.opencustomer.webapp.module.system.settings.list.PageColumnsAction.Entity;

public final class PageColumnsForm extends EditPageForm {
    private static final long serialVersionUID = 3258690996551430965L;

    private final static Logger log = Logger.getLogger(PageColumnsForm.class);
    
    private Hashtable<Integer, String> positions = new Hashtable<Integer, String>();
    
    @Override
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request) {
        
        Collection<Entity> entities = (Collection<Entity>)getPanel().getAttribute("entities");
        
        for(Integer key : positions.keySet()) {
            String value = positions.get(key);
            
            try {
                positions.put(key, Integer.toString(Integer.parseInt(value)));
            } catch(NumberFormatException e) {
                for(Entity entity : entities) {
                    for(Property property : entity.getProperties()) {
                        if(key.equals(property.getPosition())) {
                            errors.add("position["+key+"]", new ActionMessage("default.error.invalidValue.integer.positive", MessageUtil.message(request, property.getMessageKey())));
                            break;
                        }
                    }
                    
                }
            }
        }
    }

    public final String getPosition(int id) {
        if(positions.containsKey(id)) {
            return positions.get(id);
        } else {
            return null;
        }
    }

    public final void setPosition(int id, String value) {
        value = FormUtility.adjustParameter(value);
        if(value == null) {
            positions.remove(id);
        } else {
            positions.put(id, value);
        }
    }
    
    public Hashtable<Integer, String> getPositions() {
        return positions;
    }
}
