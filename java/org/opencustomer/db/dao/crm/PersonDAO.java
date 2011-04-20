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

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.VersionedDAO;

public final class PersonDAO extends VersionedDAO<PersonVO>
{
    private static final Logger log = Logger.getLogger(PersonDAO.class);

    /**
     * @throws DatabaseException
     */
    public PersonDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return PersonVO.class;
    }

    public PersonVO getByName(String firstName, String lastName)
    {
        String _firstName = toLower(firstName);
        String _lastName = toLower(lastName);

        if (log.isDebugEnabled())
            log.debug("search for person by name (firstNane=" + firstName + "/lastName=" + lastName + ")");

        PersonVO person = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE 1=1 ");
            if (firstName != null)
                hql.append(" AND lower(e.firstName) = :firstName ");
            if (lastName != null)
                hql.append(" AND lower(e.lastName) = :lastName ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (firstName != null)
                query.setString("firstName", _firstName);
            if (lastName != null)
                query.setString("lastName", _lastName);

            person = (PersonVO) query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("found person: " + person);
        }
        catch (HibernateException e)
        {
            log.error("Could not find person", e);
            throw e;
        }

        return person;
    }

    public List<PersonVO> getByMail(String mail)
    {
        String _mail = toLower(adjustWildcards(mail));
        if (log.isDebugEnabled())
            log.debug("search for person by mail (Mail=" + mail);

        List<PersonVO> list = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            if (mail != null)
                hql.append(" WHERE lower(e.email) like :mail ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (mail != null)
                query.setString("mail", _mail);

            list = toTypeSafeList(query.list());

            HibernateContext.getSession().clear(); // lösche den SessionCache

            if (log.isDebugEnabled())
                log.debug("found " + list.size() + " persons");
        }
        catch (HibernateException e)
        {
            log.error("Could not find Person", e);
            throw e;
        }

        return list;
    }

    public List<PersonVO> getWithUser(PersonVO activePerson)
    {
        if (log.isDebugEnabled())
            log.debug("search for persons with user - except activePerson: "+activePerson);

        List<PersonVO> list = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT e ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" inner join e.user u ");
            if (activePerson != null)
                hql.append(" WHERE e.id != :activePerson ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (activePerson != null)
                query.setParameter("activePerson", activePerson.getId());

            list = toTypeSafeList(query.list());

            HibernateContext.getSession().clear(); // lösche den SessionCache

            if (log.isDebugEnabled())
                log.debug("found " + list.size() + " persons");
        }
        catch (HibernateException e)
        {
            log.error("Could not find Person", e);
            throw e;
        }

        return list;
    }

    public List<PersonVO> getForCompany(CompanyVO company)
    {
        if (log.isDebugEnabled())
            log.debug("get persons for company with id "+company.getId());

        List<PersonVO> list = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE company = :company ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            query.setEntity("company", company);

            list = toTypeSafeList(query.list());

            if (log.isDebugEnabled())
                log.debug("found " + list.size() + " persons");
        }
        catch (HibernateException e)
        {
            log.error("Could not find Person", e);
            throw e;
        }

        return list;
    }
}
