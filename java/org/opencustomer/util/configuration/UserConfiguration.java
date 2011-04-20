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

import java.util.List;

import org.opencustomer.db.dao.system.ConfigurationDAO;
import org.opencustomer.db.vo.system.ConfigurationVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.util.EnumUtility;

public final class UserConfiguration extends Configuration<UserConfiguration.Key> {

    public static enum Key implements ConfigurationKey {
        LIST_NUMBER_ROWS             (Integer.class, new Integer(20)),
        CALENDAR_FIRST_DAY_OF_WEEK   (Integer.class, new Integer(java.util.Calendar.MONDAY));

        private Class clazz;
        
        private Object defaultValue;
        
        private Key(Class clazz, Object defaultValue) {
            this.clazz        = clazz; 
            this.defaultValue = defaultValue;
        }
       
       public Class getType() {
           return clazz;
       }
       
       public Object getDefaultValue() {
           return defaultValue;
       }
    }
    
    private UserVO user;
    
    public UserConfiguration(UserVO user) {
        this.user = user;
        
        refresh();
    }
    
    @Override
    protected final UserConfiguration.Key getKey(ConfigurationVO vo) {
        return EnumUtility.valueOf(UserConfiguration.Key.class,vo.getKey());
    }
    
    @Override
    public void refresh() {
        getConfigurations().clear();
        
        List<ConfigurationVO> userConfigurations = new ConfigurationDAO().getForUser(user);
        for(ConfigurationVO vo : userConfigurations) {
            addConfiguration(vo);
        }
    }
}
