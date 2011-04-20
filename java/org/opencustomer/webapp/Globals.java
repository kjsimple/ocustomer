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

package org.opencustomer.webapp;

/**
 * The global constants used for this webapplication.
 * 
 * @author Thomas Bader
 * @version 1.0
 */
public final class Globals
{
    /**
     * Private Constructor - instances are not necessary for this class.
     */
    private Globals()
    {

    }

    /**
     * Constant with the key of the active user.
     */
    public static final String USER_KEY = "org.opencustomer.application.USER";

    /**
     * Constant with the key of the active company. The active company may only
     * be differ from the company associated to the active user, if the users
     * company is null (This means, the active user is an administrative user).
     */
    public static final String COMPANY_KEY = "org.opencustomer.application.COMPANY";

    /**
     * Constant with the key of the authenticator of the active user.
     */
    public static final String AUTHENTICATOR_KEY = "org.opencustomer.application.AUTHENTICATOR";

    /**
     * Constant with the attribute key of the menu [session].
     */
    public static final String MENU_KEY = "org.opencustomer.application.MENU";

    /**
     * Constant with the attribute key of the menu factory [context].
     */
    public static final String MENU_FACTORY_KEY = "org.opencustomer.application.MENU_FACTORY";
    
    /**
     * Constant with the attribute key of the configuration [session].
     */
    public static final String CONFIGURATION_KEY = "org.opencustomer.application.CONFIGURATION";
    
    /**
     * Constant with the key of the session monitor [context].
     */
    public static final String SESSION_MONITOR_KEY = "org.opencustomer.application.SESSION_MONITOR";

    public static final String SETTINGS_KEY = "org.opencustomer.application.SETTINGS";
    
    public static final String JUMP_KEY = "org.opencustomer.application.JUMP";

    /**
     * Constant with the key for the tag settings [context].
     */
    public static final String TAG_SETTINGS_KEY = "org.opencustomer.application.TAG_SETTINGS";
}