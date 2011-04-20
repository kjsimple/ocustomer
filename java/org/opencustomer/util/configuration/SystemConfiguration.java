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
 * Software-Ingenieurb�ro). Portions created by the Initial Developer are
 * Copyright (C) 2006 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.util.configuration;

import java.util.List;

import org.apache.log4j.Logger;
import org.opencustomer.db.dao.system.ConfigurationDAO;
import org.opencustomer.db.vo.system.ConfigurationVO;
import org.opencustomer.framework.util.EnumUtility;

public final class SystemConfiguration extends Configuration<SystemConfiguration.Key> {

    private final static Logger log = Logger.getLogger(SystemConfiguration.class);
    
    private static SystemConfiguration instance;
    
    public static enum Key implements ConfigurationKey {
        MAX_FAILED_LOGINS            (Integer.class, new Integer(3)),
//        DEFAULT_MAX_SESSION_INACTIVE (Integer.class, new Integer(300)),
        DEFAULT_TIMELOCK             (Integer.class, new Integer(30)),
        DEFAULT_IPPATTERN            (String.class,  "*"),
        MAIL_SMTP_SERVER             (String.class),
        SHOW_OVERVIEW                (Boolean.class, Boolean.TRUE),
        MULTIPLE_LOGINS              (Boolean.class, Boolean.TRUE),
        LDAP_AUTHENTICATION_ENABLED  (Boolean.class, Boolean.FALSE),
        LDAP_ADDRESS_EXPORT_ENABLED  (Boolean.class, Boolean.FALSE),
        LDAP_ADDRESS_INITIAL_EXPORT  (Boolean.class, Boolean.FALSE),
        LDAP_SERVER                  (String.class),
        LDAP_PORT                    (Integer.class),
        LDAP_ADMIN_USER              (String.class),
        LDAP_ADMIN_PASSWORD          (String.class),
        LDAP_BASE_DN                 (String.class),
        LDAP_ADDRESS_PREFIX          (String.class),
        LDAP_ADDRESS_COMPANY_PREFIX  (String.class),
        LDAP_ADDRESS_PERSON_PREFIX   (String.class),
        LDAP_USER_PREFIX             (String.class),
        LDAP_GROUP_PREFIX            (String.class),
        LDAP_SYSTEM_USER             (String.class);

        private Class clazz;

        private Object defaultValue;
        
        private Key(Class clazz) {
            this(clazz, null);
        }
        
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
    
    private SystemConfiguration() {
        refresh();
    }

    public static SystemConfiguration getInstance() {
        if(instance == null)
            instance = new SystemConfiguration();
        
        return instance;
    }
    
    @Override
    protected final SystemConfiguration.Key getKey(ConfigurationVO vo) {
        return EnumUtility.valueOf(SystemConfiguration.Key.class,vo.getKey());
    }
    
    @Override
    public void refresh() {
        getConfigurations().clear();
        
        List<ConfigurationVO> systemConfigurations = new ConfigurationDAO().getForSystem();
        for(ConfigurationVO vo : systemConfigurations) {
            addConfiguration(vo);
        }
    }
}
