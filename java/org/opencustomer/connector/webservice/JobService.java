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
 *                 Felix Breske <felix.breske@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.connector.webservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.opencustomer.db.dao.crm.JobDAO;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.util.logon.LdapLogon;
import org.opencustomer.util.logon.LocalLogon;
import org.opencustomer.util.logon.Logon;
import org.opencustomer.webapp.auth.Authenticator;
import org.opencustomer.webapp.auth.Right;

/**
 * This class is a webservice to read the jobs for a user from opencustomer.
 * The webservice is used by the opencustomer client.
 * @author fbreske
 *
 */
public class JobService
{
    private static Logger log = Logger.getLogger(JobService.class);
    
    public JobService()
    {   
    }

    /**
     * This method reads the jobs for a user, and return a list of actitvities.<br>
     * The webservice saves the job informations in a HashMap.<br>
     * Content of the Hashmap:<br>
     * <table>
     * <tr><th>HashMap key</th><th>JobVO entity</th></tr>
     * <tr><td>ID</td><td>job.getId()</td></tr>
     * <tr><td>info</td><td>job.getInfo()</td></tr>
     * <tr><td>subject</td><td>job.getSubject()</td></tr>
     * <tr><td>date</td><td>job.getDueDate()</td></tr>
     * <tr><td>status</td><td>job.getStatus().toString()</td></tr>
     * <tr><td>deeplink</td><td>NOT SUPPORTED NOW</td></tr>
     * </table>
     * @param user the username
     * @param password the password of the user
     * @param startDate the start date of the time period
     * @param endDate the end date of the time period
     * @return a List of HashMaps
     */
    public List<HashMap> getJobs(String user, String password, Date startDate, Date endDate)
    {
        ArrayList<HashMap> jobList = null;

        Logon logon;
        
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_AUTHENTICATION_ENABLED)){
            if(log.isDebugEnabled())
                log.debug("do local login");
            logon = new LocalLogon();
        }     
        else
        {
            if(log.isDebugEnabled())
                log.debug("do ldap login");
            logon = new LdapLogon();          
        }
        
        ActionErrors errors = new ActionErrors();
        UserVO uservo = logon.validate(user, password, Logon.Type.WEBSERVICE, errors);
        
        if(uservo != null && errors.isEmpty())
        {       
            Authenticator auth = new Authenticator(uservo);
            if(auth.isValid(Right.EXTERN_WEBSERVICE_READ))
            {
                jobList = new ArrayList<HashMap>();
                List<JobVO> jobs = new JobDAO().getByUser(uservo, startDate, endDate);         
                HashMap<String,String> jobMap;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                
                for(JobVO job : jobs)
                {
                    jobMap = new HashMap<String,String>();
                    jobMap.put("id",Integer.toString(job.getId()));
                    jobMap.put("info",job.getInfo());
                    jobMap.put("subject",job.getSubject());
                    if(job.getDueDate() != null)
                        jobMap.put("date",sdf.format(job.getDueDate()));
                    jobMap.put("status",job.getStatus().toString());
                    jobMap.put("deeplink","http://localhost:8080/opencustomer/login.do"); //TODO: read URL from settings
                    
                    jobList.add(jobMap);
                }
            }
            else
            {
            if(log.isInfoEnabled())
                log.info("client access denied for username [" + user + "]");
            }
        }
        else
        {
            if(log.isInfoEnabled())
                log.info("user not found");
        }       

        return jobList;
    }
    
    /**
     * This method changes the status of the job.
     * @param user the username
     * @param password the password of the user
     * @param id the id of the JobVO to change
     * @param status the new status of the job
     * @return true if successfull otherwise false
     */
    public boolean setJobStatus(String user, String password, int id, String status)
    {
        Logon logon;
        
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_AUTHENTICATION_ENABLED)){
            if(log.isDebugEnabled())
                log.debug("do local login");
            logon = new LocalLogon();
        }     
        else
        {
            if(log.isDebugEnabled())
                log.debug("do ldap login");
            logon = new LdapLogon();          
        }
        
        ActionErrors errors = new ActionErrors();
        UserVO uservo = logon.validate(user, password, Logon.Type.WEBSERVICE, errors);
        
        if(uservo != null && errors.isEmpty())
        {
            Authenticator auth = new Authenticator(uservo);
            if(auth.isValid(Right.EXTERN_WEBSERVICE_WRITE))
            {
                JobVO job = new JobDAO().getById(id);
                if(status.equals(JobVO.Status.COMPLETED.toString()))
                    job.setStatus(JobVO.Status.COMPLETED);
                else if(status.equals(JobVO.Status.IN_PROGRESS.toString()))
                    job.setStatus(JobVO.Status.IN_PROGRESS);
                else if(status.equals(JobVO.Status.PLANNED.toString()))
                    job.setStatus(JobVO.Status.PLANNED);
                new JobDAO().insertOrUpdate(job);
            }
            else
            {
            if(log.isInfoEnabled())
                log.info("client access denied for username [" + user + "]");
            return false;
            }
        }
        else
        {
            if(log.isInfoEnabled())
                log.info("user not found");
            return false;
        }
        return true;
    }
}
