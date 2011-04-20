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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.ImageButtonBean;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.util.validator.EmailAddressValidator;
import org.opencustomer.framework.util.validator.URLValidator;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageStandardForm extends EditPageForm {
    private static final long serialVersionUID = 3904964165969197368L;

    private String companyName;

    private String email;

    private String phone;

    private String fax;

    private String url;

    private String comment;

    private int sectorId;

    private int legalFormId;

    private int companyStateId;

    private int companyTypeId;

    private int categoryId;
    
    private int ratingId;

    private String staffCount;
    
    private ImageButtonBean doAddUser = new ImageButtonBean();
    
    private ImageButtonBean doRemoveUser = new ImageButtonBean();

    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request) {
        companyName = FormUtility.adjustParameter(companyName, 255);
        email       = FormUtility.adjustParameter(email, 255);
        phone       = FormUtility.adjustParameter(phone, 50);
        fax         = FormUtility.adjustParameter(fax, 50);
        url         = FormUtility.adjustParameter(url, 50);
        comment     = FormUtility.adjustParameter(comment);
        staffCount  = FormUtility.adjustParameter(staffCount, 5);

        if (staffCount != null && !staffCount.matches("\\d+")) {
            errors.add("staffCount", new ActionMessage("default.error.invalidFormat.unsignedInteger", MessageUtil.message(request, "entity.crm.company.staffCount")));
        }
        
        if(email !=  null && !EmailAddressValidator.getInstance().validate(email)) {
            errors.add("email", new ActionMessage("default.error.invalidFormat.email", MessageUtil.message(request, "entity.crm.company.email")));
        }

        if(url !=  null && !URLValidator.getInstance().validate(url)) {
            errors.add("url", new ActionMessage("default.error.invalidFormat.url", MessageUtil.message(request, "entity.crm.company.url")));
        }
    }

    public final String getComment() {
        return comment;
    }

    public final void setComment(String comment) {
        this.comment = comment;
    }

    public final String getEmail() {
        return email;
    }

    public final void setEmail(String email) {
        this.email = email;
    }

    public final String getFax() {
        return fax;
    }

    public final void setFax(String fax) {
        this.fax = fax;
    }

    public final String getPhone() {
        return phone;
    }

    public final void setPhone(String phone) {
        this.phone = phone;
    }

    public final String getCompanyName() {
        return companyName;
    }

    public final void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public final String getUrl() {
        return url;
    }

    public final void setUrl(String url) {
        this.url = url;
    }

    public final int getLegalFormId() {
        return legalFormId;
    }

    public final void setLegalFormId(int legalFormId) {
        this.legalFormId = legalFormId;
    }

    public final int getSectorId() {
        return sectorId;
    }

    public final void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    /**
     * @return Returns the companyStateId.
     */
    public final int getCompanyStateId() {
        return companyStateId;
    }

    /**
     * @param companyStateId The companyStateId to set.
     */
    public final void setCompanyStateId(int companyStateId) {
        this.companyStateId = companyStateId;
    }

    /**
     * @return Returns the companyTypeId.
     */
    public final int getCompanyTypeId() {
        return companyTypeId;
    }

    /**
     * @param companyTypeId The companyTypeId to set.
     */
    public final void setCompanyTypeId(int companyTypeId) {
        this.companyTypeId = companyTypeId;
    }

    /**
     * @return Returns the staffCount.
     */
    public final String getStaffCount() {
        return staffCount;
    }

    /**
     * @param staffCount The staffCount to set.
     */
    public final void setStaffCount(String staffCount) {
        this.staffCount = staffCount;
    }

    public final int getCategoryId() {
        return categoryId;
    }

    public final void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public final int getRatingId() {
        return ratingId;
    }

    public final void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public ImageButtonBean getDoAddUser() {
        return doAddUser;
    }

    public void setDoAddUser(ImageButtonBean doAddUser) {
        this.doAddUser = doAddUser;
    }

    public ImageButtonBean getDoRemoveUser() {
        return doRemoveUser;
    }

    public void setDoRemoveUser(ImageButtonBean doRemoveUser) {
        this.doRemoveUser = doRemoveUser;
    }

}
