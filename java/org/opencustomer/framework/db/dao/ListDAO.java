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

package org.opencustomer.framework.db.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.vo.ListVO;

public abstract class ListDAO<E extends ListVO> extends BaseDAO<E>
{
    private static final Logger log = Logger.getLogger(ListDAO.class);

    public ListDAO() throws HibernateException
    {
        super();
    }

    public final List<E> getAll() throws HibernateException
    {
        List<E> l = null;
        try
        {
            StringBuffer hql = new StringBuffer();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.deleted = 0");
            hql.append(" ORDER BY e.sort");

            l = toTypeSafeList(HibernateContext.getSession().createQuery(hql.toString()).list());

        }
        catch (HibernateException e)
        {
            log.error("Could not get all objects", e);
            throw e;
        }

        return l;
    }

    public void delete(E entity) throws HibernateException
    {
        entity.setDeleted(true);

        super.update(entity);
    }

    public final E getById(Integer id) throws HibernateException
    {
        E entity = super.getById(id);

        if (entity.isDeleted())
            throw new HibernateException("entity not found");

        return entity;
    }

    @SuppressWarnings("unchecked")
    protected List<E> toTypeSafeList(List list)
    {
        return (List<E>) list;
    }
}
