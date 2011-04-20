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

package org.opencustomer.webapp.module.crm.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.dao.crm.ContactDAO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.ScrollBean;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.util.configuration.UserConfiguration;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditPageListAction;

public class PageContactAction extends EditPageListAction<PageContactForm>
{
    private static Logger log = Logger.getLogger(PageContactAction.class);

    @Override
    protected void search(PageContactForm form, ActionMessages errors, HttpServletRequest request)
    {
        UserVO user       = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        CompanyVO company = (CompanyVO) getPanel().getEntity();
        UserConfiguration conf = (UserConfiguration)request.getSession().getAttribute(Globals.CONFIGURATION_KEY);

        List list = null;
        long count = 0;

        Sort sort = null;
        if (form.getSort() == null)
            sort = new Sort(ContactDAO.SORT_CONTACTTIMESTAMP, false);
        else
            sort = Sort.parseParam(form.getSort());

        Page page = new Page(conf.getIntValue(UserConfiguration.Key.LIST_NUMBER_ROWS), form.getPage());

        String paramSubject = form.getSubject();
        String paramBoundType = form.getBoundType();
        String paramContactType = form.getContactType();
        String paramName = form.getName();

        Date paramContactTimestampStart = null;
        Date paramContactTimestampEnd = null;
        String format = MessageUtil.message(request, "default.format.input.date");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            if (form.getContactTimestampStart() != null)
                paramContactTimestampStart = sdf.parse(form.getContactTimestampStart());
            if (form.getContactTimestampEnd() != null)
                paramContactTimestampEnd = DateUtility.getEndOfDay(sdf.parse(form.getContactTimestampEnd()));
        } catch (ParseException e) {
            log.error("bad form validation", e);
        }

        ContactDAO dao = new ContactDAO();

        count = dao.countListForCompany(company.getId(), paramSubject, paramBoundType, paramContactType, paramContactTimestampStart, paramContactTimestampEnd, paramName, user);
        if (count < page.getFirstEntry())
            page.setPage(1);

        if (count > 0)
            list = dao.getListForCompany(company.getId(), paramSubject, paramBoundType, paramContactType, paramContactTimestampStart, paramContactTimestampEnd, paramName, sort, page, user);
        else
            list = new ArrayList();

        ScrollBean scroll = new ScrollBean();
        scroll.setCount(count);
        scroll.setPage(page);

        request.setAttribute("crm.company.edit.contactListScroll", scroll);
        request.setAttribute("crm.company.edit.contactList", list);
    }
}
