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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.dao.calendar.CalendarDAO;
import org.opencustomer.db.vo.system.RoleVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.UsergroupVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.UndeletableDAO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class UserDAO extends UndeletableDAO<UserVO>
{
    private static final Logger log = Logger.getLogger(UserDAO.class);

    public final static int SORT_USERNAME = 1;

    /**
     * @throws DatabaseException
     */
    public UserDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return UserVO.class;
    }
    
    public void delete(UserVO entity, Integer userId) throws HibernateException
    {
        new CalendarDAO().delete(entity.getCalendar(), userId);
        
        super.delete(entity, userId);
    }
    
    protected final String getSortString(Sort sort)
    {
        String orderField = "";

        switch (sort.getField())
        {
            case SORT_USERNAME:
            default:
                orderField = "e.userName";
                break;
        }

        if (!sort.isAscending())
            return orderField + " DESC";
        else
            return orderField + " ASC";
    }

    public UserVO getByUserName(String userName)
    {
        UserVO user = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.userName = :userName ");

            user = (UserVO) HibernateContext.getSession().createQuery(hql.toString()).setString("userName", userName).uniqueResult();

            if (log.isDebugEnabled())
                log.debug("found user: " + user);
        }
        catch (HibernateException e)
        {
            log.error("Could not find user by " + userName, e);
            throw e;
        }

        return user;
    }

    public List<UserVO> getForRole(RoleVO role)
    {
        List<UserVO> list = new ArrayList<UserVO>();

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.profile.role = :role ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());

            query.setEntity("role", role);

            list = toTypeSafeList(query.list());

            if (log.isDebugEnabled())
                log.debug("found role: " + role);
        }
        catch (HibernateException e)
        {
            log.error("Could not find role for " + role, e);
            throw e;
        }

        return list;
    }

    public List<UserVO> getForUsergroup(UsergroupVO usergroup)
    {
        List<UserVO> list = new ArrayList<UserVO>();

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT e ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE  e.profile.usergroups.id = :usergroupId ");
            hql.append(" order by e.userName ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());

            query.setInteger("usergroupId", usergroup.getId());

            list = toTypeSafeList(query.list());

            if (log.isDebugEnabled())
                log.debug("found users: " + list.size());
        }
        catch (HibernateException e)
        {
            log.error("Could not find user for " + usergroup, e);
            throw e;
        }

        return list;
    }

    public List<UserVO> getList(String userName, Boolean userLocked, Sort sort, Page page, UserVO user)
    {
        String _userName = toLower(adjustWildcards(userName));

        List<UserVO> list = new ArrayList<UserVO>();

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE 1=1 ");
            if (userName != null)
                hql.append(" AND lower(e.userName) like :userName ");
            if (userLocked != null) {
                if (userLocked) {
                    hql.append(" AND ((e.profile.validFrom is not null and e.profile.validFrom > current_timestamp() or e.profile.validUntil is not null and e.profile.validUntil < current_timestamp()) ");
                    hql.append(" or e.profile.locked = true) ");
                } else {
                    hql.append(" AND not((e.profile.validFrom is not null and e.profile.validFrom > current_timestamp() or e.profile.validUntil is not null and e.profile.validUntil < current_timestamp()) ");
                    hql.append(" or e.profile.locked = true) ");
                }
            }
            if (user != null) {
                if(!user.getProfile().getRole().isAdmin())
                    hql.append(" AND e.profile.role.admin = 0 ");
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }

            if (sort != null)
                hql.append(" order by " + getSortString(sort));

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (userName != null)
                query.setString("userName", _userName);
            if (user != null)
                query.setInteger("userId", user.getId());

            if (page != null)
            {
                query.setFirstResult(getFirstResult(page));
                query.setMaxResults(getMaxResults(page));
            }

            list = toTypeSafeList(query.list());

            if (log.isDebugEnabled())
                log.debug("found " + list.size() + " users");
        }
        catch (HibernateException e)
        {
            log.error("Could not find users", e);
            throw e;
        }

        return list;
    }

    public long countList(String userName, Boolean userLocked, UserVO user)
    {
        String _userName = toLower(adjustWildcards(userName));

        long count = 0;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT count(e.id) ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE 1=1 ");
            if (userName != null)
                hql.append(" AND lower(e.userName) like :userName ");
            if (userLocked != null) {
                if (userLocked) {
                    hql.append(" AND ((e.profile.validFrom is not null and e.profile.validFrom > current_timestamp() or e.profile.validUntil is not null and e.profile.validUntil < current_timestamp()) ");
                    hql.append(" or e.profile.locked = true) ");
                } else {
                    hql.append(" AND not((e.profile.validFrom is not null and e.profile.validFrom > current_timestamp() or e.profile.validUntil is not null and e.profile.validUntil < current_timestamp()) ");
                    hql.append(" or e.profile.locked = true) ");
                }
            }

            if (user != null) {
                if(!user.getProfile().getRole().isAdmin())
                    hql.append(" AND e.profile.role.admin = 0 ");
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (userName != null)
                query.setString("userName", _userName);
            if (user != null)
                query.setInteger("userId", user.getId());

            count = (Long) query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("count " + count + " users");
        }
        catch (HibernateException e)
        {
            log.error("Could not find users", e);
            throw e;
        }

        return count;
    }

}
