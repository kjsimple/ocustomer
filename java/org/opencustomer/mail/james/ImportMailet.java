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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;

import org.apache.log4j.Logger;
import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;
import org.apache.mailet.MailetContext;
import org.opencustomer.db.dao.crm.ContactDAO;
import org.opencustomer.db.dao.crm.PersonContactDAO;
import org.opencustomer.db.dao.crm.PersonDAO;
import org.opencustomer.db.vo.crm.ContactVO;
import org.opencustomer.db.vo.crm.PersonContactVO;
import org.opencustomer.db.vo.crm.PersonVO;

/**
 * 
 * @author fbreske
 *
 */
public class ImportMailet extends GenericMailet
{
    private final static Logger log = Logger.getLogger(ImportMailet.class);
    
    private ContactVO contact;

    private boolean contentTypetext = false;

    public void init()
    {
        if(log.isInfoEnabled())
            log.info("Initializing Mailet");
    }

    public void service(Mail mail)
    {
        try
        {
            if(log.isInfoEnabled())
                log.info("import mail from " + mail.getSender());
            String recipient;
            PersonContactVO pc;
            List<PersonContactVO> pcs = new ArrayList<PersonContactVO>();

            MimeMessage message = mail.getMessage();
            MailetContext mailetContext = getMailetContext();
            contact = new ContactVO();

            contact.setBoundType(ContactVO.BoundType.IN);
            contact.setContactType(ContactVO.ContactType.EMAIL);
            contact.setContactTimestamp(new Date());
            contact.setImportType(ContactVO.ImportType.NEW);

            // Subject setzen
            if (message.getSubject() != null)
                contact.setSubject(message.getSubject());
            else
                contact.setSubject("Kein Betreff angegeben");

            // Content Setzen
            insertContent(message);
            if (contentTypetext)
                contact.setContentType(ContactVO.ContentType.PLAINTEXT);
            else
                contact.setContentType(ContactVO.ContentType.HTML);

            // Absender
            List<PersonVO> senders = new PersonDAO().getByMail(mail.getSender().toString());
            if (!senders.isEmpty())
                for (PersonVO person : senders)
                {
                    pc = new PersonContactVO(person, contact);
                    pc.setRelationType(PersonContactVO.Type.SENDER);
                    pcs.add(pc);
                    if ((mailetContext.isLocalUser(mail.getSender().getUser())) && mailetContext.isLocalServer(mail.getSender().getHost()))
                    {
                        contact.setBoundType(ContactVO.BoundType.OUT);
                    }
                }
            else
                contact.setContactName("From: " + mail.getSender().toString());

            // Emfaenger TO
            Address[] addresses = message.getRecipients(MimeMessage.RecipientType.TO);
            for (Address address : addresses)
            {
                recipient = address.toString();
                List<PersonVO> recipients = new PersonDAO().getByMail(recipient);
                if (!recipients.isEmpty())
                    for (PersonVO person : recipients)
                    {
                        pc = new PersonContactVO(person, contact);
                        pc.setRelationType(PersonContactVO.Type.TO);
                        pcs.add(pc);
                    }
                else if (contact.getContactName() == null)
                    contact.setContactName("To: " + recipient);
                else
                    contact.setContactName(contact.getContactName() + ", To: " + recipient);
            }

            // Emfaenger CC
            addresses = message.getRecipients(MimeMessage.RecipientType.CC);
            if (addresses != null)
                for (Address address : addresses)
                {
                    recipient = address.toString();
                    List<PersonVO> recipients = new PersonDAO().getByMail(recipient);
                    if (!recipients.isEmpty())
                        for (PersonVO person : recipients)
                        {
                            pc = new PersonContactVO(person, contact);
                            pc.setRelationType(PersonContactVO.Type.CC);
                            pcs.add(pc);
                        }
                    else if (contact.getContactName() == null)
                        contact.setContactName("CC: " + recipient);
                    else
                        contact.setContactName(contact.getContactName() + ", CC: " + recipient);
                }

            // Emfaenger BCC
            addresses = message.getRecipients(MimeMessage.RecipientType.BCC);
            if (addresses != null)
                for (Address address : addresses)
                {
                    recipient = address.toString();
                    List<PersonVO> recipients = new PersonDAO().getByMail(recipient);
                    if (!recipients.isEmpty())
                        for (PersonVO person : recipients)
                        {
                            pc = new PersonContactVO(person, contact);
                            pc.setRelationType(PersonContactVO.Type.BCC);
                            pcs.add(pc);
                        }
                    else if (contact.getContactName() == null)
                        contact.setContactName("BCC: " + recipient);
                    else
                        contact.setContactName(contact.getContactName() + ", BCC: " + recipient);
                }

            // Datenbank Eintrag
            if (!pcs.isEmpty())
            {
                new ContactDAO().insert(contact);
                for (PersonContactVO pce : pcs)
                {
                    pce.setContact(contact);
                    new PersonContactDAO().insert(pce);
                }
                mail.getMessage().addHeader("Filter: ", "james2oc"); // header
                                                                        // setzten
                mail.setAttribute("james2oc", true); // mailattribut setzen
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    public String getMailetInfo()
    {
        return "James to Opencustomer Mailet";
    }

    public void destroy()
    {
    }

    private void insertContent(MimePart part) throws MessagingException, IOException
    {
        boolean rc = false;
        String disposition = part.getDisposition();
        if (disposition == null)
        {
            if (part.isMimeType("text/plain"))
            {
                contact.setContent(part.getContent().toString());
                contentTypetext = true;
            }
            else if (part.isMimeType("text/html"))
            {
                if (!contentTypetext)
                    contact.setContent(part.getContent().toString());
            }
            else if (part.isMimeType("multipart/*"))
            {
                MimeMultipart multipart = (MimeMultipart) part.getContent();
                for (int i = 0; i < multipart.getCount(); i++)
                {
                    MimeBodyPart mimeBodyPart = (MimeBodyPart) multipart.getBodyPart(i);
                    insertContent(mimeBodyPart);
                }
            }
            else
            {
                System.out.println("Unbekannter ContentType");
            }
        }
        else
        {
            if (disposition.equals(Part.ATTACHMENT) || disposition.equals(Part.INLINE))
                System.out.println("Anhang gefunden: " + part.getFileName());
            else
                System.out.println("Unbekannter ContentType");
        }
    }
}
