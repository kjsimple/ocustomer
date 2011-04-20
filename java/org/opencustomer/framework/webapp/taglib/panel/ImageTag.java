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

package org.opencustomer.framework.webapp.taglib.panel;

import javax.servlet.jsp.PageContext;

import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.html.FormTag;

public class ImageTag extends org.apache.struts.taglib.html.ImageTag
{
    private static final long serialVersionUID = 6566284241707033364L;

    private boolean enable = false;
    
    @Override
    protected void prepareFocusEvents(StringBuffer handlers)
    {
        prepareAttribute(handlers, "onblur", getOnblur());
        prepareAttribute(handlers, "onfocus", getOnfocus());
        
        if(!enable)
        {
            // Get the parent FormTag (if necessary)
            FormTag formTag = null;
            if ((doDisabled && !getDisabled()) ||
                (doReadonly && !getReadonly())) {
                formTag = (FormTag)pageContext.getAttribute(Constants.FORM_KEY,
                                                            PageContext.REQUEST_SCOPE);
            }
    
            // Format Disabled
            if (doDisabled) {
                boolean formDisabled = formTag == null ? false : formTag.isDisabled();
                if (formDisabled || getDisabled()) {
                    handlers.append(" disabled=\"disabled\"");
                }
            }
    
            // Format Read Only
            if (doReadonly) {
                boolean formReadOnly = formTag == null ? false : formTag.isReadonly();
                if (formReadOnly || getReadonly()) {
                    handlers.append(" readonly=\"readonly\"");
                }
            }
        }
    }

    public final String getEnable()
    {
        return Boolean.toString(enable);
    }

    public final void setEnable(String enable)
    {
        if(enable != null)
            this.enable = Boolean.parseBoolean(enable.toLowerCase());
        else
            this.enable = true;
    }
    
}
