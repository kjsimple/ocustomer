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
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.db.vo.crm.CompanyVO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.db.vo.crm.JobVO.Priority;
import org.opencustomer.db.vo.crm.JobVO.Status;
import org.opencustomer.db.vo.crm.custom.JobListVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.AbstractDAO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class JobListDAO extends AbstractDAO
{
    private static final Logger log = Logger.getLogger(JobListDAO.class);

    public final static int SORT_SUBJECT = 1;

    public final static int SORT_DUEDATE = 2;

    public final static int SORT_PRIORITY = 3;

    public final static int SORT_STATUS = 4;

    public final static int SORT_ASSIGNEDUSER = 5;
    
    /**
     * @throws DatabaseException
     */
    public JobListDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return JobVO.class;
    }
    
    public List<JobListVO> getList(CompanyVO company, PersonVO person, JobVO job, String subject, Status[] status, Priority priority, Date dateStart, Date dateEnd, boolean assignedUser, UserVO activeUser, Sort sort, Page page, UserVO user)
    {
        String _subject = toLower(adjustWildcards(subject));
        
        List<JobListVO> list = new ArrayList<JobListVO>();
        
        try
        {
            StringBuilder hql = new StringBuilder();
            
            hql.append(" SELECT e.id, e.dueDate, e.status, e.priority, e.subject, u.userName, p.firstName, p.lastName, c.companyName ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" left join e.referencedCompany c ");
            hql.append(" left join e.referencedPerson p ");
            hql.append(" left join e.assignedUser u ");
            hql.append(" WHERE 1=1 ");
            if (company != null)
                hql.append(" AND e.referencedCompany = :company ");
            if (person != null)
                hql.append(" AND e.referencedPerson = :person ");
            if (job != null)
                hql.append(" AND e.parentJob = :job ");
            if (subject != null)
                hql.append(" AND lower(e.subject) like :subject ");
            if (status != null && status.length > 0)
                hql.append(" AND e.status in (:status) ");
            if (priority != null)
                hql.append(" AND e.priority = :priority ");
            if (activeUser != null) {
                if(!assignedUser)
                    hql.append(" AND (e.assignedUser = :activeUser or e.ownerUser = :activeUserId) ");
                else
                    hql.append(" AND e.assignedUser = :activeUser ");
            }
            if (dateStart != null)
                hql.append(" AND e.dueDate >= :dateStart ");
            if (dateEnd != null)
                hql.append(" AND e.dueDate <= :dateEnd ");
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
            if (company != null)
                query.setEntity("company", company);
            if (person != null)
                query.setEntity("person", person);
            if (job != null)
                query.setEntity("job", job);
            if (subject != null)
                query.setString("subject", _subject);
            if (status != null)
                query.setParameterList("status", status);
            if (priority != null)
                query.setString("priority", priority.toString());
            if (activeUser != null) {
                query.setEntity("activeUser", activeUser);
                if(!assignedUser)
                    query.setInteger("activeUserId", activeUser.getId());
            }
            if (dateStart != null)
                query.setTimestamp("dateStart", dateStart);
            if (dateEnd != null)
                query.setTimestamp("dateEnd", dateEnd);
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
                log.debug("found " + list.size() + " jobs");
        }
        catch (HibernateException e)
        {
            log.error("Could not find jobs", e);
            throw e;
        }

        return list;
    }
    
    public long countList(CompanyVO company, PersonVO person, JobVO job, String subject, Status[] status, Priority priority, Date dateStart, Date dateEnd, boolean assignedUser, UserVO activeUser, UserVO user) {

        String _subject = toLower(adjustWildcards(subject));
        
        long count = 0;
        
        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" select count(e.id) ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE 1=1 ");
            if (company != null)
                hql.append(" AND e.referencedCompany = :company ");
            if (person != null)
                hql.append(" AND e.referencedPerson = :person ");
            if (job != null)
                hql.append(" AND e.parentJob = :job ");
            if (subject != null)
                hql.append(" AND lower(e.subject) like :subject ");
            if (status != null && status.length > 0)
                hql.append(" AND e.status in (:status) ");
            if (priority != null)
                hql.append(" AND e.priority = :priority ");
            if (activeUser != null) {
                if(!assignedUser)
                    hql.append(" AND (e.assignedUser = :activeUser or e.ownerUser = :activeUserId) ");
                else
                    hql.append(" AND e.assignedUser = :activeUser ");
            }
            if (dateStart != null)
                hql.append(" AND e.dueDate >= :dateStart ");
            if (dateEnd != null)
                hql.append(" AND e.dueDate <= :dateEnd ");
            if(user != null) {
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (company != null)
                query.setEntity("company", company);
            if (person != null)
                query.setEntity("person", person);
            if (job != null)
                query.setEntity("job", job);
            if (subject != null)
                query.setString("subject", _subject);
            if (status != null)
                query.setParameterList("status", status);
            if (priority != null)
                query.setString("priority", priority.toString());
            if (activeUser != null) {
                query.setEntity("activeUser", activeUser);
                if(!assignedUser)
                    query.setInteger("activeUserId", activeUser.getId());
            }
            if (dateStart != null)
                query.setTimestamp("dateStart", dateStart);
            if (dateEnd != null)
                query.setTimestamp("dateEnd", dateEnd);
            if (user != null)
                query.setInteger("userId", user.getId());

            count = (Long) query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("count " + count + " jobs");
        }
        catch (HibernateException e)
        {
            log.error("Could not find jobs", e);
            throw e;
        }
        
        return count;
    }
    

    private void addEntity(List<JobListVO> list, Object[] columns)
    {
        JobListVO vo = new JobListVO();

        vo.setJobId((Integer) columns[0]);
        vo.setDueDate((Date) columns[1]);
        vo.setStatus((JobVO.Status) columns[2]);
        vo.setPriority((JobVO.Priority) columns[3]);
        vo.setSubject((String) columns[4]);
        vo.setAssignedUserName((String) columns[5]);
        
        StringBuilder reference = new StringBuilder();
        if(columns[6] != null)
            reference.append(columns[6]);
        if(columns[7] != null) {
            if(reference.length() > 0)
                reference.append(" ");
            reference.append(columns[7]);
        }
        if(columns[8] != null) {
            boolean hasPerson = false;
            if(reference.length() > 0) {
                hasPerson = true;
                reference.append(" (");
            }
            reference.append(columns[8]);
            if(hasPerson) 
                reference.append(")");
        }
        if(reference.length() > 0)
            vo.setReference(reference.toString());
        
        list.add(vo);
    }

    protected final String getSortString(Sort sort)
    {
        String orderField = "";

        switch (sort.getField())
        {
            case SORT_SUBJECT:
            default:
                orderField = "e.subject";
                break;
            case SORT_DUEDATE:
                orderField = "e.dueDate";
                break;
            case SORT_PRIORITY:
                orderField = "e.priority";
                break;
            case SORT_STATUS:
                orderField = "e.status";
                break;
            case SORT_ASSIGNEDUSER:
                orderField = "u.userName";
                break;
        }

        if (!sort.isAscending())
            return orderField + " DESC";
        else
            return orderField + " ASC";
    }
}
