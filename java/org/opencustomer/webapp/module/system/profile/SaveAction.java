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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.util.LocaleUtility;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditSaveAction;

public class SaveAction extends EditSaveAction
{
    private static Logger log = Logger.getLogger(SaveAction.class);

    @Override
    protected void saveEntity(EditPanel panel, ActionMessages errors, HttpServletRequest request)
    {
        UserVO user = (UserVO)panel.getEntity();

        try
        {
            new UserDAO().update(user);

            request.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY, user.getLocale());
            request.getSession().setAttribute(Globals.USER_KEY, user);
            
            panel.setTitle(MessageUtil.message(request, "module.system.user.profile.default.title"));
            
            List<LocaleUtility.LocaleBean> locales = new ArrayList<LocaleUtility.LocaleBean>();
            for(Locale locale : LocaleUtility.getInstance().getLocales()) {
                locales.add(new LocaleUtility.LocaleBean(locale, MessageUtil.message(request, "locale."+locale)));
            }            
            Collections.sort(locales);
            panel.setAttribute("locales", locales);
        }
        catch (HibernateException e)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.rollback"));
        }

    }
}
