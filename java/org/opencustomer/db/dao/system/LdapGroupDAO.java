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
import org.opencustomer.db.vo.system.LdapGroupVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.VersionedDAO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class LdapGroupDAO extends VersionedDAO<LdapGroupVO>
{
    private static final Logger log = Logger.getLogger(LdapGroupDAO.class);

    public final static int SORT_NAME = 1;

    public final static int SORT_PRIORITY = 2;
    
    public LdapGroupDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return LdapGroupVO.class;
    }

    public LdapGroupVO getByName(String name)
    {
        LdapGroupVO group = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.name = :name ");

            group = (LdapGroupVO) HibernateContext.getSession().createQuery(hql.toString()).setString("name", name).uniqueResult();

            if (log.isDebugEnabled())
                log.debug("found ldap group: " + group);
        }
        catch (HibernateException e)
        {
            log.error("Could not find ldap group by " + name, e);
            throw e;
        }

        return group;
    }
    
    /**
     * Searches LdapGroupVO by a DN.
     * @param name dn for this group
     * @return the LdapGroupVO if a group is found, otherwise null
     * @author fbreske
     */
    public LdapGroupVO getByDN(String name)
    {
        LdapGroupVO group = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.ldapDN = :name ");

            group = (LdapGroupVO) HibernateContext.getSession().createQuery(hql.toString()).setString("name", name).uniqueResult();

            if (log.isDebugEnabled())
                log.debug("found ldap group: " + group);
        }
        catch (HibernateException e)
        {
            log.error("Could not find ldap group by " + name, e);
            throw e;
        }

        return group;
    }
    
    public List<LdapGroupVO> getList(String name, Sort sort, Page page, UserVO user)
    {
        String _name = toLower(adjustWildcards(name));

        List<LdapGroupVO> list = new ArrayList<LdapGroupVO>();

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE 1=1 ");
            if (name != null)
                hql.append(" AND lower(e.name) like :name ");
            if (user != null)
            {
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

    public long countList(String name, UserVO user)
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
            if (user != null)
            {
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (name != null)
                query.setString("name", _name);
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
                orderField = "e.ldap_usergroup";
                break;
            case SORT_PRIORITY:
            default:
                orderField = "e.priority";
                break;
        }

        if (!sort.isAscending())
            return orderField + " DESC";
        else
            return orderField + " ASC";
    }

}
