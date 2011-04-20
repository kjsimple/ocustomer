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
 * Software-Ingenieurb�ro). Portions created by the Initial Developer are
 * Copyright (C) 2005 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.db.dao.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.crm.ContactVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.VersionedDAO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class ContactDAO extends VersionedDAO<ContactVO>
{
    private static final Logger log = Logger.getLogger(ContactDAO.class);

    public final static int SORT_CONTACTTIMESTAMP = 1;

    public final static int SORT_SUBJECT = 2;

    public final static int SORT_CONTACTTYPE = 3;

    public final static int SORT_BOUNDTYPE = 4;

    /**
     * @throws DatabaseException
     */
    public ContactDAO() throws HibernateException
    {
        super();
    }
    
    protected Class getEntityClass()
    {
        return ContactVO.class;
    }

    protected final String getSortString(Sort sort)
    {
        String orderField = "";

        switch (sort.getField())
        {
            case SORT_CONTACTTIMESTAMP:
            default:
                orderField = "e.contactTimestamp";
                break;
            case SORT_SUBJECT:
                orderField = "e.subject";
                break;
            case SORT_CONTACTTYPE:
                orderField = "e.contactType";
                break;
            case SORT_BOUNDTYPE:
                orderField = "e.boundType";
                break;
        }

        if (!sort.isAscending())
            return orderField + " DESC";
        else
            return orderField + " ASC";
    }

    public List<ContactVO> getListForPerson(Integer personId, String subject, String contactType, String boundType, Date contactTimestampStart, Date contactTimestampEnd, String name, Sort sort, Page page, UserVO user)
    {
        String _subject = toLower(adjustWildcards(subject));
        String _name = toLower(adjustWildcards(name));

        List<ContactVO> list = new ArrayList<ContactVO>();

        try
        {
            StringBuilder hql = new StringBuilder();

            hql.append(" select distinct e ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" left join e.personContacts pc ");
            hql.append(" left join pc.person p ");
            hql.append(" left join e.company c ");
            hql.append(" WHERE e in ( ");
            hql.append(" select e1 ");
            hql.append(" from ").append(getEntityClass().getName()).append(" e1 ");
            hql.append(" inner join e1.personContacts pc1 ");
            hql.append(" inner join pc1.person p1 ");
            hql.append(" where p1.id = :personId ");
            hql.append(" ) ");
            if (subject != null)
                hql.append(" AND lower(e.subject) like :subject ");
            if (contactType != null)
                hql.append(" AND lower(e.contactType) like :contactType ");
            if (boundType != null)
                hql.append(" AND lower(e.boundType) like :boundType ");
            if (contactTimestampStart != null)
                hql.append(" AND e.contactTimestamp >= :contactTimestampStart ");
            if (contactTimestampEnd != null)
                hql.append(" AND e.contactTimestamp <= :contactTimestampEnd ");
            if (name != null)
            {
                hql.append(" AND (lower(p.firstName) like :name ");
                hql.append(" OR lower(p.lastName) like :name ");
                hql.append(" OR lower(c.companyName) like :name ");
                hql.append(" ) ");
            }
            if(user != null) {
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }
            
            if (sort != null)
                hql.append(" order by " + getSortString(sort));

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (personId != null)
                query.setInteger("personId", personId);
            if (subject != null)
                query.setString("subject", _subject);
            if (contactType != null)
                query.setString("contactType", contactType);
            if (boundType != null)
                query.setString("boundType", boundType);
            if (contactTimestampStart != null)
                query.setTimestamp("contactTimestampStart", contactTimestampStart);
            if (contactTimestampEnd != null)
                query.setTimestamp("contactTimestampEnd", contactTimestampEnd);
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
                log.debug("found " + list.size() + " contacts");
        }
        catch (HibernateException e)
        {
            log.error("Could not find contacts", e);
            throw e;
        }

        return list;
    }

    public long countListForPerson(Integer personId, String subject, String contactType, String boundType, Date contactTimestampStart, Date contactTimestampEnd, String name, UserVO user)
    {
        String _subject = toLower(adjustWildcards(subject));
        String _name = toLower(adjustWildcards(name));

        long count = 0;

        try
        {
            StringBuilder hql = new StringBuilder();

            hql.append(" select count(distinct e.id) ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" left join e.personContacts pc ");
            hql.append(" left join pc.person p ");
            hql.append(" left join e.company c ");
            hql.append(" WHERE e in ( ");
            hql.append(" select e1 ");
            hql.append(" from ").append(getEntityClass().getName()).append(" e1 ");
            hql.append(" inner join e1.personContacts pc1 ");
            hql.append(" inner join pc1.person p1 ");
            hql.append(" where p1.id = :personId ");
            hql.append(" ) ");
            if (subject != null)
                hql.append(" AND lower(e.subject) like :subject ");
            if (contactType != null)
                hql.append(" AND lower(e.contactType) like :contactType ");
            if (boundType != null)
                hql.append(" AND lower(e.boundType) like :boundType ");
            if (contactTimestampStart != null)
                hql.append(" AND e.contactTimestamp >= :contactTimestampStart ");
            if (contactTimestampEnd != null)
                hql.append(" AND e.contactTimestamp <= :contactTimestampEnd ");
            if (name != null)
            {
                hql.append(" AND (lower(p.firstName) like :name ");
                hql.append(" OR lower(p.lastName) like :name ");
                hql.append(" OR lower(c.companyName) like :name ");
                hql.append(" ) ");
            }
            if(user != null) {
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }
            
            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (personId != null)
                query.setInteger("personId", personId);
            if (subject != null)
                query.setString("subject", _subject);
            if (contactType != null)
                query.setString("contactType", contactType);
            if (boundType != null)
                query.setString("boundType", boundType);
            if (contactTimestampStart != null)
                query.setTimestamp("contactTimestampStart", contactTimestampStart);
            if (contactTimestampEnd != null)
                query.setTimestamp("contactTimestampEnd", contactTimestampEnd);
            if (name != null)
                query.setString("name", _name);
            if (user != null)
                query.setInteger("userId", user.getId());

            count = (Long) query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("count " + count + " contacts");
        }
        catch (HibernateException e)
        {
            log.error("Could not find contacts", e);
            throw e;
        }

        return count;
    }

    public List<ContactVO> getListForCompany(Integer companyId, String subject, String contactType, String boundType, Date contactTimestampStart, Date contactTimestampEnd, String name, Sort sort, Page page, UserVO user)
    {
        String _subject = toLower(adjustWildcards(subject));
        String _name = toLower(adjustWildcards(name));

        List<ContactVO> list = new ArrayList<ContactVO>();

        try
        {
            StringBuilder hql = new StringBuilder();

            hql.append(" select distinct e ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" left join e.personContacts pc ");
            hql.append(" left join pc.person p ");
            hql.append(" left join e.company c ");
            hql.append(" WHERE (c.id = :companyId ");
            hql.append(" OR pc.company.id = :companyId) ");
            if (subject != null)
                hql.append(" AND lower(e.subject) like :subject ");
            if (contactType != null)
                hql.append(" AND lower(e.contactType) like :contactType ");
            if (boundType != null)
                hql.append(" AND lower(e.boundType) like :boundType ");
            if (contactTimestampStart != null)
                hql.append(" AND e.contactTimestamp >= :contactTimestampStart ");
            if (contactTimestampEnd != null)
                hql.append(" AND e.contactTimestamp <= :contactTimestampEnd ");
            if (name != null)
            {
                hql.append(" AND (lower(p.firstName) like :name ");
                hql.append(" OR lower(p.lastName) like :name ");
                hql.append(" OR lower(c.companyName) like :name ");
                hql.append(" ) ");
            }
            if(user != null) {
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }
            
            if (sort != null)
                hql.append(" order by " + getSortString(sort));

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (companyId != null)
                query.setInteger("companyId", companyId);
            if (subject != null)
                query.setString("subject", _subject);
            if (contactType != null)
                query.setString("contactType", contactType);
            if (boundType != null)
                query.setString("boundType", boundType);
            if (contactTimestampStart != null)
                query.setTimestamp("contactTimestampStart", contactTimestampStart);
            if (contactTimestampEnd != null)
                query.setTimestamp("contactTimestampEnd", contactTimestampEnd);
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
                log.debug("found " + list.size() + " contacts");
        }
        catch (HibernateException e)
        {
            log.error("Could not find contacts", e);
            throw e;
        }

        return list;
    }

    public long countListForCompany(Integer companyId, String subject, String contactType, String boundType, Date contactTimestampStart, Date contactTimestampEnd, String name, UserVO user)
    {
        String _subject = toLower(adjustWildcards(subject));
        String _name = toLower(adjustWildcards(name));

        long count = 0;

        try
        {
            StringBuilder hql = new StringBuilder();

            hql.append(" select count(distinct e.id) ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" left join e.personContacts pc ");
            hql.append(" left join pc.person p ");
            hql.append(" left join e.company c ");
            hql.append(" WHERE (c.id = :companyId ");
            hql.append(" OR pc.company.id = :companyId) ");
            if (subject != null)
                hql.append(" AND lower(e.subject) like :subject ");
            if (contactType != null)
                hql.append(" AND lower(e.contactType) like :contactType ");
            if (boundType != null)
                hql.append(" AND lower(e.boundType) like :boundType ");
            if (contactTimestampStart != null)
                hql.append(" AND e.contactTimestamp >= :contactTimestampStart ");
            if (contactTimestampEnd != null)
                hql.append(" AND e.contactTimestamp <= :contactTimestampEnd ");
            if (name != null)
            {
                hql.append(" AND (lower(p.firstName) like :name ");
                hql.append(" OR lower(p.lastName) like :name ");
                hql.append(" OR lower(c.companyName) like :name ");
                hql.append(" ) ");
            }
            if(user != null) {
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }
            
            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (companyId != null)
                query.setInteger("companyId", companyId);
            if (subject != null)
                query.setString("subject", _subject);
            if (contactType != null)
                query.setString("contactType", contactType);
            if (boundType != null)
                query.setString("boundType", boundType);
            if (contactTimestampStart != null)
                query.setTimestamp("contactTimestampStart", contactTimestampStart);
            if (contactTimestampEnd != null)
                query.setTimestamp("contactTimestampEnd", contactTimestampEnd);
            if (name != null)
                query.setString("name", _name);
            if (user != null)
                query.setInteger("userId", user.getId());

            count = (Long) query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("count " + count + " contacts");
        }
        catch (HibernateException e)
        {
            log.error("Could not find contacts", e);
            throw e;
        }

        return count;
    }
}
