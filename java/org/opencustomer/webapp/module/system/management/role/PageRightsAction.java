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

package org.opencustomer.webapp.module.system.management.role;

import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.system.RightDAO;
import org.opencustomer.db.vo.system.RightVO;
import org.opencustomer.db.vo.system.RoleVO;
import org.opencustomer.webapp.action.EditPageAction;

public final class PageRightsAction extends EditPageAction<PageRightsForm>
{
    private final static Logger log = Logger.getLogger(PageRightsAction.class);

    @Override
    public void writeForm(PageRightsForm form, ActionMessages errors, HttpServletRequest request)
    {
        RoleVO role = (RoleVO) getPanel().getEntity();

        int[] rightIds = new int[role.getRights().size()];
        int pos = 0;
        Iterator<RightVO> it = role.getRights().iterator();
        while (it.hasNext())
        {
            RightVO right = it.next();
            rightIds[pos++] = right.getId().intValue();
        }
        form.setRights(rightIds);
        if (log.isDebugEnabled())
        {
            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < rightIds.length; i++)
            {
                if (i != 0)
                    ids.append(",");
                ids.append(rightIds[i]);
            }
            log.debug("rights: " + ids);
        }
    }
    
    @Override
    public void readForm(PageRightsForm form, ActionMessages errors, HttpServletRequest request)
    {
        RoleVO role = (RoleVO) getPanel().getEntity();

        // entferne zuerst alle Rechte
        role.getRights().clear();
    
        for (int i = 0; i < form.getRights().length; i++)
        {
            // füge die neuen Rechte hinzu
            try
            {
                RightVO right = new RightDAO().getById(new Integer(form.getRights()[i]));
                role.getRights().add(right);
                if (log.isDebugEnabled())
                    log.debug("added right: " + right);
            }
            catch (HibernateException e)
            {
                log.error("could not load role for id: " + form.getRights()[i], e);
            }
        }
        
        // füge alle fehlenden Lese-Rechte hinzu
        HashSet<RightVO> missingRights = new HashSet<RightVO>();
        for(RightVO right : role.getRights()) {
            if(RightVO.Type.WRITE.equals(right.getType())) {
                RightVO readRight = new RightDAO().getReadRightForRightGroup(right.getRightGroup());
                if(!role.getRights().contains(readRight)) {
                    missingRights.add(readRight);
                }
            }
        }
        if(!missingRights.isEmpty()) {
            if(log.isDebugEnabled())
                log.debug("add "+missingRights.size()+" missing rights");
            role.getRights().addAll(missingRights);
        }
    }
    
}
