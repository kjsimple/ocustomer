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

package org.opencustomer.webapp.module.crm.person;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageAddressForm extends EditPageForm
{
    private static final long serialVersionUID = 3763091947321111089L;

    private String mainAddressName;

    private String mainAddressStreet;

    private String mainAddressZip;

    private String mainAddressCity;

    private String postalAddressName;

    private String postalAddressStreet;

    private String postalAddressPostbox;

    private String postalAddressZip;

    private String postalAddressCity;

    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        mainAddressName   = FormUtility.adjustParameter(mainAddressName, 255);
        mainAddressStreet = FormUtility.adjustParameter(mainAddressStreet, 255);
        mainAddressZip    = FormUtility.adjustParameter(mainAddressZip, 5);
        mainAddressCity   = FormUtility.adjustParameter(mainAddressCity, 255);

        postalAddressName    = FormUtility.adjustParameter(postalAddressName, 255);
        postalAddressStreet  = FormUtility.adjustParameter(postalAddressStreet, 255);
        postalAddressPostbox = FormUtility.adjustParameter(postalAddressPostbox, 255);
        postalAddressZip     = FormUtility.adjustParameter(postalAddressZip, 5);
        postalAddressCity    = FormUtility.adjustParameter(postalAddressCity, 255);

        if(mainAddressName != null || mainAddressStreet != null || mainAddressZip != null || mainAddressCity != null) {
            if(mainAddressName == null) 
                errors.add("mainAddressName", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.address.name")));
            
            if(mainAddressStreet == null) 
                errors.add("mainAddressStreet", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.address.street")));
            
            if(mainAddressZip == null) 
                errors.add("mainAddressZip", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.address.zip")));
            else if (!mainAddressZip.matches("\\d{5}"))
                errors.add("mainAddressZip", new ActionMessage("default.error.invalidFormat.undefined", MessageUtil.message(request, "entity.crm.address.zip")));
            
            if(mainAddressCity == null) 
                errors.add("mainAddressCity", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.address.city")));
        }

        if(postalAddressName != null || postalAddressStreet != null || postalAddressPostbox != null || postalAddressZip != null || postalAddressCity != null) {
            if(postalAddressName == null) 
                errors.add("postalAddressName", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.address.name")));
            
            if(postalAddressStreet == null && postalAddressPostbox == null) 
                errors.add("postalAddressStreet", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.address.street")));
            
            if (postalAddressStreet != null && postalAddressPostbox != null)
                errors.add("postalAddress", new ActionMessage("module.crm.company.error.streetAndPostboxSelected", MessageUtil.message(request, "entity.crm.address.type.postalAddress")));

            if(postalAddressZip == null) 
                errors.add("postalAddressZip", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.address.zip")));
            else if (!postalAddressZip.matches("\\d{5}"))
                errors.add("postalAddressZip", new ActionMessage("default.error.invalidFormat.undefined", MessageUtil.message(request, "entity.crm.address.zip")));

            if(postalAddressCity == null) 
                errors.add("postalAddressCity", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.crm.address.city")));
        }
    }

    public final String getMainAddressCity()
    {
        return mainAddressCity;
    }

    public final void setMainAddressCity(String mainAddressCity)
    {
        this.mainAddressCity = mainAddressCity;
    }

    public final String getMainAddressName()
    {
        return mainAddressName;
    }

    public final void setMainAddressName(String mainAddressName)
    {
        this.mainAddressName = mainAddressName;
    }

    public final String getMainAddressStreet()
    {
        return mainAddressStreet;
    }

    public final void setMainAddressStreet(String mainAddressStreet)
    {
        this.mainAddressStreet = mainAddressStreet;
    }

    public final String getMainAddressZip()
    {
        return mainAddressZip;
    }

    public final void setMainAddressZip(String mainAddressZip)
    {
        this.mainAddressZip = mainAddressZip;
    }

    public final String getPostalAddressCity()
    {
        return postalAddressCity;
    }

    public final void setPostalAddressCity(String postalAddressCity)
    {
        this.postalAddressCity = postalAddressCity;
    }

    public final String getPostalAddressName()
    {
        return postalAddressName;
    }

    public final void setPostalAddressName(String postalAddressName)
    {
        this.postalAddressName = postalAddressName;
    }

    public final String getPostalAddressPostbox()
    {
        return postalAddressPostbox;
    }

    public final void setPostalAddressPostbox(String postalAddressPostbox)
    {
        this.postalAddressPostbox = postalAddressPostbox;
    }

    public final String getPostalAddressStreet()
    {
        return postalAddressStreet;
    }

    public final void setPostalAddressStreet(String postalAddressStreet)
    {
        this.postalAddressStreet = postalAddressStreet;
    }

    public final String getPostalAddressZip()
    {
        return postalAddressZip;
    }

    public final void setPostalAddressZip(String postalAddressZip)
    {
        this.postalAddressZip = postalAddressZip;
    }
    
}
