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
 * Copyright (C) 2006 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.webapp.util.listener;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.webapp.Globals;

public class SessionMonitor implements Serializable, HttpSessionListener, HttpSessionAttributeListener {

    private static final long serialVersionUID = -5131370911549262532L;

    private final static Logger log = Logger.getLogger(SessionMonitor.class);
    
    private static Set<String> sessionIds = new HashSet<String>();
    
    private static Set<HttpSession> sessions = new HashSet<HttpSession>();
    
    public SessionMonitor() {
    }

    public void sessionCreated(HttpSessionEvent event) {
        sessionIds.add(event.getSession().getId());
        
        if(log.isDebugEnabled())
            log.debug("create session with id '"+event.getSession().getId()+"' (count: "+sessionIds.size()+")");
    }
    
    public void sessionDestroyed(HttpSessionEvent event) {
        sessionIds.remove(event.getSession().getId());

        if(log.isDebugEnabled())
            log.debug("destroy session with id '"+event.getSession().getId()+"' (count: "+sessionIds.size()+")");
    }
    
    public void attributeAdded(HttpSessionBindingEvent event) {        
        if(Globals.USER_KEY.equals(event.getName())) {
            if(log.isDebugEnabled())
                log.debug("user logged in: "+((UserVO)event.getValue()).getUserName());
            
            if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.MULTIPLE_LOGINS)) {
                UserVO user = (UserVO)event.getSession().getAttribute(Globals.USER_KEY);
                
                for(HttpSession session : getSessionsByUser(user.getUserName())) {
                    session.invalidate();
                }
            }
            
            sessions.add(event.getSession());
            
            if(log.isDebugEnabled())
                log.debug("actual sessions: "+sessions.size());
        }
    }
    
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if(Globals.USER_KEY.equals(event.getName())) {
            if(log.isDebugEnabled())
                log.debug("user logged off: "+((UserVO)event.getValue()).getUserName());
            
            sessions.remove(event.getSession());
        }
    }
    
    public void attributeReplaced(HttpSessionBindingEvent event) {
    }
    
    /**
     * The session count over all sessions on the server (logged in or not).
     * @return the session count.
     */
    public int getSessionCount() {
        return sessionIds.size();
    }

    /** 
     * The sessions of all logged in users.
     * @return the sessions of all logged in users.
     */
    public Set<HttpSession> getUserSessions() {
        return sessions;
    }
    
    private Set<HttpSession> getSessionsByUser(String userName) {
        Set<HttpSession> userSessions = new HashSet<HttpSession>();
        
        for(HttpSession session : sessions) {
            UserVO user = (UserVO)session.getAttribute(Globals.USER_KEY);
            if(user.getUserName().equals(userName)) {
                userSessions.add(session);
            }
        }
        
        if(log.isDebugEnabled()) {
            log.debug("found "+userSessions.size()+" user session for user: "+userName);
        }
        
        return userSessions;
    }
    
}
