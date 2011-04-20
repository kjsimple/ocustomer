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

package org.opencustomer.db.dao.system;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.system.UserLoginLogVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.UserLoginLogVO.Result;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.BaseDAO;

public final class UserLoginLogDAO extends BaseDAO<UserLoginLogVO>
{
    private static final Logger log = Logger.getLogger(UserLoginLogDAO.class);

    /**
     * @throws DatabaseException
     */
    public UserLoginLogDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return UserLoginLogVO.class;
    }

    public UserLoginLogVO getLastValidLoginForUser(UserVO user) {
        return getLastLoginForUser(user, Result.VALID);
    }

    public UserLoginLogVO getLastInvalidLoginForUser(UserVO user) {
        return getLastLoginForUser(user, Result.INVALID);
    }

    private UserLoginLogVO getLastLoginForUser(UserVO user, Result result)
    {
        UserLoginLogVO userLoginLog = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.user = :user ");
            hql.append(" AND result = :result ");
            hql.append(" ORDER by e.loginDate DESC ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            query.setEntity("user", user);
            query.setString("result", result.toString());
            
            query.setMaxResults(1);
            
            userLoginLog = (UserLoginLogVO) query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("found user: " + user);
        }
        catch (HibernateException e)
        {
            log.error("Could not find user login log for user with id " + user.getId(), e);
            throw e;
        }

        return userLoginLog;
    }

    public long countInvalidLoginsForUser(UserVO user)
    {
        long count = 0;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT count(*) ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.user = :user ");
            hql.append(" AND result = '").append(Result.INVALID).append("'");
            hql.append(" AND loginDate >= ( ");
            hql.append(" select max(s.loginDate) ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" s ");
            hql.append(" WHERE s.user = e.user ");
            hql.append(" AND result = '").append(Result.VALID).append("'");
            hql.append(" ) "); 
            hql.append(" ORDER by e.loginDate DESC ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            query.setEntity("user", user);
            
            query.setMaxResults(1);
            
            count = (Long)query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("found user: " + user);
        }
        catch (HibernateException e)
        {
            log.error("Could not find user login log for user with id " + user.getId(), e);
            throw e;
        }

        return count;
    }
}
