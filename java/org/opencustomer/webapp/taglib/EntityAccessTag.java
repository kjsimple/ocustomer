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

package org.opencustomer.webapp.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.webapp.Globals;

public class EntityAccessTag extends TagSupport
{
    private static final long serialVersionUID = 3257567287094620467L;

    private final static Logger log = Logger.getLogger(EntityAccessTag.class);

    private static final String DELIMITER = ",";

    private String scope;

    private String name;

    private String property;
    
    private EntityAccess.Access access;

    @Override
    public int doStartTag() throws JspException
    {
        if (condition())
            return EVAL_BODY_INCLUDE;
        else
            return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException
    {
        return EVAL_PAGE;
    }

    @Override
    public void release()
    {
        super.release();
        name = null;
        scope = null;
        access = null;
    }

    protected boolean condition() throws JspException
    {
        boolean hasAccess = false;

        if (access == null)
            hasAccess = true;
        else
        {
            UserVO user = (UserVO) pageContext.getSession().getAttribute(Globals.USER_KEY);

            Object obj = TagUtils.getInstance().lookup(pageContext, name, property, scope);
            
            if(obj instanceof EntityAccess)
            {
                EntityAccess entity = (EntityAccess)obj;
                if (EntityAccessUtility.isAccessGranted(user, entity, access))
                    hasAccess = true;
            }
            else
            {
                if(log.isDebugEnabled())
                    log.debug("not found entity access object: "+obj);
                hasAccess = true;
            }
        }

        return hasAccess;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public EntityAccess.Access getAccess()
    {
        return access;
    }

    public void setAccess(EntityAccess.Access access)
    {
        this.access = access;
    }

    public final String getProperty()
    {
        return property;
    }

    public final void setProperty(String property)
    {
        this.property = property;
    }

}
