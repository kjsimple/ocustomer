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

package org.opencustomer.webapp.module.system.management.role;

import org.apache.log4j.Logger;
import org.opencustomer.framework.util.FormUtility;

public final class ListForm extends org.opencustomer.framework.webapp.action.ListForm
{
    private final static Logger log = Logger.getLogger(ListForm.class);

    private static final long serialVersionUID = 3258135743162364474L;

    private String name;

    private String admin;
    
    @Override
    protected void adjustInput()
    {
        name  = FormUtility.adjustParameter(name);
        admin = FormUtility.adjustParameter(admin);
    }

    @Override
    public void reset()
    {
        name  = null;
        admin = null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAdmin()
    {
        return admin;
    }

    public void setAdmin(String admin)
    {
        this.admin = admin;
    }

}
