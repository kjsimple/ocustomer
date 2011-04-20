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
 *                 Felix Breske <felix.breske@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.connector.ldap.auth;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;
import org.opencustomer.db.dao.calendar.CalendarDAO;
import org.opencustomer.db.dao.system.LdapGroupDAO;
import org.opencustomer.db.dao.system.RoleDAO;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.system.LdapGroupVO;
import org.opencustomer.db.vo.system.ProfileVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.db.vo.EntityAccess.Access;
import org.opencustomer.framework.util.SignatureUtility;
import org.opencustomer.framework.util.password.PasswordGenerator;
import org.opencustomer.util.configuration.SystemConfiguration;

/**
 * Utilities for the LdapAuthenticator and the LdapSync.
 * @author fbreske
 *
 */
public class LdapUtil
{
    private static final Logger log = Logger.getLogger(LdapUtil.class); 
    
    private static LdapUtil instance;
    
    private Hashtable<String,String> env;
    
    private UserVO adminUser;
    
    public LdapUtil()
    {
        env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.factory.object", "com.sun.jndi.ldap.obj.LdapGroupFactory");
        env.put("java.naming.factory.state", "com.sun.jndi.ldap.obj.LdapGroupFactory");
        env.put(Context.PROVIDER_URL,"ldap://" + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_SERVER) + ":" + SystemConfiguration.getInstance().getIntValue(SystemConfiguration.Key.LDAP_PORT) + "/" + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_BASE_DN));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");    
        env.put(Context.SECURITY_PRINCIPAL, SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADMIN_USER));
        env.put(Context.SECURITY_CREDENTIALS, SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADMIN_PASSWORD));
        adminUser = new UserDAO().getByUserName(SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_SYSTEM_USER));
    }
    
    /**
     * 
     * @return instance of LdapUtil
     */
    public static LdapUtil getInstance()
    {
        if(instance == null)
            instance = new LdapUtil();
        
        return instance;
    }
   
    /**
     * 
     * @return HastTable with the settings for the ldap connection. (admin user and password)
     */
    public Hashtable<String, String> getLdapEnvironment()
    {
        return env;
    }

    /**
     * This method creates a new user account with the given username. The user profile is set to the corresponding
     * LdapGroup.
     * @param username 
     * @return the new UserVO or null if the user is no ldap user, or has no LdapGroup
     * @throws NamingException error on ladp connection
     */
    public UserVO createUserFromLdap(String username) throws NamingException
    {
        UserVO user = new UserDAO().getByUserName(username);
        
        if(!LdapAuthenticator.getInstance().isLdapUser(username))
        {
            if(log.isDebugEnabled())
                log.debug("no ldap user for username: " + username);
            return null;
        }
        else if(user == null)
        {
            LdapGroupVO profile = getProfileForUser(username);
            if(profile != null)
            {
                if(log.isDebugEnabled())
                    log.debug("creating new user: " + username);
                InitialDirContext ctx = new InitialDirContext(env);
                Attributes attrs = ctx.getAttributes("uid=" + username + "," + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_USER_PREFIX));
                
                // Defaultwerte setzen
                user = new UserVO();
                user.setUserName(attrs.get("Uid").get().toString());
                user.setProfile(profile.getProfile());
                user.setLocale(adminUser.getLocale());
                user.setOwnerUser(adminUser.getId());
                user.setOwnerGroup(adminUser.getId());
                user.setAccessUser(Access.WRITE_SYSTEM);
                user.setAccessGroup(Access.READ);
                user.setAccessGlobal(Access.READ);
                user.setPassword(SignatureUtility.getInstance().createSignature(new PasswordGenerator().generate()));
                
                new UserDAO().insert(user,adminUser);
                CalendarVO calendar = new CalendarVO();
                calendar.setUser(user);
                calendar.setAccessUser(EntityAccess.Access.WRITE_SYSTEM);
                calendar.setOwnerUser(user.getId());
                calendar.setAccessGroup(EntityAccess.Access.NONE);
                calendar.setOwnerGroup(user.getProfile().getDefaultUsergroup().getId());
                calendar.setAccessGlobal(EntityAccess.Access.NONE);
                
                new CalendarDAO().insert(calendar, user);
            }
            else
            {
                if(log.isDebugEnabled())
                    log.debug("user has no Ldap group: " + username);
            }
        }
        else if((user.getProfile().getRole().isAdmin() || (user.getProfile().getLdapGroup() == null))&& LdapAuthenticator.getInstance().isLdapUser(username))
        {
            log.error("username conflict: " +  username);
        }
        else
        {
            if(log.isDebugEnabled())
                log.debug("user already exists: " + username);
        }
        return user;
    }
    
    /**
     * This method creates a new LdapGroup from the given Binding
     * @param binding the binding of the corresponding LdapGroup
     * @return the new LdapGroupVO
     * @throws NamingException error on ladp connection
     */
    private LdapGroupVO createGroupFromLdap(String groupDN) throws NamingException
    {
        InitialDirContext ctx = new InitialDirContext(env);
        Attributes attrs = ctx.getAttributes(groupDN + "," + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_GROUP_PREFIX),new String[]{"cn"});
        
        LdapGroupVO ldapGroup = null;
        
        if(attrs.get("cn") != null)
        {
            String name = (String)attrs.get("cn").get(0); // TODO: what is this name for
            ldapGroup = new LdapGroupDAO().getByName(name);
            if(ldapGroup == null)
            {
                if(log.isDebugEnabled())
                    log.debug("creating new ldapGroup: " + name);
                ldapGroup = new LdapGroupVO();
                ldapGroup.setName(groupDN);
                ldapGroup.setOwnerGroup(adminUser.getProfile().getDefaultUsergroup().getId());
                ldapGroup.setOwnerUser(adminUser.getId());
                ldapGroup.setPriority(new LdapGroupDAO().getAll().size());
                ldapGroup.setAccessUser(Access.WRITE_SYSTEM);
                ldapGroup.setAccessGroup(Access.NONE);
                ldapGroup.setAccessGlobal(Access.NONE);
                ProfileVO profile = new ProfileVO();
                profile.setAccessGlobal(Access.READ);
                profile.setAccessGroup(Access.READ);
                profile.setAccessUser(Access.WRITE_SYSTEM);
                profile.setIpPattern("*");
                profile.setTimeLock(30);
                profile.setRole(new RoleDAO().getByName("User"));
                profile.setDefaultUsergroup(profile.getRole().getDefaultUsergroup());
                profile.setOwnerUser(adminUser.getId());
                profile.setOwnerGroup(adminUser.getProfile().getDefaultUsergroup().getId());
                ldapGroup.setProfile(profile);
                new LdapGroupDAO().insert(ldapGroup,adminUser);
            }
            else
            {
                if(log.isDebugEnabled())
                    log.debug("ldapGroup already exists: " + name);
            }
        }
        return ldapGroup;
    }
    
    /**
     * This method searches for the actual LdapGroup for the user.
     * @param username username to search for
     * @return the prefered LdapGroupVO or null if the user has no LdapGroup
     * @throws NamingException error on ladp connection
     */
    public LdapGroupVO getProfileForUser(String username) throws NamingException
    {
        String filter_groups = "(&(objectClass=groupOfUniqueNames)(uniqueMember={0}))";
        String filter_user = "(&(objectClass=inetOrgPerson)(uid={0}))";
        
        List<LdapGroupVO> ldapGroups = new ArrayList<LdapGroupVO>();
        
        InitialDirContext ctx = new InitialDirContext(env);
        NamingEnumeration<SearchResult> enm = ctx.search(SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_USER_PREFIX),filter_user,new String[]{username},null);
        
        while(enm.hasMore())
        {
            NamingEnumeration<SearchResult> enm2 = ctx.search(SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_GROUP_PREFIX),filter_groups,new String[]{enm.next().getNameInNamespace()},null);
            while(enm2.hasMore())
            {
                LdapGroupVO tmpGroup = new LdapGroupDAO().getByDN(enm2.next().getName());
                ldapGroups.add(tmpGroup);
                if(log.isDebugEnabled())
                    log.debug("found ldap group " + tmpGroup.getName() + " for user " + username);
            }
        }
        
        LdapGroupVO preferdGroup = null;      
        for(LdapGroupVO tmp : ldapGroups)
        {
            if(preferdGroup == null || preferdGroup.getPriority() > tmp.getPriority())
                preferdGroup = tmp;
        }
        
        if(log.isDebugEnabled()){
            if(preferdGroup == null)
                log.debug("user has no group");
            else
                log.debug("prefered group is: " + preferdGroup.getName());
        }
        return preferdGroup;
    }
    
    /**
     * Test the ldap connection.
     * @return true if connection successfull, false if not
     */
    public boolean testLdapConnection()
    {
        InitialDirContext ctx;
        try
        {
            ctx = new InitialDirContext(env);
            ctx.lookup("");
            return true;
        }
        catch (NamingException e)
        {
            log.error("no ldap connection");
            return false;
        }   
    }
    
    /**
     * reads the oc ldap groups from ldap server
     * @return a list of group names (DN)
     * @throws NamingException
     */
    public List<String> getLdapGroups() throws NamingException
    {
        List<String> groupList = new ArrayList<String>();
        InitialDirContext ctx = new InitialDirContext(env);
        NamingEnumeration<Binding> enm = ctx.listBindings(SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_GROUP_PREFIX));
        while (enm.hasMore())
        {
            Binding b = enm.next();
            groupList.add(b.getName());
        }
        return groupList;
    }
}
