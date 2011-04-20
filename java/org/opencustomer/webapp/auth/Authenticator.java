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

package org.opencustomer.webapp.auth;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.opencustomer.db.vo.system.RightVO;
import org.opencustomer.db.vo.system.UserVO;

public class Authenticator
{
    private static final Logger log = Logger.getLogger(Authenticator.class);

    private Set<Right> rights = new HashSet<Right>();

    public Authenticator(UserVO user)
    {
        Iterator it = user.getProfile().getRole().getRights().iterator();
        while (it.hasNext())
        {
            RightVO right = (RightVO) it.next();

            StringBuilder builder = new StringBuilder();
            builder.append(right.getRightGroup().getRightArea().getLabel());
            builder.append(".");
            builder.append(right.getRightGroup().getLabel());
            builder.append(".");
            if (RightVO.Type.OTHER.equals(right.getType()))
                builder.append(right.getLabel());
            else
                builder.append(right.getType());

            if (log.isDebugEnabled())
                log.debug("add right to authenticator: " + builder.toString());

            rights.add(Right.parseRight(builder.toString()));
        }

        if (log.isDebugEnabled())
            log.debug("found " + rights.size() + " rights for user: " + user.getUserName());
    }

    public boolean isValid(Right... paramRights)
    {
        boolean valid = false;
        for (Right right : paramRights)
        {
            if (right != null)
            {
                valid = rights.contains(right);
                if (valid)
                {
                    if (log.isDebugEnabled())
                        log.debug("right " + right + " is valid");
                    break;
                }
                else
                {
                    if (log.isDebugEnabled())
                        log.debug("right " + right + " is invalid");
                }
            }
        }

        if (log.isDebugEnabled() && !valid)
            log.debug("no valid right found");

        return valid;
    }
}
