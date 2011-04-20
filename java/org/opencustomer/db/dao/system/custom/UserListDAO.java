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

package org.opencustomer.db.dao.system.custom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.custom.UserListVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.AbstractDAO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class UserListDAO extends AbstractDAO
{
    private static final Logger log = Logger.getLogger(UserListDAO.class);

    public final static int SORT_FIRSTNAME = 1;

    public final static int SORT_LASTNAME = 2;

    public final static int SORT_USERNAME = 3;
    /**
     * @throws DatabaseException
     */
    public UserListDAO() throws HibernateException
    {
        super();
    }

    public List<UserListVO> getList(String firstName, String lastName, String userName, List<UserVO> excludedUsers, Sort sort, Page page, UserVO user)
    {
        String _firstName = toLower(adjustWildcards(firstName));
        String _lastName = toLower(adjustWildcards(lastName));
        String _userName = toLower(adjustWildcards(userName));

        List<UserListVO> list = new ArrayList<UserListVO>();

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT p.id, p.firstName, p.lastName, e.id, e.userName ");
            hql.append(" FROM ").append(UserVO.class.getName()).append(" e ");
            hql.append(" left join e.person p ");
            hql.append(" WHERE 1=1 ");
            if (firstName != null)
                hql.append(" AND lower(p.firstName) like :firstName ");
            if (lastName != null)
                hql.append(" AND lower(p.lastName) like :lastName ");
            if (userName != null)
                hql.append(" AND lower(e.userName) like :userName ");
            if (excludedUsers != null)
            {
                StringBuilder userIds = new StringBuilder();
                for(UserVO exUser : excludedUsers)
                {
                    if (userIds.length() > 0)
                        userIds.append(", ");
                    userIds.append(exUser.getId());
                }

                hql.append(" AND LOWER(e.id) not in ( ").append(userIds).append(")");
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
            if (firstName != null)
                query.setString("firstName", _firstName);
            if (lastName != null)
                query.setString("lastName", _lastName);
            if (userName != null)
                query.setString("userName", _userName);
            if (user != null)
                query.setInteger("userId", user.getId());

            if (page != null)
            {
                query.setFirstResult(getFirstResult(page));
                query.setMaxResults(getMaxResults(page));
            }

            Iterator it = query.list().iterator();
            while (it.hasNext())
                addEntity(list, (Object[]) it.next());

            if (log.isDebugEnabled())
                log.debug("found " + list.size() + " users");
        }
        catch (HibernateException e)
        {
            log.error("Could not find persons", e);
            throw e;
        }

        return list;
    }

    public long countList(String firstName, String lastName, String userName, List<UserVO> excludedUsers, UserVO user)
    {
        String _firstName = toLower(adjustWildcards(firstName));
        String _lastName = toLower(adjustWildcards(lastName));
        String _userName = toLower(adjustWildcards(userName));

        long count = 0;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT count(e.id) ");
            hql.append(" FROM ").append(UserVO.class.getName()).append(" e ");
            hql.append(" left join e.person p ");
            hql.append(" WHERE 1=1 ");
            if (firstName != null)
                hql.append(" AND lower(p.firstName) like :firstName ");
            if (lastName != null)
                hql.append(" AND lower(p.lastName) like :lastName ");
            if (userName != null)
                hql.append(" AND lower(e.userName) like :userName ");
            if (excludedUsers != null)
            {
                StringBuilder userIds = new StringBuilder();
                for(UserVO exUser : excludedUsers)
                {
                    if (userIds.length() > 0)
                        userIds.append(", ");
                    userIds.append(exUser.getId());
                }

                hql.append(" AND LOWER(e.id) not in ( ").append(userIds).append(")");
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
            if (firstName != null)
                query.setString("firstName", _firstName);
            if (lastName != null)
                query.setString("lastName", _lastName);            
            if (userName != null)
                query.setString("userName", _userName);
            if (user != null)
                query.setInteger("userId", user.getId());

            count = (Long)query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("count " + count + " persons");
        }
        catch (HibernateException e)
        {
            log.error("Could not find persons", e);
            throw e;
        }

        return count;
    }

    private void addEntity(List<UserListVO> list, Object[] columns)
    {
        UserListVO vo = new UserListVO();

        vo.setPersonId((Integer) columns[0]);
        vo.setFirstName((String) columns[1]);
        vo.setLastName((String) columns[2]);
        
        vo.setUserId((Integer) columns[3]);
        vo.setUserName((String) columns[4]);
        
        list.add(vo);
    }

    protected final String getSortString(Sort sort)
    {
        String orderField = "";

        switch (sort.getField())
        {
            case SORT_FIRSTNAME:
                orderField = "p.firstName";
                break;
            case SORT_LASTNAME:
                orderField = "p.lastName";
                break;
            case SORT_USERNAME:
            default:
                orderField = "e.userName";
                break;
        }

        if (!sort.isAscending())
            return orderField + " desc";
        else
            return orderField + " asc";
    }
}
