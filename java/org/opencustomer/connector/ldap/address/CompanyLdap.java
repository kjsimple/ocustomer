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

package org.opencustomer.connector.ldap.address;

import java.util.Hashtable;
import java.util.List;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.crm.CompanyDAO;
import org.opencustomer.db.vo.crm.AddressVO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.framework.util.validator.EmailAddressValidator;
import org.opencustomer.framework.util.validator.TelephoneNumberValidator;
import org.opencustomer.util.configuration.SystemConfiguration;

/**
 * The class CompanyLdap exports any CompanyVO to an existing Ldap server. 
 * This class requeries the mozillaAbPersonAlpha schema on the Ldap server.
 * 
 * @author fbreske
 *
 */
public class CompanyLdap {
    private static Logger log = Logger.getLogger(PersonLdap.class);
    
    private static CompanyLdap instance;
    
    private Hashtable<String,String> env;
    
    public CompanyLdap() {
        env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL,"ldap://" + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_SERVER) + ":" + SystemConfiguration.getInstance().getIntValue(SystemConfiguration.Key.LDAP_PORT) + "/" + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADDRESS_COMPANY_PREFIX) + "," +SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADDRESS_PREFIX)+ ","+ SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_BASE_DN));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");    
        env.put(Context.SECURITY_PRINCIPAL, SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADMIN_USER));
        env.put(Context.SECURITY_CREDENTIALS, SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADMIN_PASSWORD));
    }
    
    /**
     * 
     * @return instance of CompanyLdap
     */
    public static CompanyLdap getInstance() {
        if(instance == null)
            instance = new CompanyLdap();
        
        return instance;
    }
    
    /**
     * @param companies
     * @throws NamingException
     */
    public void insertLdapCompany(List<CompanyVO> companies) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        for(CompanyVO company : companies)
            insertLdapCompany(company);
    }

    /**
     * writes a single CompanyVO to the Ldap Server.
     * <table>
     * <tr><td>o, cn</td><td>getcompanyName</td></tr>
     * <tr><td>sn</td><td> " " (empty)</td></tr>
     * <tr><td>uid </td><td> getId</td></tr>
     * <tr><td>mail </td><td> getEmail</td></tr>
     * <tr><td>telephoneNumber </td><td> getPhone</td></tr>
     * <tr><td>fax </td><td> getFax</td></tr>
     * <tr><td>description </td><td> getComment</td></tr>
     * <tr><td>mozillaWorkUrl </td><td> getUrl</td></tr>
     * <tr><td>street</td><td>getStreet</td></tr>
     * <tr><td>l</td><td>getCity</td></tr>
     * <tr><td>postalCode</td><td>getZip</td></tr>
     * </table>
     * <br>
     * If MAIN address is available this address is used, otherwise the POSTAL address is used.
     * @param company
     * @throws NamingException
     */
    public void insertLdapCompany(CompanyVO company) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        DirContext ctx = new InitialDirContext(env); /* konfiguration laden */
        
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocls = new BasicAttribute("objectClass"); //notwendige Ldap objectClasses
        ocls.add("top");
        ocls.add("person");
        ocls.add("organizationalPerson");
        ocls.add("inetOrgPerson");
        ocls.add("mozillaAbPersonAlpha"); //mozilla spezifisch
        attrs.put(ocls);
        
        /* notwendige Ldap felder fuellen */
        attrs.put("cn",company.getCompanyName());
        attrs.put("sn"," ");
        attrs.put("uid",company.getId().toString());
        attrs.put("o",company.getCompanyName());
        
        /* optionale Ldap felder fuellen */
        if(company.getEmail() != null && EmailAddressValidator.getInstance().validate((company.getEmail())))
            attrs.put("mail",company.getEmail());
        
        if(company.getPhone() != null && TelephoneNumberValidator.getInstance().validate(company.getPhone()))
            attrs.put("telephoneNumber", company.getPhone());
        
        if(company.getFax() != null && TelephoneNumberValidator.getInstance().validate(company.getFax()))
            attrs.put("fax",company.getFax());
        
        if(company.getComment() != null)
            attrs.put("description",company.getComment());
        
        if(company.getUrl() != null)
            attrs.put("mozillaWorkUrl",company.getUrl());
        
        /* MAIN adresse verwenden wenn vorhanden, anderenfalls POSTAL adresse */
        for(AddressVO address : company.getAddresses())
        {
            if(address.getType() == AddressVO.Type.MAIN)
            {
                if(address.getStreet() != null)
                    attrs.put("street",address.getStreet());
                
                if(address.getCity() != null)
                    attrs.put("l",address.getCity());
                
                if(address.getZip() != null)
                    attrs.put("postalCode",address.getZip());
            }
            
            if(attrs.get("street") == null && address.getType() == AddressVO.Type.POSTAL)
            {
                if(address.getCity() != null)
                    attrs.put("l",address.getCity());
                
                if(address.getZip() != null)
                    attrs.put("postalCode",address.getZip());
                
                if(address.getStreet() != null)
                    attrs.put("street",address.getStreet());
                else if(address.getPostbox() != null)
                    attrs.put("street",address.getPostbox());
            }
        }
        
        ctx.createSubcontext("uid=" + company.getId(),attrs); // ldap export, uid als dn
    }
   
    /**
     * The same as insertLdapCompany with a delteLdapCompany before.
     * @param companies
     * @throws NamingException
     */
    public void updateLdapCompany(List<CompanyVO> companies) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        for(CompanyVO company : companies)
            updateLdapCompany(company);
    }
    
    /**
     * The same as insertLdapCompany with a delteLdapCompany before.
     * @param company
     * @throws NamingException
     */
    public void updateLdapCompany(CompanyVO company) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        deleteLdapCompnay(company);
        insertLdapCompany(company);
    }
    
    /**
     * Deletes the list of companies from the Ldap server. The company is identified by the companyId.
     * @param companies
     * @throws NamingException
     */
    public void deleteLdapCompany(List<CompanyVO> companies) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        for(CompanyVO company : companies)
            deleteLdapCompnay(company);
    }
    
    /**
     * Deletes the company from the Ldap server. The company is identified by the companyId.
     * @param company
     * @throws NamingException
     */
    public void deleteLdapCompnay(CompanyVO company) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        new InitialDirContext(env).unbind("uid=" + company.getId());
    }
    
    /**
     * This method writes the all companies from the database to the ldap directory. 
     * @throws HibernateException on error reading companies from database
     * @throws NamingException on ldap error
     */
    public void initalLdapCompanyUpdate() throws HibernateException, NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        DirContext ctx = new InitialDirContext(env);
        NamingEnumeration enm = ctx.listBindings("");
        while (enm.hasMore())
        {
            Binding b = (Binding) enm.next();
            ctx.unbind(b.getName());
        }
        
        insertLdapCompany(new CompanyDAO().getAll());
    }
}
