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

package org.opencustomer.db.dao.crm;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.VersionedDAO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class CompanyDAO extends VersionedDAO<CompanyVO>
{
    private static final Logger log = Logger.getLogger(CompanyDAO.class);

    public final static int SORT_COMPANYNAME = 1;

    public final static int SORT_URL = 2;

    public final static int SORT_EMAIL = 3;

    public final static int SORT_PHONE = 4;

    public final static int SORT_FAX = 5;

    /**
     * @throws DatabaseException
     */
    public CompanyDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return CompanyVO.class;
    }

    protected final String getSortString(Sort sort)
    {
        String orderField = "";

        switch (sort.getField())
        {
            case SORT_COMPANYNAME:
                orderField = "e.companyName";
                break;
            case SORT_URL:
                orderField = "e.url";
                break;
            case SORT_EMAIL:
                orderField = "e.email";
                break;
            case SORT_PHONE:
                orderField = "e.phone";
                break;
            case SORT_FAX:
                orderField = "e.fax";
                break;
            default:
                orderField = "e.companyName";
                break;
        }

        if (!sort.isAscending())
            return orderField + " DESC";
        else
            return orderField + " ASC";
    }

    public List<CompanyVO> getList(String companyName, String url, String email, String phone, String fax, Sort sort, Page page, UserVO user)
    {
        String _companyName = toLower(adjustWildcards(companyName));
        String _url         = toLower(adjustWildcards(url));
        String _phone       = toLower(adjustWildcards(phone));
        String _fax         = toLower(adjustWildcards(fax));
        String _email       = toLower(adjustWildcards(email));

        List<CompanyVO> list = new ArrayList<CompanyVO>();

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE 1=1 ");
            if (companyName != null)
                hql.append(" AND lower(e.companyName) like :companyName ");
            if (url != null)
                hql.append(" AND lower(e.url) like :url ");
            if (phone != null)
                hql.append(" AND lower(e.phone) like :phone ");
            if (fax != null)
                hql.append(" AND lower(e.fax) like :fax ");
            if (email != null)
                hql.append(" AND lower(e.email) like :email ");
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
            if (companyName != null)
                query.setString("companyName", _companyName);
            if (url != null)
                query.setString("url", _url);
            if (phone != null)
                query.setString("phone", _phone);
            if (fax != null)
                query.setString("fax", _fax);
            if (email != null)
                query.setString("email", _email);
            if (user != null)
                query.setInteger("userId", user.getId());

            if (page != null)
            {
                query.setFirstResult(getFirstResult(page));
                query.setMaxResults(getMaxResults(page));
            }

            list = toTypeSafeList(query.list());

            if (log.isDebugEnabled())
                log.debug("found " + list.size() + " persons");
        }
        catch (HibernateException e)
        {
            log.error("Could not find persons", e);
            throw e;
        }

        return list;
    }

    public long countList(String companyName, String url, String email, String phone, String fax, UserVO user)
    {
        String _companyName = toLower(adjustWildcards(companyName));
        String _url         = toLower(adjustWildcards(url));
        String _phone       = toLower(adjustWildcards(phone));
        String _fax         = toLower(adjustWildcards(fax));
        String _email       = toLower(adjustWildcards(email));

        long count = 0;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT count(e.id) ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE 1=1 ");
            if (companyName != null)
                hql.append(" AND lower(e.companyName) like :companyName ");
            if (url != null)
                hql.append(" AND lower(e.url) like :url ");
            if (phone != null)
                hql.append(" AND lower(e.phone) like :phone ");
            if (fax != null)
                hql.append(" AND lower(e.fax) like :fax ");
            if (email != null)
                hql.append(" AND lower(e.email) like :email ");
            if(user != null) {
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }
            
            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (companyName != null)
                query.setString("companyName", _companyName);
            if (url != null)
                query.setString("url", _url);
            if (phone != null)
                query.setString("phone", _phone);
            if (fax != null)
                query.setString("fax", _fax);
            if (email != null)
                query.setString("email", _email);
            if (user != null)
                query.setInteger("userId", user.getId());

            count = (Long) query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("count " + count + " companies");
        }
        catch (HibernateException e)
        {
            log.error("Could not find companies", e);
            throw e;
        }

        return count;
    }

    public CompanyVO getByName(String companyName)
    {
        String _companyName = toLower(companyName);

        CompanyVO vo = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            if (companyName != null)
                hql.append(" WHERE lower(e.companyName) = :companyName ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (companyName != null)
                query.setString("companyName", _companyName);

            vo = (CompanyVO) query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("found " + vo);
        }
        catch (HibernateException e)
        {
            log.error("Could not find persons", e);
            throw e;
        }

        return vo;
    }
}
