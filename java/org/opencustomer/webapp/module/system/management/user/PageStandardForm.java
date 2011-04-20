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

package org.opencustomer.webapp.module.system.management.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.ImageButtonBean;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.UsergroupVO;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.util.LocaleUtility;
import org.opencustomer.framework.util.validator.IpAddressPatternValidator;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.framework.webapp.util.RequestUtility;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageStandardForm extends EditPageForm {
    private static final long serialVersionUID = 3257571706599061552L;

    private static Logger log = Logger.getLogger(PageStandardForm.class);

    private String userName;

    private String userPassword;

    private boolean locked;

    private int mainUsergroupId;

    private int doRemoveUsergroup;

    private int timeLock;

    private String ipPattern;
    
    private String locale;
    
    private String validFrom;
    
    private String validUntil;
    
    private ImageButtonBean doAddPerson = new ImageButtonBean();
    
    private ImageButtonBean doRemovePerson = new ImageButtonBean();
    
    private ImageButtonBean doAddUsergroup = new ImageButtonBean();
    
    private ImageButtonBean doAddRole = new ImageButtonBean();

    private ImageButtonBean doJumpRole = new ImageButtonBean();

    private int doJumpUsergroup;

    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request) {
        if (log.isDebugEnabled())
            log.debug("called form");

        boolean isActiveUser = false;
        if(getPanel().getAttribute("isActiveUser") != null)
            isActiveUser = (Boolean)getPanel().getAttribute("isActiveUser"); 
        boolean isLdapUser = (Boolean)getPanel().getAttribute("isLdapUser");
        
        userName     = FormUtility.adjustParameter(userName, 255);
        userPassword = FormUtility.adjustParameter(userPassword, 255);
        ipPattern    = FormUtility.adjustParameter(ipPattern, 255);
        validFrom    = FormUtility.adjustParameter(validFrom);
        validUntil   = FormUtility.adjustParameter(validUntil);
        
        if(!isLdapUser) {
            if (timeLock < 0)
                errors.add("timeLock", new ActionMessage("default.error.invalidFormat.unsignedInteger", MessageUtil.message(request, "entity.system.profile.timeLock")));
    
            if(ipPattern == null)
                errors.add("ipPattern", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.system.profile.ipPattern")));
            else if(!IpAddressPatternValidator.getInstance().validate(ipPattern))
                errors.add("ipPattern", new ActionMessage("default.error.invalidValue", MessageUtil.message(request, "entity.system.profile.ipPattern")));
            else if(isActiveUser && !IpAddressPatternValidator.getInstance().match(ipPattern, request.getRemoteAddr())) {
                errors.add("ipPattern", new ActionMessage("module.system.user.manage.error.ipPatternNotMatch", MessageUtil.message(request, "entity.system.profile.ipPattern"), request.getRemoteAddr()));
            }
            
            if (mainUsergroupId != 0) {
                boolean validMainUsergroup = false;
                Iterator<UsergroupVO> usergroups = ((UserVO) getPanel().getEntity()).getProfile().getUsergroups().iterator();
                while (usergroups.hasNext()) {
                    if (usergroups.next().getId() == mainUsergroupId) {
                        validMainUsergroup = true;
                        break;
                    }
                }
    
                if (!validMainUsergroup)
                    errors.add("usergroup", new ActionMessage("default.error.invalidValue", MessageUtil.message(request, "entity.system.user.usergroup")));
            }
            
            String format = MessageUtil.message(request, "default.format.input.dateTime");
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            
            Date validFromDate = null;
            if (validFrom != null)  {
                try {
                    validFrom = DateUtility.adjustDate(format, validFrom);
                    validFromDate = sdf.parse(validFrom);
                } catch (ParseException e) {
                    errors.add("validFrom", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "entity.system.profile.validFrom"), format));
                }
            }
            
            Date validUntilDate = null;
            if (validUntil != null) {
                try {
                    validUntil = DateUtility.adjustDate(format, validUntil);
                    validUntilDate = sdf.parse(validUntil);
                } catch (ParseException e) {
                    errors.add("validUntil", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "entity.system.profile.validUntil"), format));
                }
            }
            
            if(validFromDate != null && validUntilDate != null) {
                if(!validFromDate.before(validUntilDate)) {
                    errors.add("validUntil", new ActionMessage("module.system.user.manage.error.validUntilNotAfterValidFrom", MessageUtil.message(request, "entity.system.profile.validUntil"), format));
                }
            }
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

    @Override
    protected void analyzeRequest(ActionMapping mapping, ActionMessages errors, HttpServletRequest request) {
        doRemoveUsergroup = RequestUtility.parseParamId(request, "doRemoveUsergroup_");
        doJumpUsergroup   = RequestUtility.parseParamId(request, "doJumpUsergroup_");
    }
    
    public void reset() {
        userName = null;
    }

    public ImageButtonBean getDoAddPerson() {
        return doAddPerson;
    }

    public void setDoAddPerson(ImageButtonBean doAddPerson) {
        this.doAddPerson = doAddPerson;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public final ImageButtonBean getDoAddUsergroup() {
        return doAddUsergroup;
    }

    public final void setDoAddUsergroup(ImageButtonBean doAddUsergroup) {
        this.doAddUsergroup = doAddUsergroup;
    }

    public final int getDoRemoveUsergroup() {
        return doRemoveUsergroup;
    }

    public final void setDoRemoveUsergroup(int doRemoveUsergroup) {
        this.doRemoveUsergroup = doRemoveUsergroup;
    }

    public final int getMainUsergroupId() {
        return mainUsergroupId;
    }

    public final void setMainUsergroupId(int mainUsergroupId) {
        this.mainUsergroupId = mainUsergroupId;
    }

    public final ImageButtonBean getDoJumpRole() {
        return doJumpRole;
    }

    public final void setDoJumpRole(ImageButtonBean doJumpRole) {
        this.doJumpRole = doJumpRole;
    }

    public final int getDoJumpUsergroup() {
        return doJumpUsergroup;
    }

    public final void setDoJumpUsergroup(int doJumpUsergroup) {
        this.doJumpUsergroup = doJumpUsergroup;
    }

    public final int getTimeLock() {
        return timeLock;
    }

    public final void setTimeLock(int timeLock) {
        this.timeLock = timeLock;
    }

    public final String getIpPattern() {
        return ipPattern;
    }

    public final void setIpPattern(String ipPattern) {
        this.ipPattern = ipPattern;
    }

    public ImageButtonBean getDoRemovePerson() {
        return doRemovePerson;
    }

    public void setDoRemovePerson(ImageButtonBean doRemovePerson) {
        this.doRemovePerson = doRemovePerson;
    }

    public final String getLocale() {
        return locale;
    }

    public final void setLocale(String locale) {
        this.locale = locale;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public ImageButtonBean getDoAddRole() {
        return doAddRole;
    }

    public void setDoAddRole(ImageButtonBean doAddRole) {
        this.doAddRole = doAddRole;
    }

}
