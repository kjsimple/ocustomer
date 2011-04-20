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

package org.opencustomer.framework.webapp.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.ImageButtonBean;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.struts.ActionForm;

public abstract class ListForm extends ActionForm
{
    private int id;

    private ImageButtonBean doSearch = new ImageButtonBean();

    private ImageButtonBean doResetSearch = new ImageButtonBean();

    private String sort;

    private String order;
    
    private int page;
    
    private Panel panel;
    
    @Override
    public final void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        if(!Panel.getPanelStack(request).isEmpty())
            panel = Panel.getPanelStack(request).peek();
        
        if(panel != null) {
            adjustInput();
            order = FormUtility.adjustParameter(order);
            
            sort = Sort.parseRequest(request, sort);
    
            if (id < 0)
                id = 0;

            if (doResetSearch.isSelected())
                reset();
            else
                validate(errors, request);
        }
    }

    protected abstract void adjustInput();
    
    protected abstract void reset();
    
    protected void validate(ActionMessages errors, HttpServletRequest request)
    {
    }
    
    protected Panel getPanel() {
        return panel;
    }
    
    public final int getId()
    {
        return id;
    }

    public final void setId(int id)
    {
        this.id = id;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }    
}
