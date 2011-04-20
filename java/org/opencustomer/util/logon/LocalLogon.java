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
 *                 Felix Breske <felix.breske@bader-jene.de
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.util.logon;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.dao.system.UserLoginLogDAO;
import org.opencustomer.db.vo.system.UserLoginLogVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.util.SignatureUtility;
import org.opencustomer.framework.util.validator.IpAddressPatternValidator;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.webapp.auth.Authenticator;
import org.opencustomer.webapp.auth.Right;

public class LocalLogon extends Logon
{
    private static final Logger log = Logger.getLogger(LocalLogon.class); 
    
    @Override
    public UserVO validate(String userName, String password, String ipAddress, Type type, ActionMessages errors) {
        
        // Suche den Benutzer für die Anmeldung
        UserVO user = findUser(userName);
        
        validate(type, user, password, ipAddress, errors);

        // save changes
        if (user != null && errors.size("missingRights") == 0  && errors.size("invalidIpAddress") == 0) {
            UserLoginLogVO loginLog = createLoginLogEntry(user, type, ipAddress, errors);
            
            try {
                HibernateContext.beginTransaction();

                if(loginLog != null)
                    new UserLoginLogDAO().insert(loginLog);
                
                new UserDAO().update(user);
                
                HibernateContext.commitTransaction();
            } catch (HibernateException e) {
                HibernateContext.rollbackTransaction();
                
                log.error("could not save user", e);
            }
        }
        
        return user;
    }
    
    private UserLoginLogVO createLoginLogEntry(UserVO user, Type type, String ipAddress, ActionMessages errors) {
        UserLoginLogVO loginLog = null;

        UserLoginLogVO.Result result = null;
        if(errors.isEmpty())
            result = UserLoginLogVO.Result.VALID;
        else { 
            if(errors.size("maxFailedLoginsReached") > 0)
                result = UserLoginLogVO.Result.VALID_REJECTED;
            else
                result = UserLoginLogVO.Result.INVALID;
        }

        // TODO: is it good that every webservice and webdav access will be logged? can we use cookies here?
        
        loginLog = new UserLoginLogVO();
        loginLog.setResult(result);
        loginLog.setRemoteHost(ipAddress);
        loginLog.setLoginDate(new Date());
        loginLog.setUser(user);
        if(Type.WEBAPP.equals(type))
            loginLog.setType(UserLoginLogVO.Type.WEBAPP);
        else if(Type.WEBDAV.equals(type))
            loginLog.setType(UserLoginLogVO.Type.WEBDAV);
        else if(Type.WEBSERVICE.equals(type))
            loginLog.setType(UserLoginLogVO.Type.WEBSERVICE);
        
        return loginLog;
    }
    
    private void validate(Type type, UserVO user, String password, String ipAddress, ActionMessages errors) {
        
        // Überprüfe die Informationen
        if (user == null) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.common.login.error.invalidUserNamePassword"));
        } else {
            
            // validate rights if not web application
            Authenticator auth = new Authenticator(user);
            if((Type.WEBDAV.equals(type) && !auth.isValid(Right.EXTERN_WEBDAV_READ)) 
                    || (Type.WEBSERVICE.equals(type) && !auth.isValid(Right.EXTERN_WEBSERVICE_READ))) {
                if(log.isDebugEnabled())
                    log.debug("user has no access to webdav or webservice");
                errors.add("missingRights", new ActionMessage("default.error.invalidRights"));
            } else if (ipAddress != null && !IpAddressPatternValidator.getInstance().match(user.getProfile().getIpPattern(), ipAddress)) {
                if(log.isDebugEnabled())
                    log.debug("user has no access from ip: "+ipAddress);
                errors.add("invalidIpAddress", new ActionMessage("module.common.login.error.invalidIpAddress", ipAddress));
            } else {
                long failedLogins = new UserLoginLogDAO().countInvalidLoginsForUser(user);

                if (user.getProfile().isProfileLocked()) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.common.login.error.userLocked"));
                } else if (failedLogins >= SystemConfiguration.getInstance().getIntValue(SystemConfiguration.Key.MAX_FAILED_LOGINS)
                        && user.getProfile().getTimeLock() > 0 
                        && validatePassword(user, password)) {
                    UserLoginLogVO lastInvalidLogin = new UserLoginLogDAO().getLastInvalidLoginForUser(user);
                    if(lastInvalidLogin != null) {
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.setTime(lastInvalidLogin.getLoginDate());
                        cal.add(Calendar.MINUTE, user.getProfile().getTimeLock());
                        if (!new Date().after(cal.getTime()))
                            errors.add("maxFailedLoginsReached", new ActionMessage("module.common.login.error.maxFailedLoginsReached", failedLogins, user.getProfile().getTimeLock()));
                    }
                } else if (!validatePassword(user, password)) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.common.login.error.invalidUserNamePassword"));
                } 
            }
        }
    }
        
    protected boolean validatePassword(UserVO user, String clientPassword) {
            return SignatureUtility.getInstance().isSignatureValid(user.getPassword(), clientPassword);
    }
    
    protected UserVO findUser(String userName)
    {
        UserVO user = null;
        try {
            user = new UserDAO().getByUserName(userName);
            if (user != null)
                Hibernate.initialize(user.getPerson());
        } catch (HibernateException e) {
            log.error("problems finding user user", e);
        }
        return user;
    }
}
