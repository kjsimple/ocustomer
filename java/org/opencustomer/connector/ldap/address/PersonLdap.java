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
import java.util.Set;

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
import org.opencustomer.db.dao.crm.PersonDAO;
import org.opencustomer.db.vo.crm.AddressVO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.framework.util.validator.EmailAddressValidator;
import org.opencustomer.framework.util.validator.TelephoneNumberValidator;
import org.opencustomer.util.configuration.SystemConfiguration;

/**
 * The class PersonLdap exports any PersonVO to an existing Ldap server. 
 * This class requeries the mozillaAbPersonAlpha schema on the Ldap server.
 * 
 * @author fbreske
 *
 */

public class PersonLdap {
    private static Logger log = Logger.getLogger(PersonLdap.class);
    
    private static PersonLdap personLdap;
    
    private Hashtable<String,String> env;
    
    public PersonLdap()
    {
        env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL,"ldap://" + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_SERVER) + ":" + SystemConfiguration.getInstance().getIntValue(SystemConfiguration.Key.LDAP_PORT) + "/" + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADDRESS_PERSON_PREFIX) + ","+SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADDRESS_PREFIX)+"," + SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_BASE_DN));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");    
        env.put(Context.SECURITY_PRINCIPAL, SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADMIN_USER));
        env.put(Context.SECURITY_CREDENTIALS, SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.LDAP_ADMIN_PASSWORD));
    }
    
    /**
     * 
     * @return instace of PersonLdap
     */
    public static PersonLdap getInstance()
    {
        if(personLdap == null)
           personLdap = new PersonLdap();
        return personLdap;      
    }
    
    public void insertLdapPerson(List<PersonVO> persons) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        for(PersonVO person : persons)
                insertLdapPerson(person);
    }
    
    /**
     * inserts a single PersonVO to the Ldap Server.
     * <table>
     * <tr><td>sn</td><td>getLastName</td></tr>
     * <tr><td>cn</td><td>getFirstName + getLastName</td></tr>
     * <tr><td>givenName</td><td>getFirstName</td></tr>
     * <tr><td>uid</td><td> getId</td></tr>
     * <tr><td>mail</td><td> getEmail</td></tr>
     * <tr><td>telephoneNumber</td><td> getPhone</td></tr>
     * <tr><td>fax</td><td> getFax</td></tr>
     * <tr><td>mobile</td><td> getMobile</td></tr>
     * <tr><td>title</td><td> getTitle</td></tr>
     * <tr><td>o</td><td> getCompany</td></tr>
     * <tr><td>ou</td><td> getFunction</td></tr>
     * <tr><td>description </td><td> getComment</td></tr>
     * <tr><td>mozillaWorkUrl </td><td>getCompany().getUrl</td></tr>
     * <tr><td>mozillaHomeUrl </td><td>getUrl</td></tr>
     * <tr><td>street</td><td>getStreet</td></tr>
     * <tr><td>l</td><td>getCity</td></tr>
     * <tr><td>postalCode</td><td>getZip</td></tr>
     * </table>
     * <br>
     * If MAIN address is available this address is used, otherwise the POSTAL address is used. If there is no
     * address, the addresses of the company is used.
     * @param person
     * @throws NamingException
     */
    public void insertLdapPerson(PersonVO person) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        DirContext ctx = new InitialDirContext(env); /* konfiguration laden */
        
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocls = new BasicAttribute("objectClass");//notwendige Ldap objectClasses
        ocls.add("top");
        ocls.add("person");
        ocls.add("organizationalPerson");
        ocls.add("inetOrgPerson");
        ocls.add("mozillaAbPersonAlpha"); //mozilla spezifisch
        attrs.put(ocls);
        
        /* notwendige Ldap felder fuellen */
        attrs.put("sn",person.getLastName());
        
        if(person.getFirstName() != null)
            attrs.put("cn",person.getFirstName() + " " + person.getLastName());
        else
            attrs.put("cn",person.getLastName());
        
        attrs.put("uid",person.getId().toString());
        
        /* optionale Ldap felder fuellen */
        if(person.getFirstName() != null)
            attrs.put("givenName",person.getFirstName());
        
        if(person.getEmail() != null && EmailAddressValidator.getInstance().validate((person.getEmail())))
            attrs.put("mail",person.getEmail());
        
        if(person.getPhone() != null && TelephoneNumberValidator.getInstance().validate(person.getPhone()))
            attrs.put("telephoneNumber", person.getPhone());
        
        if(person.getFax() != null && TelephoneNumberValidator.getInstance().validate(person.getFax()))
            attrs.put("fax",person.getFax());
        
        if(person.getMobile() != null && TelephoneNumberValidator.getInstance().validate(person.getMobile()))
            attrs.put("mobile",person.getMobile());
        
        if(person.getTitle() != null)
            attrs.put("title",person.getTitle());
        
        if(person.getCompany() != null)
            attrs.put("o",person.getCompany().getCompanyName());
        
        if(person.getFunction() != null)
            attrs.put("ou",person.getFunction());
        
        if(person.getComment() != null)
            attrs.put("description",person.getComment());
        
        if(person.getCompany() != null && person.getCompany().getUrl() != null)
            attrs.put("mozillaWorkUrl",person.getCompany().getUrl());
        
        if(person.getUrl() != null)
            attrs.put("mozillaHomeUrl",person.getUrl());
        
        /* Es wird die MAIN adresse verwendet, wenn nicht vorhanden wird die POSTAL adresse verwendet
         * sollte weder MAIN noch POSTAL adresse der Person vorhanden sein wird mit dem Company adressen weiterverwendet
         */
        
        for(AddressVO address: person.getAddresses())
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
        
        if(attrs.get("street") == null && person.getCompany() != null)
        {
            Set<AddressVO> addresses = new CompanyDAO().getById(person.getCompany().getId()).getAddresses();
            for(AddressVO address : addresses)
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
        }
        
        ctx.createSubcontext("uid=" + person.getId(),attrs);
        
    }
    
    /**
     * The same as insertLdapPerson with a delteLdapPerson befor.
     * @param persons
     * @throws NamingException
     */
    public void updateLdapPerson(List<PersonVO> persons) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        for(PersonVO person : persons)
                updateLdapPerson(person);
    }
    
    /**
     * The same as insertLdapPerson with a delteLdapPerson befor.
     * @param person
     * @throws NamingException
     */
    public void updateLdapPerson(PersonVO person) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        deleteLdapPerson(person);
        insertLdapPerson(person);
    }
    
    /**
     * Deletes the person from the Ldap server. The person is identified by the Id.
     * @param persons
     * @throws NamingException
     */
    public void deleteLdapPerson(List<PersonVO> persons) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        for(PersonVO person : persons)
            deleteLdapPerson(person);
    }
    
    /**
     * Deletes the person from the Ldap server. The person is identified by the Id.
     * @param person
     * @throws NamingException
     */
    public void deleteLdapPerson(PersonVO person) throws NamingException
    {
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_EXPORT_ENABLED))
            return;
        
        new InitialDirContext(env).unbind("uid=" + person.getId());       
    }
    
    /**
     * This method writes the all persons from the database to the ldap directory. 
     * @throws HibernateException on error reading persons from database
     * @throws NamingException on ldap error
     */
    public void initalLdapPersonUpdate() throws HibernateException, NamingException
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
        
        insertLdapPerson(new PersonDAO().getAll());
    }
}
