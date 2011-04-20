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

package org.opencustomer.db.dao.crm.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.crm.custom.CompanyListVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.AbstractDAO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.db.vo.EntityAccess;

public class CompanyListDAO extends AbstractDAO
{
    private static final Logger log = Logger.getLogger(CompanyListDAO.class);

    public final static int SORT_COMPANY_NAME = 1;

    public final static int SORT_LAST_CONTACT_DATE = 2;

    public CompanyListDAO() throws HibernateException
    {
        super();
    }

    protected void addEntity(List<CompanyListVO> list, Object[] columns)
    {
        CompanyListVO vo = new CompanyListVO();

        vo.setId((Integer) columns[0]);
        vo.setCompanyName((String) columns[1]);
        vo.setCompanyTypeName((String) columns[2]);
        vo.setCompanyStateName((String) columns[3]);
        vo.setSectorName((String) columns[4]);
        vo.setLastContactDate((Date) columns[5]);
        vo.setCompanyTypeNameKey((String) columns[6]);
        vo.setCompanyStateNameKey((String) columns[7]);
        vo.setSectorNameKey((String) columns[8]);
        vo.setCategoryName((String) columns[9]);
        vo.setCategoryNameKey((String) columns[10]);
        vo.setRatingName((String) columns[11]);
        vo.setRatingNameKey((String) columns[12]);

        list.add(vo);
    }

    protected final String getSortString(Sort sort)
    {
        String orderField = "";

        switch (sort.getField())
        {
            case SORT_COMPANY_NAME:
            default:
                orderField = "2";
                break;
            case SORT_LAST_CONTACT_DATE:
                orderField = "6";
                break;
        }

        if (!sort.isAscending())
            return orderField + " DESC";
        else
            return orderField + " ASC";
    }

    public List<CompanyListVO> getList(String companyName, Integer companyStateId, Integer companyTypeId, Integer sectorId, Integer categoryId, Integer ratingId, Date lastContactDateStart, Date lastContactDateEnd, Sort sort, Page page, UserVO user)
    {
        String _companyName = toLower(adjustWildcards(companyName));

        List<CompanyListVO> list = new ArrayList<CompanyListVO>();

        try
        {
            StringBuilder hql = new StringBuilder();

            hql.append(" select c.id, c.companyName, ct.name, cs.name, s.name, ");
            hql.append(" (case when max(co.contactTimestamp) is null then max(co2.contactTimestamp) else ");
            hql.append("    case when max(co2.contactTimestamp) is null then max(co.contactTimestamp) else ");
            hql.append("       case when max(co.contactTimestamp) > max(co2.contactTimestamp) then max(co.contactTimestamp) else max(co2.contactTimestamp) end ");
            hql.append("    end ");
            hql.append(" end), ct.nameKey, cs.nameKey, s.nameKey, ca.name, ca.nameKey, r.name, r.nameKey");
            hql.append(" from ").append(CompanyVO.class.getName()).append(" c ");
            hql.append(" left join c.companyType ct ");
            hql.append(" left join c.companyState cs ");
            hql.append(" left join c.sector s ");
            hql.append(" left join c.category ca ");
            hql.append(" left join c.rating r ");
            hql.append(" left join c.contacts co ");
            hql.append(" left join c.personContacts pc ");
            hql.append(" left join pc.contact co2 ");
            hql.append(" where 1=1 ");

            if (companyName != null)
                hql.append(" AND LOWER(c.companyName) like :companyName");
            if (companyStateId != null)
                hql.append(" AND c.companyState = :companyState");
            if (companyTypeId != null)
                hql.append(" AND c.companyType = :companyType");
            if (sectorId != null)
                hql.append(" AND c.sector = :sector");
            if (ratingId != null)
                hql.append(" AND c.rating = :rating");
            if (categoryId != null)
                hql.append(" AND c.category = :category");
            if(user != null) {
                hql.append(" AND (c.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (c.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = c.ownerGroup)) ");
                hql.append(" OR (c.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND c.ownerUser = :userId)) ");
            }
            
            hql.append(" group by c.id, c.companyName, ct.name, cs.name, s.name");
            if (lastContactDateStart != null || lastContactDateEnd != null)
            {
                hql.append(" having ");
                if (lastContactDateStart != null)
                {
                    hql.append(" (case when max(co.contactTimestamp) is null then max(co2.contactTimestamp) else ");
                    hql.append("    case when max(co2.contactTimestamp) is null then max(co.contactTimestamp) else ");
                    hql.append("       case when max(co.contactTimestamp) > max(co2.contactTimestamp) then max(co.contactTimestamp) else max(co2.contactTimestamp) end ");
                    hql.append("    end ");
                    hql.append(" end ");
                    hql.append(" >= :lastContactDateStart)");
                }
                if (lastContactDateEnd != null)
                {
                    if (lastContactDateStart != null)
                        hql.append(" and ");
                    hql.append(" (case when max(co.contactTimestamp) is null then max(co2.contactTimestamp) else ");
                    hql.append("    case when max(co2.contactTimestamp) is null then max(co.contactTimestamp) else ");
                    hql.append("       case when max(co.contactTimestamp) > max(co2.contactTimestamp) then max(co.contactTimestamp) else max(co2.contactTimestamp) end ");
                    hql.append("    end ");
                    hql.append(" end ");
                    hql.append(" <= :lastContactDateEnd)");
                }
            }
            
            if (sort != null)
                hql.append(" order by " + getSortString(sort));

            // companyStateId, Integer companyTypeId, Integer sectorTypeId, Date
            // lastContactDateStart, Date lastContactDateEnd
            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (companyName != null)
                query.setString("companyName", _companyName);
            if (companyStateId != null)
                query.setInteger("companyState", companyStateId);
            if (companyTypeId != null)
                query.setInteger("companyType", companyTypeId);
            if (sectorId != null)
                query.setInteger("sector", sectorId);
            if (ratingId != null)
                query.setInteger("rating", ratingId);
            if (categoryId != null)
                query.setInteger("category", categoryId);
            if (lastContactDateStart != null)
                query.setDate("lastContactDateStart", lastContactDateStart);
            if (lastContactDateEnd != null)
                query.setDate("lastContactDateEnd", lastContactDateEnd);
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
                log.debug("found " + list.size() + " companies");
        }
        catch (HibernateException e)
        {
            log.error("Could not find companies", e);
            throw e;
        }

        return list;
    }

    public long countList(String companyName, Integer companyStateId, Integer companyTypeId, Integer sectorId, Integer categoryId, Integer ratingId, Date lastContactDateStart, Date lastContactDateEnd, UserVO user)
    {
        String _companyName = toLower(adjustWildcards(companyName));

        long count = 0;

        try
        {
            StringBuilder hql = new StringBuilder();

            hql.append(" select c.id ");
            hql.append(" from ").append(CompanyVO.class.getName()).append(" c ");
            hql.append(" left join c.companyType ct ");
            hql.append(" left join c.companyState cs ");
            hql.append(" left join c.sector s ");
            hql.append(" left join c.category ca ");
            hql.append(" left join c.rating r ");
            hql.append(" left join c.contacts co ");
            hql.append(" left join c.personContacts pc ");
            hql.append(" left join pc.contact co2 ");
            hql.append(" where 1=1 ");

            if (companyName != null)
                hql.append(" AND LOWER(c.companyName) like :companyName");
            if (companyStateId != null)
                hql.append(" AND c.companyState = :companyState");
            if (companyTypeId != null)
                hql.append(" AND c.companyType = :companyType");
            if (sectorId != null)
                hql.append(" AND c.sector = :sector");
            if (ratingId != null)
                hql.append(" AND c.rating = :rating");
            if (categoryId != null)
                hql.append(" AND c.category = :category");
            if(user != null) {
                hql.append(" AND (c.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (c.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = c.ownerGroup)) ");
                hql.append(" OR (c.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND c.ownerUser = :userId)) ");
            }
            hql.append(" group by c.id, c.companyName, ct.name, cs.name, s.name");
            if (lastContactDateStart != null || lastContactDateEnd != null)
            {
                hql.append(" having ");
                if (lastContactDateStart != null)
                {
                    hql.append(" (case when max(co.contactTimestamp) is null then max(co2.contactTimestamp) else ");
                    hql.append("    case when max(co2.contactTimestamp) is null then max(co.contactTimestamp) else ");
                    hql.append("       case when max(co.contactTimestamp) > max(co2.contactTimestamp) then max(co.contactTimestamp) else max(co2.contactTimestamp) end ");
                    hql.append("    end ");
                    hql.append(" end ");
                    hql.append(" >= :lastContactDateStart)");
                }
                if (lastContactDateEnd != null)
                {
                    if (lastContactDateStart != null)
                        hql.append(" and ");
                    hql.append(" (case when max(co.contactTimestamp) is null then max(co2.contactTimestamp) else ");
                    hql.append("    case when max(co2.contactTimestamp) is null then max(co.contactTimestamp) else ");
                    hql.append("       case when max(co.contactTimestamp) > max(co2.contactTimestamp) then max(co.contactTimestamp) else max(co2.contactTimestamp) end ");
                    hql.append("    end ");
                    hql.append(" end ");
                    hql.append(" <= :lastContactDateEnd)");
                }
            }

            // companyStateId, Integer companyTypeId, Integer sectorTypeId, Date
            // lastContactDateStart, Date lastContactDateEnd
            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (companyName != null)
                query.setString("companyName", _companyName);
            if (companyStateId != null)
                query.setInteger("companyState", companyStateId);
            if (companyTypeId != null)
                query.setInteger("companyType", companyTypeId);
            if (sectorId != null)
                query.setInteger("sector", sectorId);
            if (ratingId != null)
                query.setInteger("rating", ratingId);
            if (categoryId != null)
                query.setInteger("category", categoryId);
            if (lastContactDateStart != null)
                query.setDate("lastContactDateStart", lastContactDateStart);
            if (lastContactDateEnd != null)
                query.setDate("lastContactDateEnd", lastContactDateEnd);
            if (user != null)
                query.setInteger("userId", user.getId());

            count = query.list().size();

            if (log.isDebugEnabled())
                log.debug("count " + count + " companies");
        }
        catch (HibernateException e)
        {
            log.error("Could not count companies", e);
            throw e;
        }

        return count;
    }
}
