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
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.webapp.auth;

import java.util.Hashtable;

public enum Right {
    
    CRM_PERSONS_READ(1, "crm.persons.READ"),
    CRM_PERSONS_WRITE(2, "crm.persons.WRITE"),
    CRM_COMPANIES_READ(3, "crm.companies.READ"),
    CRM_COMPANIES_WRITE(4, "crm.companies.WRITE"),
    CRM_CONTACTS_READ(5, "crm.contacts.READ"),
    CRM_CONTACTS_WRITE(6, "crm.contacts.WRITE"),
    ADMINISTRATION_USERPROFILE_READ(7, "administration.userProfile.READ"),
    ADMINISTRATION_USERPROFILE_WRITE(8, "administration.userProfile.WRITE"),
    ADMINISTRATION_USERMANAGEMENT_READ(9, "administration.userManagement.READ"),
    ADMINISTRATION_USERMANAGEMENT_WRITE(10, "administration.userManagement.WRITE"),
    CALENDAR_CALENDAR_READ(11, "calendar.calendar.READ"),
    CALENDAR_CALENDAR_WRITE(12, "calendar.calendar.WRITE"),
    ADMINISTRATION_ROLE_READ(13, "administration.role.READ"),
    ADMINISTRATION_ROLE_WRITE(14, "administration.role.WRITE"),
    ADMINISTRATION_USERGROUP_READ(15, "administration.usergroup.READ"),
    ADMINISTRATION_USERGROUP_WRITE(16, "administration.usergroup.WRITE"),
    ADMINISTRATION_USERMANAGEMENT_SESSIONMONITOR(17, "administration.userManagement.SESSION_MONITOR"),
    CALENDAR_CALENDAR_CHOOSE(18, "calendar.calendar.CHOOSE"),
    EXTERN_WEBDAV_READ(19, "extern.webdav.READ"),
    EXTERN_WEBDAV_WRITE(20, "extern.webdav.WRITE"),
    CRM_JOBS_READ(21, "crm.jobs.READ"),
    CRM_JOBS_WRITE(22, "crm.jobs.WRITE"),
    CRM_JOBS_ASSIGNUSER(23, "crm.jobs.ASSIGN_USER"),
    EXTERN_WEBSERVICE_READ(24, "extern.webservice.READ"),
    EXTERN_WEBSERVICE_WRITE(25, "extern.webservice.WRITE"),
    ADMINISTRATION_CONFIGURATION_READ(26, "administration.configuration.READ"),    
    ADMINISTRATION_CONFIGURATION_WRITE(27, "administration.configuration.WRITE"),
    ADMINISTRATION_LIST_READ(28, "administration.list.READ"),    
    ADMINISTRATION_LIST_WRITE(29, "administration.list.WRITE"),    
    ADMINISTRATION_LIST_GLOBALLIST(30, "administration.list.GLOBAL_LIST"),    
    CRM_PERSONS_DOWNLOAD(31, "crm.persons.DOWNLOAD"),    
    CRM_COMPANIES_DOWNLOAD(32, "crm.companies.DOWNLOAD");    

    //private static Hashtable<String, Right> rights;

    private String label;

    private int id;

    private Right(int id, String label)
    {
        this.label = label;
        this.id = id;
/*
        if (Right.rights == null)
            Right.rights = new Hashtable<String, Right>();

        Right.rights.put(label, this);
        */
    }

    public String getLabel()
    {
        return label;
    }

    public static Right parseRight(String label) throws IllegalArgumentException
    {
        String s = label.replaceAll("\\.", "_").toUpperCase();
        if ("ADMINISTRATION_USERMANAGEMENT_SESSION_MONITOR".equals(s)) {
            s = "ADMINISTRATION_USERMANAGEMENT_SESSIONMONITOR";
        }
        return Right.valueOf(s);
    /*
        Right right = rights.get(label);

        if (right == null)
            throw new IllegalArgumentException("found no valid right for label: '" + label + "'");

        return right;
        */
    }
}
