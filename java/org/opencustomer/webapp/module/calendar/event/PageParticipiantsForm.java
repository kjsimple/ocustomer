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

package org.opencustomer.webapp.module.calendar.event;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.ImageButtonBean;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.RequestUtility;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageParticipiantsForm extends EditPageForm
{
    private static final long serialVersionUID = 3258690996551430963L;

    private final static Logger log = Logger.getLogger(PageParticipiantsForm.class);

    private int doJumpPerson;

    private int doRemovePerson;
    
    private ImageButtonBean doAddPerson = new ImageButtonBean();

    private int doJumpUser;

    private int doRemoveUser;
    
    private ImageButtonBean doAddUser = new ImageButtonBean();
    
    private List<String> invitationStatus = new ArrayList<String>();
    
    private ImageButtonBean doStatusNew = new ImageButtonBean();

    private ImageButtonBean doStatusAccept = new ImageButtonBean();
    
    private ImageButtonBean doStatusReject = new ImageButtonBean();

    private ImageButtonBean doStatusDelete = new ImageButtonBean();

    private String unknownParticipiants;
    
    @Override
    public void validate(ActionMapping mapping, ActionMessages error, HttpServletRequest request)
    {
        doRemovePerson = RequestUtility.parseParamId(request, "doRemovePerson_");
        doRemoveUser = RequestUtility.parseParamId(request, "doRemoveUser_");
        
        unknownParticipiants = FormUtility.adjustParameter(unknownParticipiants, 255);
    }
    
    @Override
    protected void analyzeRequest(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        doJumpUser   = RequestUtility.parseParamId(request, "doJumpUser_");
        doJumpPerson   = RequestUtility.parseParamId(request, "doJumpPerson_");
    }

    public final ImageButtonBean getDoAddPerson()
    {
        return doAddPerson;
    }

    public final void setDoAddPerson(ImageButtonBean doAddPerson)
    {
        this.doAddPerson = doAddPerson;
    }

    public final int getDoRemovePerson()
    {
        return doRemovePerson;
    }

    public final void setDoRemovePerson(int doRemovePerson)
    {
        this.doRemovePerson = doRemovePerson;
    }

    public final int getDoJumpPerson()
    {
        return doJumpPerson;
    }

    public final void setDoJumpPerson(int doJumpPerson)
    {
        this.doJumpPerson = doJumpPerson;
    }

    public final ImageButtonBean getDoAddUser()
    {
        return doAddUser;
    }

    public final void setDoAddUser(ImageButtonBean doAddUser)
    {
        this.doAddUser = doAddUser;
    }

    public final int getDoRemoveUser()
    {
        return doRemoveUser;
    }

    public final void setDoRemoveUser(int doRemoveUser)
    {
        this.doRemoveUser = doRemoveUser;
    }

    public final int getDoJumpUser()
    {
        return doJumpUser;
    }

    public final void setDoJumpUser(int doJumpUser)
    {
        this.doJumpUser = doJumpUser;
    }

    public final String getInvitationStatus(int index)
    {
        return invitationStatus.get(index);
    }

    public final void setInvitationStatus(int index, String value)
    {
        if(index>invitationStatus.size()) {
            invitationStatus.add(value);
        }
        else {    
            this.invitationStatus.add(index, value);
        }
    }
    
    public final List<String> getInvitationStatus() 
    {
        return invitationStatus;
    }

    public final ImageButtonBean getDoStatusAccept()
    {
        return doStatusAccept;
    }

    public final void setDoStatusAccept(ImageButtonBean doStatusAccept)
    {
        this.doStatusAccept = doStatusAccept;
    }

    public final ImageButtonBean getDoStatusNew()
    {
        return doStatusNew;
    }

    public final void setDoStatusNew(ImageButtonBean doStatusNew)
    {
        this.doStatusNew = doStatusNew;
    }

    public final ImageButtonBean getDoStatusReject()
    {
        return doStatusReject;
    }

    public final void setDoStatusReject(ImageButtonBean doStatusReject)
    {
        this.doStatusReject = doStatusReject;
    }

    public ImageButtonBean getDoStatusDelete()
    {
        return doStatusDelete;
    }

    public void setDoStatusDelete(ImageButtonBean doStatusDelete)
    {
        this.doStatusDelete = doStatusDelete;
    }

    public String getUnknownParticipiants()
    {
        return unknownParticipiants;
    }

    public void setUnknownParticipiants(String unknownParticipiants)
    {
        this.unknownParticipiants = unknownParticipiants;
    }
    
}
