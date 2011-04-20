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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.ImageButtonBean;
import org.opencustomer.framework.db.util.Sort;

public abstract class EditPageListForm extends EditPageForm
{
    private final static Logger log = Logger.getLogger(EditPageListForm.class);
    
    private ImageButtonBean doSearch = new ImageButtonBean();

    private ImageButtonBean doResetSearch = new ImageButtonBean();

    private String sort;

    private int page;
    
    @Override
    public final void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        if(getPanel() != null) {
            adjustInput();
            
            sort = Sort.parseRequest(request, sort);

            if (doResetSearch.isSelected())
                reset();

            validate(errors, request);
        }
    }
    
    protected abstract void adjustInput();
    
    protected abstract void reset();
    
    protected void validate(ActionMessages errors, HttpServletRequest request)
    {
    }
    
    public final int getPage()
    {
        return page;
    }

    public final void setPage(int page)
    {
        this.page = page;
    }

    public final String getSort()
    {
        return sort;
    }

    public final void setSort(String sort)
    {
        this.sort = sort;
    }

    public final ImageButtonBean getDoResetSearch()
    {
        return doResetSearch;
    }

    public final void setDoResetSearch(ImageButtonBean doResetSearch)
    {
        this.doResetSearch = doResetSearch;
    }

    public final ImageButtonBean getDoSearch()
    {
        return doSearch;
    }

    public final void setDoSearch(ImageButtonBean doSearch)
    {
        this.doSearch = doSearch;
    }        
}
