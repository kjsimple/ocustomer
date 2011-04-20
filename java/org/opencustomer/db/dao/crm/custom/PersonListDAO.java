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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.db.vo.crm.custom.PersonListVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.AbstractDAO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class PersonListDAO extends AbstractDAO
{
    private static final Logger log = Logger.getLogger(PersonListDAO.class);

    public final static int SORT_FIRSTNAME = 1;

    public final static int SORT_LASTNAME = 2;

    public final static int SORT_EMAIL = 3;

    public final static int SORT_PHONE = 4;

    public final static int SORT_FAX = 5;

    public final static int SORT_MOBILE = 6;

    public final static int SORT_COMPANY_NAME = 7;

    /**
     * @throws DatabaseException
     */
    public PersonListDAO() throws HibernateException
    {
        super();
    }

    public List<PersonListVO> getList(String firstName, String lastName, String phone, String fax, String mobile, String email, String companyName, PersonVO.Type type, Sort sort, Page page, UserVO user)
    {
        return getList(firstName, lastName, phone, fax, mobile, email, companyName, type, false, null, sort, page, user);
    }

    public long countList(String firstName, String lastName, String phone, String fax, String mobile, String email, String companyName, PersonVO.Type type, UserVO user)
    {
        return countList(firstName, lastName, phone, fax, mobile, email, companyName, type, false, null, user);
    }

    public List<PersonListVO> getList(String firstName, String lastName, String phone, String fax, String mobile, String email, String companyName, PersonVO.Type type, boolean notUsers, List<PersonVO> excludedPersons, Sort sort, Page page, UserVO user)
    {
        String _firstName = toLower(adjustWildcards(firstName));
        String _lastName = toLower(adjustWildcards(lastName));
        String _companyName = toLower(adjustWildcards(companyName));
        String _phone = toLower(adjustWildcards(phone));
        String _fax = toLower(adjustWildcards(fax));
        String _mobile = toLower(adjustWildcards(mobile));
        String _email = toLower(adjustWildcards(email));

        List<PersonListVO> list = new ArrayList<PersonListVO>();

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT e.id, e.firstName, e.lastName, e.phone, e.fax, e.mobile, e.email, c.companyName, e.type, u.id, u.userName ");
            hql.append(" FROM ").append(PersonVO.class.getName()).append(" e ");
            hql.append(" left join e.company c ");
            hql.append(" left join e.user u ");
            hql.append(" WHERE 1=1 ");
            if (firstName != null)
                hql.append(" AND lower(e.firstName) like :firstName ");
            if (lastName != null)
                hql.append(" AND lower(e.lastName) like :lastName ");
            if (phone != null)
                hql.append(" AND lower(e.phone) like :phone ");
            if (fax != null)
                hql.append(" AND lower(e.fax) like :fax ");
            if (mobile != null)
                hql.append(" AND lower(e.mobile) like :mobile ");
            if (email != null)
                hql.append(" AND lower(e.email) like :email ");
            if (companyName != null)
                hql.append(" AND lower(c.companyName) like :companyName ");
            if (type != null)
                hql.append(" AND lower(e.type) like :type ");
            if(notUsers)
                hql.append(" AND u.id is null ");
            if (excludedPersons != null && !excludedPersons.isEmpty())
            {
                StringBuilder personIds = new StringBuilder();
                Iterator<PersonVO> it = excludedPersons.iterator();
                while (it.hasNext())
                {
                    if (personIds.length() > 0)
                        personIds.append(", ");
                    personIds.append(it.next().getId());
                }

                hql.append(" AND LOWER(e.id) not in ( ").append(personIds).append(")");
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
            if (firstName != null)
                query.setString("firstName", _firstName);
            if (lastName != null)
                query.setString("lastName", _lastName);
            if (phone != null)
                query.setString("phone", _phone);
            if (fax != null)
                query.setString("fax", _fax);
            if (mobile != null)
                query.setString("mobile", _mobile);
            if (email != null)
                query.setString("email", _email);
            if (companyName != null)
                query.setString("companyName", _companyName);
            if (type != null)
                query.setString("type", type.toString());
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
                log.debug("found " + list.size() + " persons");
        }
        catch (HibernateException e)
        {
            log.error("Could not find persons", e);
            throw e;
        }

        return list;
    }

    public long countList(String firstName, String lastName, String phone, String fax, String mobile, String email, String companyName, PersonVO.Type type, boolean notUsers, List<PersonVO> excludedPersons, UserVO user)
    {
        String _firstName = toLower(adjustWildcards(firstName));
        String _lastName = toLower(adjustWildcards(lastName));
        String _companyName = toLower(adjustWildcards(companyName));
        String _phone = toLower(adjustWildcards(phone));
        String _fax = toLower(adjustWildcards(fax));
        String _mobile = toLower(adjustWildcards(mobile));
        String _email = toLower(adjustWildcards(email));

        long count = 0;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT count(e.id) ");
            hql.append(" FROM ").append(PersonVO.class.getName()).append(" e ");
            hql.append(" left join e.company c ");
            hql.append(" left join e.user u ");
            hql.append(" WHERE 1=1 ");
            if (firstName != null)
                hql.append(" AND lower(e.firstName) like :firstName ");
            if (lastName != null)
                hql.append(" AND lower(e.lastName) like :lastName ");
            if (phone != null)
                hql.append(" AND lower(e.phone) like :phone ");
            if (fax != null)
                hql.append(" AND lower(e.fax) like :fax ");
            if (mobile != null)
                hql.append(" AND lower(e.mobile) like :mobile ");
            if (email != null)
                hql.append(" AND lower(e.email) like :email ");
            if (companyName != null)
                hql.append(" AND lower(c.companyName) like :companyName ");
            if (type != null)
                hql.append(" AND e.type = :type ");
            if(notUsers)
                hql.append(" AND u.id is null ");
            if (excludedPersons != null && !excludedPersons.isEmpty())
            {
                StringBuilder personIds = new StringBuilder();
                Iterator<PersonVO> it = excludedPersons.iterator();
                while (it.hasNext())
                {
                    if (personIds.length() > 0)
                        personIds.append(", ");
                    personIds.append(it.next().getId());
                }

                hql.append(" AND LOWER(e.id) not in ( ").append(personIds).append(")");
            }
            if(user != null) {
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
            if (phone != null)
                query.setString("phone", _phone);
            if (fax != null)
                query.setString("fax", _fax);
            if (mobile != null)
                query.setString("mobile", _mobile);
            if (email != null)
                query.setString("email", _email);
            if (companyName != null)
                query.setString("companyName", _companyName);
            if (type != null)
                query.setString("type", type.toString());
            if (user != null)
                query.setInteger("userId", user.getId());

            count = (Long) query.uniqueResult();

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

    private void addEntity(List<PersonListVO> list, Object[] columns)
    {
        PersonListVO vo = new PersonListVO();

        vo.setPersonId((Integer) columns[0]);
        vo.setFirstName((String) columns[1]);
        vo.setLastName((String) columns[2]);
        vo.setPhone((String) columns[3]);
        vo.setFax((String) columns[4]);
        vo.setMobile((String) columns[5]);
        vo.setEmail((String) columns[6]);

        vo.setCompanyName((String) columns[7]);
        if(columns[8] != null)
            vo.setType((PersonVO.Type)columns[8]);

        if (columns[9] != null)
            vo.setUser(true);
        else
            vo.setUser(false);
        vo.setUserId((Integer) columns[9    ]);
        
        list.add(vo);
    }

    protected final String getSortString(Sort sort)
    {
        String orderField = "";

        switch (sort.getField())
        {
            case SORT_COMPANY_NAME:
                orderField = "c.companyName";
                break;
            case SORT_FIRSTNAME:
                orderField = "e.firstName";
                break;
            case SORT_LASTNAME:
            default:
                orderField = "e.lastName";
                break;
            case SORT_PHONE:
                orderField = "e.phone";
                break;
            case SORT_FAX:
                orderField = "e.fax";
                break;
            case SORT_MOBILE:
                orderField = "e.mobile";
                break;
            case SORT_EMAIL:
                orderField = "e.email";
                break;
        }

        if (!sort.isAscending())
            return orderField + " desc";
        else
            return orderField + " asc";
    }
}
