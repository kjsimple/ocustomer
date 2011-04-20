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

package org.opencustomer.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.ImageButtonBean;

public abstract class EditPageListAction<E extends EditPageListForm> extends EditPageAction<E>
{
    private final static Logger log = Logger.getLogger(EditPageListAction.class);
    
    public final static String FORM_KEY = "form";
    
    @Override
    protected final void preSearch(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response)
    {
        ActionMessages errors = new ActionMessages();
        
        // use a dynamic form key. an edit panel can have multiple lists
        String formKey = FORM_KEY+"."+form.getClass().getCanonicalName();
        
        if(!form.getDoSearch().isSelected() 
                && !form.getDoResetSearch().isSelected()
                && form.getSort() == null
                && form.getPage() == 0
                && getPanel().getAttribute(formKey) != null) {
            if(log.isDebugEnabled())
                log.debug("load form from panel");
            form = (E)getPanel().getAttribute(formKey);
            request.setAttribute(mapping.getAttribute(), form);
        } else if (getPanel().getAttribute(formKey) != null) {
            if(log.isDebugEnabled())
                log.debug("merge form with cached form");
            
            E cacheForm = (E)getPanel().getAttribute(formKey);
            
            if(form.getSort() == null)
                form.setSort(cacheForm.getSort());

            if(form.getPage() == 0)
                form.setPage(cacheForm.getPage());
        }

        search(form, errors, request);
        
        form.setDoSearch(new ImageButtonBean());
        form.setDoResetSearch(new ImageButtonBean());
        getPanel().setAttribute(formKey, form);
        
        if(!errors.isEmpty())
            this.saveErrors(request, errors);
    }
    
    @Override
    protected final void writeForm(E form, ActionMessages errors, HttpServletRequest request)
    {
        super.writeForm(form, errors, request);
    }

    @Override
    protected void readForm(E form, ActionMessages errors, HttpServletRequest request)
    {
        super.readForm(form, errors, request);
    }
    
    protected abstract void search(E form, ActionMessages errors, HttpServletRequest request);
}
