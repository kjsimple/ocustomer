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

package org.opencustomer.webapp.module.calendar.event;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.webapp.panel.EditPanel;

public class PageSystemAction extends org.opencustomer.webapp.module.generic.PageSystemAction<PageSystemForm> {
    private static Logger log = Logger.getLogger(PageSystemAction.class);

    @Override
    protected EntityAccess getEntityAccess(HttpServletRequest request) {
        EventVO event = (EventVO)getPanel().getEntity();
        CalendarVO calendar = (CalendarVO)getPanel().getAttribute("calendar");
        
        for(EventCalendarVO eventCalendar : event.getEventCalendars()) {
            if(calendar.equals(eventCalendar.getCalendar())) {
                if(log.isDebugEnabled())
                    log.debug("use for entity access: "+eventCalendar);
                
                return eventCalendar;
            }
        }
        
        return  null;
    }
    
    @Override
    protected boolean isEditable(EditPanel panel, UserVO user, EntityAccess ea, HttpServletRequest request) {
        EventVO event       = (EventVO)panel.getEntity();
        CalendarVO calendar = (CalendarVO)panel.getAttribute("calendar");

        boolean participientCalendar = EntityAccessUtility.isAccessGranted(user, ea, EntityAccess.Access.WRITE_SYSTEM);
        boolean participientEvent = EntityAccessUtility.isAccessGranted(user, calendar, EntityAccess.Access.WRITE_SYSTEM);
        
        if(log.isDebugEnabled())
            log.debug("editable(content) participientCalendar="+participientCalendar+" and participientEvent="+participientEvent);
            
        return participientCalendar && participientEvent;
    }
}
