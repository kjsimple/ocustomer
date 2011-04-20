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

package org.opencustomer.webapp.module.system.management.user;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;

public final class ListForm extends org.opencustomer.framework.webapp.action.ListForm
{
    private final static Logger log = Logger.getLogger(ListForm.class);

    private static final long serialVersionUID = 3258135743162364472L;

    private String userName;

    private String lastLoginStart;

    private String lastLoginEnd;

    private String locked;

    @Override
    protected void adjustInput()
    {
        userName = FormUtility.adjustParameter(userName);
        locked = FormUtility.adjustParameter(locked);
        lastLoginStart = FormUtility.adjustParameter(lastLoginStart);
        lastLoginEnd = FormUtility.adjustParameter(lastLoginEnd);
    }

    @Override
    public void validate(ActionMessages errors, HttpServletRequest request)
    {
        String format = MessageUtil.message(request, "default.format.input.date");

        if (locked != null)
        {
            if (Boolean.TRUE.toString().equalsIgnoreCase(locked))
                locked = Boolean.TRUE.toString();
            else if (Boolean.FALSE.toString().equalsIgnoreCase(locked))
                locked = Boolean.FALSE.toString();
            else
                locked = null;
        }

        if (lastLoginStart != null)
        {
            try
            {
                lastLoginStart = DateUtility.adjustDate(format, lastLoginStart);
            }
            catch (ParseException e)
            {
                errors.add("lastLoginStart", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "module.system.user.manage.showUsers.lastLoginStart"), format));
            }
        }
        if (lastLoginEnd != null)
        {
            try
            {
                lastLoginEnd = DateUtility.adjustDate(format, lastLoginEnd);
            }
            catch (ParseException e)
            {
                errors.add("lastLoginEnd", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "module.system.user.manage.showUsers.lastLoginEnd"), format));
            }
        }

    }
    
    @Override
    public void reset()
    {
        userName = null;
        lastLoginStart = null;
        lastLoginEnd = null;
        locked = null;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getLocked()
    {
        return locked;
    }

    public void setLocked(String locked)
    {
        this.locked = locked;
    }

    public String getLastLoginEnd()
    {
        return lastLoginEnd;
    }

    public void setLastLoginEnd(String lastLoginEnd)
    {
        this.lastLoginEnd = lastLoginEnd;
    }

    public String getLastLoginStart()
    {
        return lastLoginStart;
    }

    public void setLastLoginStart(String lastLoginStart)
    {
        this.lastLoginStart = lastLoginStart;
    }

}
