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

package org.opencustomer.webapp.module.system.management.user.add;

import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.action.ChooseForm;

public final class AddRoleForm extends ChooseForm
{
    private static final long serialVersionUID = 3689345529142458675L;
    
    private String name;

    @Override
    protected void adjustInput()
    {
        name = FormUtility.adjustParameter(name);
    }
    
    @Override
    public void reset()
    {
        name = null;
    }
    
    public final String getName()
    {
        return name;
    }

    public final void setName(String name)
    {
        this.name = name;
    }
}
