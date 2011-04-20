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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.system.ListConfigurationVO;
import org.opencustomer.framework.db.util.engine.configuration.Configuration;
import org.opencustomer.framework.db.util.engine.configuration.Join;
import org.opencustomer.framework.db.util.engine.configuration.Property;
import org.opencustomer.webapp.action.EditPageAction;
import org.opencustomer.webapp.util.table.TableFactory;

public class PageColumnsAction extends EditPageAction<PageColumnsForm> {
    private static Logger log = Logger.getLogger(PageColumnsAction.class);

    @Override
    public void readForm(PageColumnsForm form, ActionMessages errors, HttpServletRequest request) {
        ListConfigurationVO listConfiguration = (ListConfigurationVO) getPanel().getEntity();
        
        if(form.getPositions().isEmpty()) {
            listConfiguration.setColumns(null);
        } else {
            ArrayList<Integer> positions = new ArrayList<Integer>();
            for(String value : form.getPositions().values()) {
                positions.add(Integer.valueOf(value));
            }
            Collections.sort(positions);
            
            Integer[] columns = new Integer[positions.size()]; 
            for(int i=0; i<columns.length; i++) {
                Integer position = positions.get(i);
                for(int column : form.getPositions().keySet()) {
                    if(position.toString().equals(form.getPositions().get(column))) {
                        columns[i] = column;
                        break;
                    }
                }
            }
            
            listConfiguration.setColumns(columns);
        }
    }
    
    @Override
    public void writeForm(PageColumnsForm form, ActionMessages errors, HttpServletRequest request) {
        ListConfigurationVO listConfiguration = (ListConfigurationVO) getPanel().getEntity();
        
        if(listConfiguration.getColumns() != null) {
            for(int i=0; i<listConfiguration.getColumns().length; i++) {
                form.setPosition(listConfiguration.getColumns()[i], Integer.toString(i+1));
            }
        }
    }

    @Override
    protected void preOperations(PageColumnsForm form, ActionMessages errors, HttpServletRequest request) {
        ListConfigurationVO listConfiguration = (ListConfigurationVO) getPanel().getEntity();
        
        if(getPanel().getAttribute("entities") == null) {
            if(log.isDebugEnabled()) {
                log.debug("add entities for "+listConfiguration.getType());
            }
            
            LinkedHashMap<String, Entity> entities = new LinkedHashMap<String, Entity>();

            Configuration configuration = TableFactory.getInstance().getConfiguration(listConfiguration.getType().getName());
            entities.put(configuration.getEntity().getMessageKey(), new Entity(configuration.getEntity().getMessageKey()));
            
            HashSet<Property> addedProperties = new HashSet<Property>();
            
            for(Property property : configuration.getProperties()) {
                if(property.getName().startsWith(configuration.getEntity().getAlias()+".")) {
                    entities.get(configuration.getEntity().getMessageKey()).getProperties().add(property);
                    addedProperties.add(property);
                } else if(configuration.getEntity().getMessageKey().equals(property.getEntityMessageKey())) {
                    entities.get(property.getEntityMessageKey()).getProperties().add(property);
                    addedProperties.add(property);
                }
            }
            
            for(Property property : configuration.getProperties()) {
                for(Join join : configuration.getJoins()) {
                    if(!addedProperties.contains(property)) {
                        if(property.getName().startsWith(join.getAlias()+".")) {
                            if(log.isDebugEnabled())
                                log.debug("add property by entity: "+property);
                            
                            if(!entities.containsKey(join.getMessageKey())) {
                                entities.put(join.getMessageKey(), new Entity(join.getMessageKey()));
                            }
                            entities.get(join.getMessageKey()).getProperties().add(property);
                            addedProperties.add(property);
                        } else if(join.getMessageKey().equals(property.getEntityMessageKey())) {
                            if(log.isDebugEnabled())
                                log.debug("add property by entity messageKey: "+property);
                            
                            if(!entities.containsKey(property.getEntityMessageKey())) {
                                entities.put(property.getEntityMessageKey(), new Entity(property.getEntityMessageKey()));
                            }
                            entities.get(property.getEntityMessageKey()).getProperties().add(property);
                            addedProperties.add(property);
                        }
                    }
                }
                
                // add property without entity
                if(!addedProperties.contains(property)) {
                    if(log.isDebugEnabled())
                        log.debug("add property without entity: "+property);
                    if(!entities.containsKey(property.getEntityMessageKey())) {
                        entities.put(property.getEntityMessageKey(), new Entity(property.getEntityMessageKey()));
                    }
                    entities.get(property.getEntityMessageKey()).getProperties().add(property);
                    addedProperties.add(property);
                }
            }
            
            getPanel().setAttribute("entities", entities.values());
        }
    }
    
    public class Entity {
        private String messageKey;
        
        private List<Property> properties = new ArrayList<Property>();

        public Entity() {
            
        }

        public Entity(String messageKey) {
            this.setMessageKey(messageKey);
            
        }
        
        public String getMessageKey() {
            return messageKey;
        }

        public void setMessageKey(String messageKey) {
            this.messageKey = messageKey;
        }

        public List<Property> getProperties() {
            return properties;
        }

        public void setProperties(List<Property> properties) {
            this.properties = properties;
        }
    }
}
