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

package org.opencustomer.db.dao.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.system.ListConfigurationVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.BaseDAO;

public final class ListConfigurationDAO extends BaseDAO<ListConfigurationVO> {
    
    private static final Logger log = Logger.getLogger(ListConfigurationDAO.class);

    public final static int SORT_TYPE = 1;

    public final static int SORT_NAME = 2;
    
    public ListConfigurationDAO() throws HibernateException {
        super();
    }

    @Override
    protected Class getEntityClass() {
        return ListConfigurationVO.class;
    }

    public List<ListConfigurationVO> getByTypeAndUser(ListConfigurationVO.Type type, UserVO user) {
        List<ListConfigurationVO> list = new ArrayList<ListConfigurationVO>();

        StringBuilder hql = new StringBuilder();
        hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
        hql.append(" WHERE (e.user = :user or e.user is null) ");
        hql.append(" AND e.type = :type ");
        hql.append(" ORDER BY user desc, e.name asc ");
        Query query = HibernateContext.getSession().createQuery(hql.toString());
        
        query.setString("type", type.toString());
        query.setEntity("user", user);
        
        list = toTypeSafeList(query.list());

        if (log.isDebugEnabled())
            log.debug("found " + list.size()+" list configurations");

        return list;
    }
    
    public ListConfigurationVO getForSystemByTypeAndName(ListConfigurationVO.Type type, String name) {
        ListConfigurationVO vo = null;

        StringBuilder hql = new StringBuilder();
        hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
        hql.append(" WHERE e.user is null ");
        hql.append(" AND e.type = :type ");
        hql.append(" AND e.name = :name ");
        
        Query query = HibernateContext.getSession().createQuery(hql.toString());
        
        query.setString("type", type.toString());
        query.setString("name", name);
        
        vo = (ListConfigurationVO)query.uniqueResult();

        if (log.isDebugEnabled())
            log.debug("found list configuration: "+vo);

        return vo;
    }

    public ListConfigurationVO getForUserByTypeAndName(ListConfigurationVO.Type type, String name, UserVO user) {
        ListConfigurationVO vo = null;

        StringBuilder hql = new StringBuilder();
        hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
        hql.append(" WHERE e.type = :type ");
        hql.append(" AND e.name = :name ");
        hql.append(" AND e.user = :user ");
        
        Query query = HibernateContext.getSession().createQuery(hql.toString());
        
        query.setString("type", type.toString());
        query.setString("name", name);
        query.setEntity("user", user);
        
        vo = (ListConfigurationVO)query.uniqueResult();

        if (log.isDebugEnabled())
            log.debug("found list configuration: "+vo);

        return vo;
    }

    public void resetDefaultValue(ListConfigurationVO.Type type, boolean global) {
        StringBuffer hql = new StringBuffer();
        
        hql.append(" UPDATE ").append(getEntityClass().getName()).append(" ");
        hql.append(" SET default = 0 ");
        hql.append(" WHERE type = :type ");
        if(global)
            hql.append(" AND user is null ");
        else
            hql.append(" AND user is not null ");
    
        Query query = HibernateContext.getSession().createQuery(hql.toString());
        query.setString("type", type.toString());
        
        query.executeUpdate();
    }
    
    public ListConfigurationVO getDefaultListConfiguration(ListConfigurationVO.Type type, UserVO user) {
        ListConfigurationVO vo = null;

        StringBuilder hql = new StringBuilder();
        hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
        hql.append(" WHERE e.type = :type ");
        hql.append(" AND (e.user = :user OR e.user is null)");
        hql.append(" ORDER BY default desc, user desc, name asc ");
        
        Query query = HibernateContext.getSession().createQuery(hql.toString());

        query.setString("type", type.toString());
        query.setEntity("user", user);

        query.setMaxResults(1);
        
        vo = (ListConfigurationVO)query.uniqueResult();

        if (log.isDebugEnabled())
            log.debug("found default list configuration: "+vo);

        return vo;
    }
}
