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

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.BaseDAO;

public final class JobDAO extends BaseDAO<JobVO>
{
    
    private static final Logger log = Logger.getLogger(JobDAO.class);
    
    /**
     * @throws DatabaseException
     */
    public JobDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return JobVO.class;
    }
    
    /**
     * Searches for JobVO at the given time period.
     * @param user the user
     * @param startDate the start date of the time period
     * @param endDate the end date if the time period
     * @return a List of JobVO found for the given time period
     * @throws HibernateException if there is an error on the Hibernate connection
     * @author fbreske
     */
    public List<JobVO> getByUser(UserVO user , Date startDate, Date endDate) throws HibernateException
    {
        
        List<JobVO> l = null;
        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.assignedUser = :assignedUser ");
            hql.append(" AND e.status != '" + JobVO.Status.COMPLETED + "' ");
            
            if(startDate != null && endDate != null)
            {
                hql.append(" AND (e.dueDate BETWEEN :startDate AND :endDate) ");
            }
            
            Query query = HibernateContext.getSession().createQuery(hql.toString());
            query.setEntity("assignedUser", user);
            if(startDate != null && endDate != null)
            {
                query.setTimestamp("startDate",startDate);
                query.setTimestamp("endDate", endDate);
            }
            
            l = toTypeSafeList(query.list());
        }
        catch (HibernateException e)
        {
            log.error("error loading jobs", e);
            throw e;
        }
        return l;
    }   
}
