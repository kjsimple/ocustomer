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

package org.opencustomer.mail.james;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.apache.mailet.GenericMatcher;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;
import org.opencustomer.db.dao.crm.PersonDAO;

/**
 * 
 * @author fbreske
 *
 */
public class ImportMatcher extends GenericMatcher
{
    private final static Logger log = Logger.getLogger(ImportMatcher.class);
    private Collection adresses;

    private Set<String> blacklist;

    public void init() throws MessagingException
    {
        if(log.isInfoEnabled())
            log.info("Initializing Matcher (Condition:" + getCondition() + ")");
       
        try{
            blacklist = new HashSet<String>();
            if(getCondition() !=null )
            {
                StringTokenizer st = new StringTokenizer(getCondition(), ", \t", false);
                while (st.hasMoreTokens())
                {
                    blacklist.add(st.nextToken());
                }
            }
        }catch (Throwable e)
        {
            e.printStackTrace(System.out);   
            throw new MessagingException("problems initializing");
        }
    }

    public Collection match(Mail mail) throws MessagingException
    {
        try{
            if(log.isInfoEnabled())
                log.info("matching mail from " + mail.getSender());
            
            boolean rcnfound = true;
    
            // Blacklist
            boolean found = false;
            Collection<MailAddress> recipients = mail.getRecipients();
            Collection<MailAddress> addresses = new HashSet<MailAddress>();
            addresses.addAll(recipients);
            addresses.add(mail.getSender());
            
            if(!blacklist.isEmpty())
            {
                for(MailAddress address : addresses)
                {
                    Iterator bit = blacklist.iterator();
                    found = false;
                    String addressitem = address.toString();
                    for(String pattern : blacklist)
                    {
                        if (addressitem.contains(pattern))
                        {
                            if(log.isInfoEnabled())
                                log.info("blacklist contains " + addressitem);
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        break;
                } 
                if (found)
                {
                    if(log.isInfoEnabled())
                        log.info("all addresses in blacklist");
                    return null;
                }
            }else
            {
                if(log.isInfoEnabled())
                    log.info("blacklist is empty");
            }
            
            // Emfaenger im OC
            if (new PersonDAO().getByMail(mail.getSender().toString()).isEmpty())
            {
                if(log.isInfoEnabled())
                    log.info("sender " + mail.getSender() + " not in database");
                return null;
            }
            
            // Absender im OC
            for (MailAddress address : recipients)
            {
                if (!(new PersonDAO().getByMail(address.toString()).isEmpty()))
                    rcnfound = false;
            }
            
            if (rcnfound)
            {
                if(log.isInfoEnabled())
                    log.info("recipients not in database");
                return null; 
            }
            
            if(log.isInfoEnabled())
                log.info("matching successfull");
            return mail.getRecipients();
        }catch (Throwable e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void destroy()
    {
    }
}
