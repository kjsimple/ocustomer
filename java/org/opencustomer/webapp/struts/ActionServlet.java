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

package org.opencustomer.webapp.struts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.hibernate.HibernateException;
import org.opencustomer.connector.ldap.address.CompanyLdap;
import org.opencustomer.connector.ldap.address.PersonLdap;
import org.opencustomer.connector.scheduling.SchedulingManager;
import org.opencustomer.framework.util.LocaleUtility;
import org.opencustomer.framework.webapp.taglib.common.SortTag;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.Settings;
import org.opencustomer.webapp.util.menu.MenuFactory;
import org.opencustomer.webapp.util.menu.MenuFactoryException;

public class ActionServlet extends org.apache.struts.action.ActionServlet
{
    private static final long serialVersionUID = 3256437014978114355L;

    private static Logger log = Logger.getLogger(ActionServlet.class);

    public ActionServlet()
    {
        super();
    }

    protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        super.process(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    public void init() throws ServletException
    {
        super.init();

        if (log.isInfoEnabled())
            log.info("initialize ActionServlet");

        ModuleConfig moduleConfig = (ModuleConfig)getServletContext().getAttribute(org.apache.struts.Globals.MODULE_KEY);

        // load menu
        try {
            getServletContext().setAttribute(Globals.MENU_FACTORY_KEY, new MenuFactory(moduleConfig));
        } catch(MenuFactoryException e) {
           log.error("can not instantiate menu factory", e);
        }
        
        // dummy setzen der props
        Settings settings = Settings.getInstance();
        getServletContext().setAttribute(Globals.SETTINGS_KEY, settings);
        
        // Einlesen und setzen der Klassen-Properties
        File classProps = new File(this.getServletContext().getRealPath("/WEB-INF/properties/default_class.properties"));
        if (!classProps.exists())
            log.error("missing file '" + classProps + "' for default class settings");
        else
        {
            Properties properties = new Properties();
            try
            {
                properties.load(new FileInputStream(classProps));

                SortTag.load(properties);

                if (log.isInfoEnabled())
                    log.info("set default class settings with '" + classProps + "'");
            }
            catch (FileNotFoundException e)
            {
                log.error("could not load file '" + classProps + "' for default class settings");
            }
            catch (IOException e)
            {
                log.error("could not load file '" + classProps + "' for default class settings");
            }
        }
        
        //Einlesen der Ldap Konfiguration (fbreske)
        if(SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_ADDRESS_INITIAL_EXPORT)) {
            if (log.isInfoEnabled())
                log.info("Starting LDAP address export");
            try
            {
                CompanyLdap.getInstance().initalLdapCompanyUpdate();
                PersonLdap.getInstance().initalLdapPersonUpdate();
            }
            catch (HibernateException e)
            {
                log.error("could not update LDAP address book (database error)");
                e.printStackTrace();
            }
            catch (NamingException e)
            {
                log.error("could not update LDAP address book (ldap error)");
                e.printStackTrace();
            }
        }
        
        //Job scheduler starten, und einlesen der jobs aus dem xml file (fbreske)
        SchedulingManager.getInstance().readXMLJobs(new File(this.getServletContext().getRealPath("/WEB-INF/events.xml")));
        
        // laden der aktuellen reminder mails jobs (fbreske)
        Calendar startDate = GregorianCalendar.getInstance();
        Calendar endDate = GregorianCalendar.getInstance();
        startDate.set(startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),startDate.get(Calendar.DATE),0,0);
        endDate.set(startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),startDate.get(Calendar.DATE)+1,0,0);
        SchedulingManager.getInstance().loadReminderJobsfromDB(startDate.getTime(), endDate.getTime());

        // load style properties
        Properties tagSettings = new Properties();
        try {
            tagSettings.load(new FileInputStream(new File(this.getServletContext().getRealPath("/WEB-INF/properties/tagSettings.properties"))));
        } catch(IOException e) {
            log.error("could not load tagSettings.properties", e);
        }
        this.getServletContext().setAttribute(Globals.TAG_SETTINGS_KEY, tagSettings);
        
        // load available locales
        MessageResources resources = (MessageResources)this.getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
        LocaleUtility.getInstance().load(resources.getConfig());   
    }

}
