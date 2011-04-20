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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.UserAssigned;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.UsergroupVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.util.EnumUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditPageForm;

public class PageSystemForm extends EditPageForm
{
    private static final long serialVersionUID = 3977018439913322801L;

    private final static Logger log = Logger.getLogger(PageSystemForm.class);

    private String groupAccess;

    private int group;

    private String globalAccess;
    
    public final void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        if (globalAccess == null)
            errors.add("globalAccess", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.entityAccess.globalAccess")));
        else if (EnumUtility.valueOf(EntityAccess.Access.class, globalAccess) == null)
            errors.add("globalAccess", new ActionMessage("default.error.invalidValue", MessageUtil.message(request, "entity.entityAccess.globalAccess")));

        if (groupAccess == null)
            errors.add("groupAccess", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.entityAccess.groupAccess")));
        else if (EnumUtility.valueOf(EntityAccess.Access.class, groupAccess) == null)
            errors.add("groupAccess", new ActionMessage("default.error.invalidValue", MessageUtil.message(request, "entity.entityAccess.groupAccess")));

        if (group <= 0)
            errors.add("group", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.entityAccess.groupOwner")));
        else
        {
            UsergroupVO usergroup = null;

            List<UsergroupVO> usergroups = (List<UsergroupVO>)getPanel().getAttribute("system_usergroups");
            for (UsergroupVO ug : usergroups)
            {
                if (ug.getId() == group)
                {
                    usergroup = ug;
                    break;
                }
            }

            if (usergroup == null)
            {
                errors.add("group", new ActionMessage("default.error.invalidValue", MessageUtil.message(request, "entity.entityAccess.groupOwner")));
            }
        }
        
        if(getEntityAccess(request) != null)
        {
            UserVO user = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
            EntityAccess entity = getEntityAccess(request);
            TestEntityAccess test = new TestEntityAccess(entity);
            test.setAccessGlobal(EnumUtility.valueOf(EntityAccess.Access.class, globalAccess));
            test.setAccessGroup(EnumUtility.valueOf(EntityAccess.Access.class, groupAccess));
            test.setOwnerGroup(group);
            
            validateAccess(errors, user, test);
        }
    }

    protected void validateAccess(ActionMessages errors, UserVO activeUser, EntityAccess testEntity) {
        if(!EntityAccessUtility.isAccessGranted(activeUser, testEntity, EntityAccess.Access.WRITE_SYSTEM))
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.generic.pageSystem.error.noRightsLeft"));
        
        if(getPanel().getEntity() instanceof UserAssigned) {
            UserAssigned userAssigned = (UserAssigned)getPanel().getEntity();
            
            if(userAssigned.getAssignedUser() != null && !EntityAccessUtility.isAccessGranted(userAssigned.getAssignedUser(), testEntity, EntityAccess.Access.WRITE))
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.generic.pageSystem.error.missingWriteAccessForAssignedUser", userAssigned.getAssignedUser().getUserName()));
        }

    }
    
    protected EntityAccess getEntityAccess(HttpServletRequest request) {
        if(getPanel().getEntity() instanceof EntityAccess) {
            return (EntityAccess)getPanel().getEntity();
        } else {
            return null;
        }
    }
    
    public final String getGlobalAccess()
    {
        return globalAccess;
    }

    public final void setGlobalAccess(String globalAccess)
    {
        this.globalAccess = globalAccess;
    }

    public final int getGroup()
    {
        return group;
    }

    public final void setGroup(int group)
    {
        this.group = group;
    }

    public final String getGroupAccess()
    {
        return groupAccess;
    }

    public final void setGroupAccess(String groupAccess)
    {
        this.groupAccess = groupAccess;
    }

    private final class TestEntityAccess implements EntityAccess
    {
        public TestEntityAccess()
        {
            
        }
        
        public TestEntityAccess(EntityAccess entity)
        {
            this.setAccessGlobal(entity.getAccessGlobal());
            this.setAccessGroup(entity.getAccessGroup());
            this.setOwnerGroup(entity.getOwnerGroup());
            this.setAccessUser(entity.getAccessUser());
            this.setOwnerUser(entity.getOwnerUser());
        }
        
        private Integer ownerUser;

        private Integer ownerGroup;
        
        private EntityAccess.Access accessUser;

        private EntityAccess.Access accessGroup;

        private EntityAccess.Access accessGlobal;

        public EntityAccess.Access getAccessGlobal()
        {
            return accessGlobal;
        }

        public void setAccessGlobal(EntityAccess.Access accessGlobal)
        {
            this.accessGlobal = accessGlobal;
        }

        public EntityAccess.Access getAccessGroup()
        {
            return accessGroup;
        }

        public void setAccessGroup(EntityAccess.Access accessGroup)
        {
            this.accessGroup = accessGroup;
        }

        public EntityAccess.Access getAccessUser()
        {
            return accessUser;
        }

        public void setAccessUser(EntityAccess.Access accessUser)
        {
            this.accessUser = accessUser;
        }

        public Integer getOwnerGroup()
        {
            return ownerGroup;
        }

        public void setOwnerGroup(Integer ownerGroup)
        {
            this.ownerGroup = ownerGroup;
        }

        public Integer getOwnerUser()
        {
            return ownerUser;
        }

        public void setOwnerUser(Integer ownerUser)
        {
            this.ownerUser = ownerUser;
        }
    }
}
