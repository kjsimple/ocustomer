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
 * Copyright (C) 2006 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.db.dao.system;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.system.ConfigurationVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.VersionedDAO;

public final class ConfigurationDAO extends VersionedDAO<ConfigurationVO> {
    private static final Logger log = Logger.getLogger(ConfigurationDAO.class);
    
    /**
     * @throws DatabaseException
     */
    public ConfigurationDAO() throws HibernateException {
        super();
    }

    protected Class getEntityClass() {
        return ConfigurationVO.class;
    }
    
    public List<ConfigurationVO> getForUser(UserVO user) throws HibernateException {
        List<ConfigurationVO> list = null;
        try {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.user = :user ");
            hql.append(" ORDER BY key, subKey ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            query.setEntity("user", user);
            
            list = toTypeSafeList(query.list());
        } catch (HibernateException e) {
            log.error("could load configuration for user "+user, e);
            throw e;
        }

        return list;
    }
    
    public List<ConfigurationVO> getForSystem() throws HibernateException {
        List<ConfigurationVO> list = null;
        try {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.user is null ");
            hql.append(" ORDER BY key, subKey ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            
            list = toTypeSafeList(query.list());
        } catch (HibernateException e) {
            log.error("could load configuration for system ", e);
            throw e;
        }

        return list;
    }
}
