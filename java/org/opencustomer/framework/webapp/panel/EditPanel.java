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

package org.opencustomer.framework.webapp.panel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.BaseVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.webapp.auth.AuthenticatorUtility;
import org.opencustomer.webapp.auth.Right;

public final class EditPanel extends EntityPanel implements Serializable
{    
    private static final long serialVersionUID = -7714593175899323240L;

    private static final Logger log = Logger.getLogger(EditPanel.class);
    
    private List<Page> pages = new ArrayList<Page>();

    private boolean editable = true;
    
    private Right editRight;
     
    private Page activePage;
    
    private Hashtable<Action.Type, Action> actions = new Hashtable<Action.Type, Action>();
    
    public final static String TITLE_DELETE = "title_delete";
    
    public EditPanel(Right editRight, BaseVO entity) 
    {
        super(Type.EDIT);
        this.editRight = editRight;
        setEntity(entity);

        if(entity != null)
            setName("Panel: "+entity.getClass().getSimpleName()+" "+entity.getId());
        else
            setName("Panel: no entity");
    }

    public Right getEditRight()
    {
        return editRight;
    }

    public static boolean checkEditable(HttpServletRequest request)
    {
        return checkEditable(request, (EditPanel)Panel.getPanelStack(request).peek());
    }
    
    public static boolean checkEditable(HttpServletRequest request, EditPanel panel) {
        EntityAccess entityAccess = null;
        if(panel.getEntity() instanceof EntityAccess) {
            entityAccess = (EntityAccess)panel.getEntity();
        }
        
        return checkEditable(request, panel, entityAccess);
    }
    
    public static boolean checkEditable(HttpServletRequest request, EditPanel panel, EntityAccess entityAccess) {
        UserVO user = (UserVO)request.getSession().getAttribute(org.opencustomer.webapp.Globals.USER_KEY);
        
        return checkEditable(request, panel, entityAccess, user);
    }
    
    public static boolean checkEditable(HttpServletRequest request, EditPanel panel, EntityAccess entityAccess, UserVO user)
    {
        if(log.isDebugEnabled())
            log.debug("test for user "+user);
        
        boolean editable = false;

        if(panel.getEditRight() == null || 
                AuthenticatorUtility.getInstance().authenticate(request, panel.getEditRight()))
        {
            editable = true;
            if(log.isDebugEnabled())
                log.debug("access valid for panel controlled by right: "+panel.getEditRight());
        }
        
        if(entityAccess != null)
        {
            if(log.isDebugEnabled())
                log.debug("test access for panel via entity access");
            
            if(!EntityAccessUtility.isAccessGranted(user, entityAccess, EntityAccess.Access.WRITE))
            {
                editable = false;
                if(log.isDebugEnabled())
                    log.debug("access not valid for panel controlled by entity access: "+panel.getEntity());
            }
        }
        
        return editable;
    }

    public final boolean isEditable()
    {
        return editable;
    }

    public final void setEditable(boolean editable)
    {
        this.editable = editable;
    }
    

    public List<Page> getPages()
    {
        return pages;
    }

    public void addPage(String name, String action, String key)
    {
        addPage(name, action, key, true);
    }
    
    public void addPage(String name, String action, String key, boolean selectable)
    {
        addPage(name, action, key, selectable, null);
    }
    
    public void addPage(String name, String action, String key, boolean selectable, Boolean editable)
    {
        Page page = new Page(name, action, key, selectable, editable);
        this.pages.add(page);
        
        if(activePage == null && page.isSelectable())
            activatePage(page);
    }
    
    public boolean activatePage(Page page) {
        boolean changed = false;
        
        if(page != null && (activePage == null || !activePage.equals(page)) && page.isSelectable()) {
            activePage = page;
            setPath(activePage.getAction());
            changed = true;
        }
        
        return changed;
    }

    public Page findPage(HttpServletRequest request)
    {
        Page page = null;
        
        if(!pages.isEmpty())
        {
            String panelName = null;
           
            Enumeration params = request.getParameterNames();
            while(params.hasMoreElements()) 
            {
                String name = (String)params.nextElement();
                if(name.startsWith("doPage_")) 
                {
                   panelName = name.substring(7);
                   break;
                }
            }
            
            page = findPage(panelName);
        }
        
        return page;
    }
    
    public Page findPage(String name) {
        Page page = null;
        
        for(Page testPage : pages) {
            if(testPage.getName().equals(name)) {
                page = testPage;
                break;
            }
        }
        
        return page;
    }
    
    public Page getActivePage() 
    {
        if(this.activePage == null)
        {
            if(pages.isEmpty())
                return null;
            else
                return pages.get(0);
        }
        else
            return activePage;
    }
    
    public void addAction(Action.Type actionType, String action, boolean selectable)
    {
        actions.put(actionType, new Action(action, selectable));
    }

    public Action getAction(Action.Type actionType)
    {
        return actions.get(actionType);
    }
    
    public void addAction(Action.Type actionType, String action)
    {
        addAction(actionType, action, true);
    }
    
    @Override
    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("entity", getEntity());
        builder.append("editRight", editRight);
        
        return builder.toString();
    }

}
