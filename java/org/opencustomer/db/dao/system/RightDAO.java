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

package org.opencustomer.db.dao.system;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.opencustomer.db.vo.system.RightGroupVO;
import org.opencustomer.db.vo.system.RightVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.BaseDAO;

/**
 * @author chaenderl
 * 
 */
public final class RightDAO extends BaseDAO<RightVO>
{
    private static final Logger log = Logger.getLogger(RightDAO.class);

    /**
     * @throws DatabaseException
     */
    public RightDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return RightVO.class;
    }

    public RightVO getReadRightForRightGroup(RightGroupVO rightGroup)
    {
        RightVO right = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.rightGroup = :rightGroup ");
            hql.append(" AND e.type = '"+RightVO.Type.READ+"'");

            right = (RightVO) HibernateContext.getSession()
                .createQuery(hql.toString())
                .setEntity("rightGroup", rightGroup)
                .uniqueResult();

            if (log.isDebugEnabled())
                log.debug("found right: " + right);
        }
        catch (HibernateException e)
        {
            log.error("Could not find read right for " + rightGroup, e);
            throw e;
        }

        return right;
    }
}
