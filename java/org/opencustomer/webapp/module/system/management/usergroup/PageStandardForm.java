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

package org.opencustomer.webapp.module.system.management.usergroup;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.framework.webapp.util.RequestUtility;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageStandardForm extends EditPageForm
{
    private static final long serialVersionUID = 3258690996551430965L;

    private final static Logger log = Logger.getLogger(PageStandardForm.class);

    private String name;
    private boolean admin;
    private boolean defaultUsergroup;
    
    private int doJumpUser;
    
    private int doJumpRole;
    
    @Override
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        name = FormUtility.adjustParameter(name, 255);

        if (name == null)
            errors.add("name", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.role.name")));
    }

    @Override
    protected void analyzeRequest(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        doJumpUser = RequestUtility.parseParamId(request, "doJumpUser_");
        doJumpRole = RequestUtility.parseParamId(request, "doJumpRole_");
    }
    
    public final String getName()
    {
        return name;
    }

    public final void setName(String name)
    {
        this.name = name;
    }

    public final int getDoJumpUser()
    {
        return doJumpUser;
    }

    public final void setDoJumpUser(int doJumpUser)
    {
        this.doJumpUser = doJumpUser;
    }

    public boolean isAdmin()
    {
        return admin;
    }

    public void setAdmin(boolean admin)
    {
        this.admin = admin;
    }

    public boolean isDefaultUsergroup()
    {
        return defaultUsergroup;
    }

    public void setDefaultUsergroup(boolean defaultUsergroup)
    {
        this.defaultUsergroup = defaultUsergroup;
    }

    public int getDoJumpRole()
    {
        return doJumpRole;
    }

    public void setDoJumpRole(int doJumpRole)
    {
        this.doJumpRole = doJumpRole;
    }
}
