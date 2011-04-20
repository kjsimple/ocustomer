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

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.vo.UndeletableVO;

public abstract class UndeletableDAO<E extends UndeletableVO> extends VersionedDAO<E>
{
    private static final Logger log = Logger.getLogger(UndeletableDAO.class);

    public UndeletableDAO() throws HibernateException
    {
        super();
    }

    public void delete(E entity, Integer userId) throws HibernateException
    {
        HibernateContext.beginTransaction();
        
        entity.setDeleted(true);
        
        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" update ").append(getEntityClass().getName()).append(" set ");
            hql.append(" deleted = 1, ");
            hql.append(" modifyUser = :modifyUser, ");
            hql.append(" modifyDate = :modifyDate ");
            hql.append(" where id = :id ");        

            int updatedEntities = HibernateContext.getSession().createQuery(hql.toString())
                 .setInteger("modifyUser", entity.getModifyUser())
                 .setTimestamp("modifyDate", entity.getModifyDate())
                 .setInteger("id", entity.getId())
                 .executeUpdate();

            HibernateContext.commitTransaction();
        }
        catch (HibernateException e)
        {
            HibernateContext.rollbackTransaction();
            
            throw e;
        }
        
    }

    public void delete(E entity) throws HibernateException
    {
        this.delete(entity, -1);
    }
}
