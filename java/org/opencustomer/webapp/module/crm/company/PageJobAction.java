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

package org.opencustomer.webapp.module.crm.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.dao.crm.custom.JobListDAO;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.ScrollBean;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.util.EnumUtility;
import org.opencustomer.framework.webapp.action.ListAction;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.util.configuration.UserConfiguration;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditPageListAction;

public class PageJobAction extends EditPageListAction<PageJobForm>
{
    private static Logger log = Logger.getLogger(PageJobAction.class);

    @Override
    protected void search(PageJobForm form, ActionMessages errors, HttpServletRequest request)
    {
        UserVO user       = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        CompanyVO company = (CompanyVO) getPanel().getEntity();
        UserConfiguration conf = (UserConfiguration)request.getSession().getAttribute(Globals.CONFIGURATION_KEY);

        List list = null;
        long count = 0;

        Sort sort = ListAction.extractSort(getPanel(), "job_sort", form.getSort(), new Sort(JobListDAO.SORT_DUEDATE, true));
        Page page = new Page(conf.getIntValue(UserConfiguration.Key.LIST_NUMBER_ROWS), form.getPage());

        String paramSubject = form.getSubject();
        JobVO.Status[] paramStatus = null;
        if(form.getStatus() != null)
            paramStatus = new JobVO.Status[]{EnumUtility.valueOf(JobVO.Status.class, form.getStatus())};

        Date paramDateStart = null;
        Date paramDateEnd = null;
        SimpleDateFormat sdf = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.date"));
        try
        {
            if (form.getDateStart() != null)
                paramDateStart = sdf.parse(form.getDateStart());
            if (form.getDateEnd() != null)
                paramDateEnd = DateUtility.getEndOfDay(sdf.parse(form.getDateEnd()));
        }
        catch (ParseException e)
        {
            log.error("bad form validation", e);
        }

        JobListDAO dao = new JobListDAO();

        count = dao.countList(company, null, null, paramSubject, paramStatus, null, paramDateStart, paramDateEnd, true, null, user);
        if (count < page.getFirstEntry())
            page.setPage(1);

        if (count > 0) {
            list = dao.getList(company, null, null, paramSubject, paramStatus, null, paramDateStart, paramDateEnd, true, null, sort, page, user);
        } else {
            list = new ArrayList();
        }
        
        ScrollBean scroll = new ScrollBean();
        scroll.setCount(count);
        scroll.setPage(page);

        getPanel().setAttribute("jobListScroll", scroll);
        getPanel().setAttribute("jobList", list);
    }
}
