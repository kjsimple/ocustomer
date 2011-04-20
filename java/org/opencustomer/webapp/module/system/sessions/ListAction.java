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

package org.opencustomer.webapp.module.system.sessions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.webapp.struts.Action;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.util.listener.SessionMonitor;

public class ListAction extends Action<ListForm> {

    private final static Logger log = Logger.getLogger(ListAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ListForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        SessionMonitor monitor = (SessionMonitor)request.getSession().getServletContext().getAttribute(Globals.SESSION_MONITOR_KEY);

        if(log.isDebugEnabled())
            log.debug("found "+monitor.getUserSessions().size()+" monitored sessions");
        
        ArrayList<SessionInfoBean> list = new ArrayList<SessionInfoBean>();
        for (HttpSession session : monitor.getUserSessions()) {
            SessionInfoBean bean = new SessionInfoBean();
            bean.setLoginTime(new Date(session.getCreationTime()));
            bean.setLastAccessTime(new Date(session.getLastAccessedTime()));
            bean.setInactiveTime(System.currentTimeMillis() - session.getLastAccessedTime());
            UserVO user = (UserVO) session.getAttribute(Globals.USER_KEY);
            if (user != null)
                bean.setUsername(user.getUserName());
            list.add(bean);
        }

        Collections.sort(list, new Comparator<SessionInfoBean>() {
            public int compare(SessionInfoBean bean1, SessionInfoBean bean2)
            {
                CompareToBuilder builder = new CompareToBuilder();

                builder.append(bean2.getLastAccessTime(), bean1.getLastAccessTime());
                builder.append(bean1.getUsername(), bean2.getUsername());

                return builder.toComparison();
            }
        });

        request.setAttribute("list", list);

        SessionStatisticBean statistic = new SessionStatisticBean();
        for (SessionInfoBean bean : list)
            statistic.add(bean.getInactiveTime() / 1000);

        request.setAttribute("statistic", statistic);

        return mapping.getInputForward();
    }
}
