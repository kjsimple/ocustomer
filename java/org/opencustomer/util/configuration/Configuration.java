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

package org.opencustomer.util.configuration;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.opencustomer.db.vo.system.ConfigurationVO;

abstract class Configuration<E extends ConfigurationKey> {

    private final static Logger log = Logger.getLogger(Configuration.class);

    private Hashtable<E, Object> configurations = new Hashtable<E, Object>();

    protected final Hashtable<E, Object> getConfigurations() {
        return configurations;
    }

    public abstract void refresh();
    
    protected abstract E getKey(ConfigurationVO vo);
    
    protected final void addConfiguration(ConfigurationVO vo) {
        E key = getKey(vo);
        if(key != null) {
            configurations.put(key, toType(key, vo));
        } else {
            log.warn("could not find enum for key: "+vo.getKey());
        }
    }
    
    protected final Object toType(E key, ConfigurationVO vo) {
        Object transformedValue = null;

        if(String.class.equals(key.getType())) {
            transformedValue = vo.getValue();
        } else if(Integer.class.equals(key.getType())) {
            transformedValue = new Integer(vo.getValue());
        } else if(Boolean.class.equals(key.getType())) {
            transformedValue = new Boolean(vo.getValue());
        } else {
            log.warn("no valid transformer found for "+key.getType());
        }
        
        return transformedValue;
    }
    
    protected final String toString(E key, Object value) {
        String stringValue = null;

        if(key.getType().equals(value.getClass())) {
            if(String.class.equals(key.getType())) {
                stringValue = value.toString();
            } else if(Integer.class.equals(key.getType())) {
                stringValue = value.toString();
            } else {
                log.warn("no valid transformer found for "+key.getType());
            }
        } else {
            log.warn("invalid type "+key.getType());
        }
        
        return stringValue;
    }
    
    public final Object getValue(E key) {
        Object value = null;
        
        value = configurations.get(key);
        
        if(value == null) {
            value = key.getDefaultValue();
        }
        
        return value;
    }
    
    public final int getIntValue(E key) {
        Integer value = (Integer)getValue(key);
        if(value != null)
            return value.intValue();
        else
            return 0;
    }
    
    public final String getStringValue(E key) {
        return (String)getValue(key);
    }
    
    public final boolean getBooleanValue(E key) {
        return ((Boolean)getValue(key)).booleanValue();
    }
}
