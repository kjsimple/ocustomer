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

package org.opencustomer.webapp.module.generic;

import org.apache.struts.util.ImageButtonBean;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.html.Table;

public final class DynamicListForm extends org.opencustomer.framework.webapp.action.ListForm {
    private static final long serialVersionUID = 3258135743162364472L;

    private int listId;
    
    private String doChooseList;
    
    private ImageButtonBean doEditList = new ImageButtonBean();

    private ImageButtonBean doAddList = new ImageButtonBean();

    @Override
    protected void adjustInput() {
        doChooseList = FormUtility.adjustParameter(doChooseList);
    }
    
    @Override
    protected void reset() {
        Table table = (Table)this.getPanel().getAttribute(DynamicListAction.TABLE_KEY);
        if(table != null) {
            table.resetSearch();
        }
    }

    public String getDoChooseList() {
        return doChooseList;
    }

    public void setDoChooseList(String doChooseList) {
        this.doChooseList = doChooseList;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public ImageButtonBean getDoAddList() {
        return doAddList;
    }

    public void setDoAddList(ImageButtonBean doAddList) {
        this.doAddList = doAddList;
    }

    public ImageButtonBean getDoEditList() {
        return doEditList;
    }

    public void setDoEditList(ImageButtonBean doEditList) {
        this.doEditList = doEditList;
    }
    
}
