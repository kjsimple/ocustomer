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
import org.opencustomer.framework.db.vo.BaseVO;

/**
 * @author chaenderl
 * 
 */
public abstract class BaseDAO<E extends BaseVO> extends AbstractDAO
{
    private static final Logger log = Logger.getLogger(BaseDAO.class);
    
    public BaseDAO() throws HibernateException
    {
        super();
    }
    
    public void insert(E entity) throws HibernateException
    {
        if (log.isDebugEnabled())
            log.debug("Try to insert entity: " + entity.toString());

        boolean newTrx = false;
        
        try 
        {
            newTrx = HibernateContext.beginTransaction();
            
            HibernateContext.getSession().save(entity);
            
            if(newTrx)
                HibernateContext.commitTransaction();
        }
        catch(HibernateException e) 
        {
            if(newTrx)
                HibernateContext.rollbackTransaction();
            
            throw e;
        }
        
    }

    public void update(E entity) throws HibernateException
    {
        if (log.isDebugEnabled())
            log.debug("Try to update object: " + entity.toString());

        boolean newTrx = false;
        
        try 
        {
            newTrx = HibernateContext.beginTransaction();
            
            HibernateContext.getSession().update(entity);
            
            if(newTrx)
                HibernateContext.commitTransaction();
        }
        catch(HibernateException e) 
        {
            if(newTrx)
                HibernateContext.rollbackTransaction();
            
            throw e;
        }
    }

    public void insertOrUpdate(E entity) throws HibernateException
    {
        if (log.isDebugEnabled())
            log.debug("Try to insert/update object: " + entity.toString());

        boolean newTrx = false;
        
        try 
        {
            newTrx = HibernateContext.beginTransaction();
            
            HibernateContext.getSession().saveOrUpdate(entity);
            
            if(newTrx)
                HibernateContext.commitTransaction();
        }
        catch(HibernateException e) 
        {
            if(newTrx)
                HibernateContext.rollbackTransaction();
            
            throw e;
        }
    }

    public void delete(E entity) throws HibernateException
    {
        if (log.isDebugEnabled())
            log.debug("Try to delete object: " + entity.toString());

        boolean newTrx = false;
        
        try 
        {
            newTrx = HibernateContext.beginTransaction();
            
            HibernateContext.getSession().delete(entity);
            
            if(newTrx)
                HibernateContext.commitTransaction();
        }
        catch(HibernateException e) 
        {
            if(newTrx)
                HibernateContext.rollbackTransaction();

            throw e;
        }
    }

    protected abstract Class getEntityClass();

    @SuppressWarnings("unchecked")
    public E getById(Integer id) throws HibernateException
    {
        E entity = null;

        try
        {
            entity = (E) HibernateContext.getSession().get(getEntityClass(), id);

            if (entity == null)
                throw new HibernateException("entity not found");
        }
        catch (HibernateException e)
        {
            log.error("Could not get object by id '" + id.toString() + "'", e);
            throw e;
        }

        return entity;
    }

    public List<E> getAll() throws HibernateException
    {
        List<E> l = null;
        try
        {
            StringBuffer hql = new StringBuffer();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");

            l = toTypeSafeList(HibernateContext.getSession().createQuery(hql.toString()).list());
        }
        catch (HibernateException e)
        {
            log.error("Could not get all objects", e);
            throw e;
        }

        return l;
    }
    
    @SuppressWarnings("unchecked")
    protected List<E> toTypeSafeList(List list)
    {
        return list;
    }
}
