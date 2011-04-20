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
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.db.vo.system.UsergroupVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.UndeletableDAO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class UsergroupDAO extends UndeletableDAO<UsergroupVO>
{
    private static final Logger log = Logger.getLogger(UsergroupDAO.class);

    public enum AdminSelect {
        ADMIN,
        NOT_ADMIN,
        ALL;
    }
    
    public final static int SORT_NAME = 1;

    /**
     * @throws DatabaseException
     */
    public UsergroupDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return UsergroupVO.class;
    }

    public List<UsergroupVO> getList(String name, AdminSelect adminSelect, Sort sort, Page page, UserVO user)
    {
        String _name = toLower(adjustWildcards(name));

        List<UsergroupVO> list = new ArrayList<UsergroupVO>();

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE 1=1 ");
            if (name != null)
                hql.append(" AND lower(e.name) like :name ");
            if(!adminSelect.equals(AdminSelect.ALL))
                hql.append(" AND admin like :admin ");
            if (user != null) {
                if(!user.getProfile().getRole().isAdmin())
                    hql.append(" AND e.admin = 0 ");
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }
            
            if (sort != null)
                hql.append(" order by " + getSortString(sort));

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (name != null)
                query.setString("name", _name);
            if(adminSelect.equals(AdminSelect.ADMIN))
                query.setBoolean("admin", true);
            else if(adminSelect.equals(AdminSelect.NOT_ADMIN))
                query.setBoolean("admin", false);
            if (user != null)
                query.setInteger("userId", user.getId());

            if (page != null)
            {
                query.setFirstResult(getFirstResult(page));
                query.setMaxResults(getMaxResults(page));
            }

            list = toTypeSafeList(query.list());

            if (log.isDebugEnabled())
                log.debug("found " + list.size() + " usergroups");
        }
        catch (HibernateException e)
        {
            log.error("Could not find usergroups", e);
            throw e;
        }

        return list;
    }

    public long countList(String name, AdminSelect adminSelect, UserVO user)
    {
        String _name = toLower(adjustWildcards(name));

        long count = 0;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT count(e.id) ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE 1=1 ");
            if (name != null)
                hql.append(" AND lower(e.name) like :name ");
            if(!adminSelect.equals(AdminSelect.ALL))
                hql.append(" AND admin like :admin ");
            if (user != null) {
                if(!user.getProfile().getRole().isAdmin())
                    hql.append(" AND e.admin = 0 ");
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (name != null)
                query.setString("name", _name);
            if(adminSelect.equals(AdminSelect.ADMIN))
                query.setBoolean("admin", true);
            else if(adminSelect.equals(AdminSelect.NOT_ADMIN))
                query.setBoolean("admin", false);
            if (user != null)
                query.setInteger("userId", user.getId());

            count = (Long) query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("count " + count + " usergroups");
        }
        catch (HibernateException e)
        {
            log.error("Could not find usergroups", e);
            throw e;
        }

        return count;
    }

    protected final String getSortString(Sort sort)
    {
        String orderField = "";

        switch (sort.getField())
        {
            case SORT_NAME:
            default:
                orderField = "e.name";
                break;
        }

        if (!sort.isAscending())
            return orderField + " DESC";
        else
            return orderField + " ASC";
    }

    public UsergroupVO getByName(String name)
    {
        String _name = toLower(adjustWildcards(name));

        UsergroupVO usergroup = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            if (name != null)
                hql.append(" WHERE lower(e.name) like :name ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (name != null)
                query.setString("name", _name);

            usergroup = (UsergroupVO) query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("found usergroup: " + usergroup);
        }
        catch (HibernateException e)
        {
            log.error("Could not find usergroup", e);
            throw e;
        }

        return usergroup;
    }

}
