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

package org.opencustomer.connector.mail.messages;

import org.opencustomer.connector.mail.Email;

/**
 * This class is a normal email.
 * @author fbreske
 *
 */
public class DefaultEmail extends Email
{
    private String textMessageBody;
    
    private String htmlMessageBody;

    /**
     * Sets the Html Message Body (should be Html sourcecode)
     * @param htmlMessageBody the Html sourcecode
     */
    public void setHtmlMessageBody(String htmlMessageBody)
    {
        this.htmlMessageBody = htmlMessageBody;
    }

    /**
     * Sets the text Message Body.
     * @param textMessageBody
     */
    public void setTextMessageBody(String textMessageBody)
    {
        this.textMessageBody = textMessageBody;
    }

    /**
     * Overrides the getHtmlMessageBody from Email.
     * @return the Html sourcecode of the email.
     */
    @Override
    public String getHtmlMessageBody()
    {
        return htmlMessageBody;
    }

    /**
     * Overrides the getTextMessageBody from Email.
     * @return the text of the email.
     */
    @Override
    public String getTextMessageBody()
    {
        return textMessageBody;
    }
}
