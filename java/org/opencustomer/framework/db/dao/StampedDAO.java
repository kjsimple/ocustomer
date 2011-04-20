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
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.vo.StampedVO;

/**
 * @author chaenderl
 * 
 */
public abstract class StampedDAO<E extends StampedVO> extends BaseDAO<E>
{
    private static final Logger log = Logger.getLogger(StampedDAO.class);

    public StampedDAO() throws HibernateException
    {
        super();
    }

    public void insert(E entity, UserVO user) throws HibernateException
    {
        HibernateContext.setAttribute("user", user);
        
        super.insert(entity);

        HibernateContext.removeAttribute("user");
    }

    public void update(E entity, UserVO user) throws HibernateException
    {
        HibernateContext.setAttribute("user", user);
        
        super.update(entity);

        HibernateContext.removeAttribute("user");
    }

    public final void insertOrUpdate(E entity, UserVO user)
    {
        HibernateContext.setAttribute("user", user);
        
        super.insertOrUpdate(entity);

        HibernateContext.removeAttribute("user");        
    }
}
