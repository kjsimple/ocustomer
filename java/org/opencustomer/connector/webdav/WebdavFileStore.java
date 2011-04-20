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

package org.opencustomer.connector.webdav;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.ValidationException;

import org.apache.commons.transaction.util.LoggerFacade;
import org.apache.log4j.Logger;
import org.apache.slide.common.Service;
import org.apache.slide.common.ServiceAccessException;
import org.apache.slide.security.AccessDeniedException;
import org.apache.slide.simple.store.BasicWebdavStore;
import org.apache.slide.structure.ObjectNotFoundException;
import org.opencustomer.connector.ical.Ical;
import org.opencustomer.db.dao.calendar.CalendarDAO;
import org.opencustomer.db.dao.calendar.EventCalendarDAO;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.webapp.auth.Authenticator;
import org.opencustomer.webapp.auth.Right;

/**
 * The class WebdavFileStore implements the BasicWEbdavStore and handels the Webdav access for OpenCustomer.
 * @author fbreske
 *
 */
public class WebdavFileStore implements BasicWebdavStore {

    private static LoggerFacade logger = null;
    private static Service service = null;
    private static Logger log = Logger.getLogger(WebdavFileStore.class);
    
    
    private HashMap<String,CalendarVO> calendarmap;
    private UserVO user;

    /**
     * generates the list of available calendars vor the connected user
     */
    public synchronized void begin(Service service, Principal principal, Object connection, LoggerFacade logger,
            Hashtable parameters)
    {        
        if(log.isDebugEnabled())
            log.debug("initializing WebdavFileStore");
        WebdavFileStore.logger = logger;
        WebdavFileStore.service = service;
        calendarmap = new HashMap<String,CalendarVO>();
        
        if(principal == null)
            return;
        
        user = new UserDAO().getByUserName(principal.getName());
        if(log.isDebugEnabled())
            log.debug("found user " + user);
        
        List<CalendarVO> calendars = new CalendarDAO().getAvailableCalendars(user);
        for(CalendarVO calendar : calendars)
        {
            if(!new EventCalendarDAO().getByCalendar(calendar, false).isEmpty())
                calendarmap.put("/files/" + calendar.getUser().getUserName() + ".ics", calendar);
        }
        if(log.isInfoEnabled())
            log.info("webdav connect for user [" + user.getUserName() + "]");
    }
    
    /**
     * not used
     */
    public void commit(){
    }

    /**
     * not used
     */
    public void rollback(){
    }
    
    /**
     * not used
     */
    public void checkAuthentication(){
    }

    /**
     * @return true if the calendar exists, otherwise false
     */
    public boolean objectExists(String uri)
    {    
        if(uri.equals("/files")){
            return true;
        }
        return calendarmap.containsKey(uri);
    }

    /**
     * @return true for the "/files" folder, otherwise false
     */
    public boolean isFolder(String uri)
    {  
        return uri.equals("/files");
    }

    /**
     * @return true vor the available calendars
     */
    public boolean isResource(String uri)
    { 
        return calendarmap.containsKey(uri);
    }

    /**
     * not allowed, thows always an AccessDebiedException
     */
    public void createFolder(String folderUri) throws AccessDeniedException
    {     
        throw new AccessDeniedException(folderUri, "create folder", "create");
    }

    /**
     * @return the list of available calendars
     */
    public String[] getChildrenNames(String folderUri)
    {   
        if(folderUri.equals("/files"))
        {
            List<String> childList = new ArrayList<String>();
            for(CalendarVO calendar : calendarmap.values())
            {
                childList.add(calendar.getUser().getUserName() + ".ics");
            }
            String[] childrenNames = new String[childList.size()];
            childrenNames = childList.toArray(childrenNames);
            return childrenNames;
        }
        else
            return null;
    }

    /**
     * not allowed, thows always an AccessDeniedException
     */
    public void createResource(String resourceUri) throws AccessDeniedException
    {    
        throw new AccessDeniedException(resourceUri, "create file", "create");
    }

    public void setResourceContent(String resourceUri, InputStream content, String contentType, String characterEncoding) throws AccessDeniedException
    {      
        if(user != null)
        {
            Authenticator auth = new Authenticator(user);
            if(!auth.isValid(Right.EXTERN_WEBDAV_WRITE))
            {
                if(log.isInfoEnabled())
                    log.info("no write rights for user");
                return ;
            }
                
            
            if(calendarmap.containsKey(resourceUri))
            {
                    try
                    {
                        if(log.isInfoEnabled())
                            log.info("calendar import [" + resourceUri + "]");
                        Ical.getInstance().importIcal(content, calendarmap.get(resourceUri), user, Ical.EventHandling.DELETE);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (ParserException e)
                    {
                        e.printStackTrace();
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
            }
        }
    }

    /**
     * @return returns static 10kB, should be the correct size of the calendar
     */
    public long getResourceLength(String resourceUri)
    {     
        return new Long(10000); // TODO: groesse berechnen
    }

    /**
     * generates dynamically the iCalendar files.
     * @return the iCalendar file
     */
    public InputStream getResourceContent(String resourceUri) throws ServiceAccessException, AccessDeniedException,
            ObjectNotFoundException {
        
        if(calendarmap.containsKey(resourceUri))
        {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try
                {
                    if(log.isInfoEnabled())
                        log.info("calendar export [" +resourceUri + "]");
                    
                    Ical.getInstance().exportIcal(calendarmap.get(resourceUri),user,out);
                }
                catch (IOException e)
                {
                    throw new ServiceAccessException(service,e);
                }
                catch (ValidationException e)
                {
                    throw new ServiceAccessException(service,e);
                }
                catch (ParseException e)
                {
                    throw new ServiceAccessException(service,e);
                }
                return new ByteArrayInputStream(out.toByteArray());
        }
        return new StringBufferInputStream("");
   }

    /**
     * not allowd, throws always a AccessDeniedExceltion
     */
    public void removeObject(String uri) throws AccessDeniedException 
    {     
        throw new AccessDeniedException(uri,"", "delete");
    }

    /**
     * return the actual date
     */
    public Date getLastModified(String uri)
    {     
        if(uri.equals("/files"))
            return new Date();
        return new Date();
    }

    /**
     * @return the creation date of the calendar
     */
    public Date getCreationDate(String uri)
    {     
        if(uri.equals("/files"))
            return new Date();
        return calendarmap.get(uri).getCreateDate();
    }
}
