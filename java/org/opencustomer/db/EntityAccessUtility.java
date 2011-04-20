package org.opencustomer.db;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.UsergroupVO;
import org.opencustomer.framework.db.vo.EntityAccess;

public class EntityAccessUtility
{
    private final static Logger log = Logger.getLogger(EntityAccessUtility.class);

    public static boolean isAccessGranted(UserVO user, EntityAccess entity, EntityAccess.Access access)
    {
        boolean accessGranted = false;

        if (log.isDebugEnabled())
            log.debug("check access (" + access + ") for user (id="+user.getId()+") on entity: "+entity);

        if (access != null)
        {
            if (EntityAccess.Access.READ.equals(access))
            {
                if (!entity.getAccessGlobal().equals(EntityAccess.Access.NONE))
                    accessGranted = true;
                else if (!entity.getAccessGroup().equals(EntityAccess.Access.NONE) && isUserInUsergroup(user, entity.getOwnerGroup()))
                    accessGranted = true;
                else if (!entity.getAccessUser().equals(EntityAccess.Access.NONE) && entity.getOwnerUser().equals(user.getId()))
                    accessGranted = true;
            }
            else if (EntityAccess.Access.WRITE.equals(access))
            {
                if (entity.getAccessGlobal().equals(EntityAccess.Access.WRITE) 
                        || entity.getAccessGlobal().equals(EntityAccess.Access.WRITE_SYSTEM))
                    accessGranted = true;
                else if ((entity.getAccessGroup().equals(EntityAccess.Access.WRITE) 
                        || entity.getAccessGroup().equals(EntityAccess.Access.WRITE_SYSTEM)) && isUserInUsergroup(user, entity.getOwnerGroup()))
                    accessGranted = true;
                else if ((entity.getAccessUser().equals(EntityAccess.Access.WRITE) 
                        || entity.getAccessUser().equals(EntityAccess.Access.WRITE_SYSTEM)) && entity.getOwnerUser().equals(user.getId()))
                    accessGranted = true;
            }
            else if (EntityAccess.Access.WRITE_SYSTEM.equals(access))
            {
                if (entity.getAccessGlobal().equals(EntityAccess.Access.WRITE_SYSTEM))
                    accessGranted = true;
                else if (entity.getAccessGroup().equals(EntityAccess.Access.WRITE_SYSTEM) && isUserInUsergroup(user, entity.getOwnerGroup()))
                    accessGranted = true;

                else if (entity.getAccessUser().equals(EntityAccess.Access.WRITE_SYSTEM) && entity.getOwnerUser().equals(user.getId()))
                    accessGranted = true;
            }
        }
        if (log.isDebugEnabled())
            log.debug("access (" + access + ") granted " + accessGranted + "  for entity: " + entity);

        return accessGranted;
    }

    private static boolean isUserInUsergroup(UserVO user, int usergroupId)
    {
        boolean found = false;

        Iterator<UsergroupVO> usergroups = user.getProfile().getUsergroups().iterator();
        while (usergroups.hasNext())
        {
            if (usergroups.next().getId() == usergroupId)
            {
                found = true;
                break;
            }
        }

        return found;
    }

}
