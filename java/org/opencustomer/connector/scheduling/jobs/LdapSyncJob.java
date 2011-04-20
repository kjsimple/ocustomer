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
 * Copyright (C) 2005 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 *                 Felix Breske <felix.breske@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.connector.scheduling.jobs;

import java.util.List;

import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;
import org.opencustomer.connector.ldap.auth.LdapSync;
import org.opencustomer.connector.ldap.auth.LdapUtil;
import org.opencustomer.db.dao.system.LdapGroupDAO;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.system.LdapGroupVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.util.configuration.SystemConfiguration;

import com.jtheory.jdring.AlarmEntry;
import com.jtheory.jdring.AlarmListener;

/**
 * This class handels the syncronisation of the ldap server and the opencustomer database.
 * The job checks if all LdapGroups still exists. If the group is not available, the corresponding profile is locked.
 * For all user the job runs syncLdapUser from LdapSync.
 * @author fbreske
 *
 */
public final class LdapSyncJob implements AlarmListener {
    private final static Logger log = Logger.getLogger(LdapSyncJob.class);

    public void handleAlarm(AlarmEntry entry) {
        if(log.isDebugEnabled())
            log.debug("starting ldap sync job");
        
        if(SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_AUTHENTICATION_ENABLED)) {
            try {
                InitialDirContext ctx = new InitialDirContext(LdapUtil.getInstance().getLdapEnvironment());
    //            erzeugt zu jeder gruppe automatisch eine ldap group, nur zum testen
    //            NamingEnumeration<Binding> enm = ctx.listBindings(LDAPSettings.GROUP_PREFIX);
    //            while (enm.hasMore())
    //            {
    //                Binding b = enm.next();
    //                LdapUtil.getInstance().createGroupFromLdap(b.getName());
    //            }
                List<LdapGroupVO> ldapGroups = new LdapGroupDAO().getAll();
                for(LdapGroupVO group : ldapGroups) {
                    try {
                        ctx.lookup(group.getName()+ "," + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_GROUP_PREFIX));
                    } catch(NamingException e) {
                        if(!group.getProfile().isLocked()) {
                            if(log.isDebugEnabled())
                                log.debug("locked profile [" + group.getProfile()+"]");
                            group.getProfile().setLocked(true);
                            new LdapGroupDAO().insertOrUpdate(group);
                        }
                    }
                }
    
                if(log.isDebugEnabled())
                    log.debug("starting syncLdapUser");
                       
                NamingEnumeration<Binding> enm2 = ctx.listBindings(SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_USER_PREFIX));
                while (enm2.hasMore()) {
                    Attributes attrs = ctx.getAttributes(enm2.next().getName() + "," + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_USER_PREFIX),new String[]{"uid"});
                        
                    if(attrs.get("uid") != null) {
                        String username = (String)attrs.get("uid").get(0);                   
                        LdapSync.getInstance().syncLdapUser(username);
                    }
                }
                
                for(UserVO user : new UserDAO().getAll()) {
                    LdapSync.getInstance().syncLdapUser(user.getUserName());
                }
                if(log.isDebugEnabled())
                    log.debug("ending syncLdapUser");
    
            } catch (NamingException e) {
                log.error("cannot sync with ldap, no connection?",e);
            }
            
            if(log.isDebugEnabled())
                log.debug("ending ldap sync job " + entry);
        }
    }
}
