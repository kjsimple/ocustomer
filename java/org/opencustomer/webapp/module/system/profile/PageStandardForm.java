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

package org.opencustomer.webapp.module.system.profile;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.util.LocaleUtility;
import org.opencustomer.framework.util.SignatureUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageStandardForm extends EditPageForm
{
    private static final long serialVersionUID = 3257571706599061552L;

    private static Logger log = Logger.getLogger(PageStandardForm.class);

    private String oldPassword;

    private String newPassword1;

    private String newPassword2;
    
    private String locale;

    @Override
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        oldPassword = FormUtility.adjustParameter(oldPassword, 20);
        newPassword1 = FormUtility.adjustParameter(newPassword1, 20);
        newPassword2 = FormUtility.adjustParameter(newPassword2, 20);

        if(oldPassword != null || newPassword1 != null || newPassword2 != null)
        {
            if (oldPassword == null)
                errors.add("oldPassword", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "module.system.user.profile.changePassword.oldPassword")));
            else
            {
                UserVO user = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
                if (!SignatureUtility.getInstance().isSignatureValid(user.getPassword(), oldPassword))
                    errors.add("oldPassword", new ActionMessage("module.system.user.profile.changePassword.error.invalidPassword", MessageUtil.message(request, "module.system.user.profile.changePassword.oldPassword")));
            }
    
            if (newPassword1 == null)
                errors.add("newPassword1", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "module.system.user.profile.changePassword.newPassword1")));
            if (newPassword2 == null)
                errors.add("newPassword2", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "module.system.user.profile.changePassword.newPassword2")));
    
            if (newPassword1 != null && newPassword2 != null && !newPassword1.equals(newPassword2))
                errors.add("newPassword2", new ActionMessage("module.system.user.profile.changePassword.error.invalidPassword2", MessageUtil.message(request, "module.system.user.profile.changePassword.newPassword2"), MessageUtil.message(request, "module.system.user.profile.changePassword.newPassword1")));
        }
        
        if(locale == null) {
            errors.add("locale", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.user.locale")));
        } else {
            Locale foundLocale = LocaleUtility.parseLocale(locale);
            if(!LocaleUtility.getInstance().getLocales().contains(foundLocale)) {
                errors.add("locale", new ActionMessage("default.error.invalidValue", MessageUtil.message(request, "entity.system.user.locale")));
            }
        }
    }

    /**
     * @return Returns the newPassword1.
     */
    public String getNewPassword1()
    {
        return newPassword1;
    }

    /**
     * @param newPassword1 The newPassword1 to set.
     */
    public void setNewPassword1(String newPassword1)
    {
        this.newPassword1 = newPassword1;
    }

    /**
     * @return Returns the newPassword2.
     */
    public String getNewPassword2()
    {
        return newPassword2;
    }

    /**
     * @param newPassword2 The newPassword2 to set.
     */
    public void setNewPassword2(String newPassword2)
    {
        this.newPassword2 = newPassword2;
    }

    /**
     * @return Returns the oldPassword.
     */
    public String getOldPassword()
    {
        return oldPassword;
    }

    /**
     * @param oldPassword The oldPassword to set.
     */
    public void setOldPassword(String oldPassword)
    {
        this.oldPassword = oldPassword;
    }

    public final String getLocale()
    {
        return locale;
    }

    public final void setLocale(String locale)
    {
        this.locale = locale;
    }
    
}
