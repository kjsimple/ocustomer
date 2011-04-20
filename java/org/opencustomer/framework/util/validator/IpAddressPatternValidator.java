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
package org.opencustomer.framework.util.validator;

public final class IpAddressPatternValidator implements Validator
{
    private static String WILDCARD_PATTERN = 
        DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET +
        "|" +
        DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\.\\*{1}" +
        "|" +
        DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\.\\*{1}" +
        "|" +
        DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET +
        "|" +
        DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\.\\*{1}" +
        "|" +
        DomainValidator.OCTET+"\\."+DomainValidator.OCTET+"\\.\\*{1}" +
        "|" +
        DomainValidator.OCTET+"\\.\\*{1}" +
        "|" +
        "\\*{1}";

    private static IpAddressPatternValidator instance;
    
    public static IpAddressPatternValidator getInstance() {
        if(instance == null)
            instance = new IpAddressPatternValidator();
        
        return instance;
    }
    
    public boolean validate(String humanPattern) {
        boolean isValid = false;
        
        if(humanPattern != null) {
            isValid = humanPattern.matches(WILDCARD_PATTERN);
        }
        
        return isValid;
    }

    public boolean match(String pattern, String ipAddress)
    {
        boolean isValid = false;
        
        if(ipAddress != null)
        {
            if(validate(pattern))
                return ipAddress.matches(createPattern(pattern));
            else
                return false;
        }
        
        return isValid;
    }
    
    private String createPattern(String humanPattern) {
        StringBuilder ipv4Pattern = new StringBuilder();
        StringBuilder ipv6Pattern = new StringBuilder();
        
        int blocks = 0;
        for(String part : humanPattern.split("\\."))
        {
            if(part.matches(DomainValidator.OCTET))
            {
                if(blocks > 0){
                    ipv4Pattern.append("\\.");
                    ipv6Pattern.append("\\.");
                }

                ipv4Pattern.append(part);
                ipv6Pattern.append(part);

                blocks++;
            }
        }

        fillBlocks(ipv4Pattern, blocks, 4);
        fillBlocks(ipv6Pattern, blocks, 6);
        
        return ipv4Pattern.toString()+"|"+ipv6Pattern;
    }
    
    private static void fillBlocks(StringBuilder pattern, int existingBlocks, int targetBlocks) {
        for(int i=existingBlocks; i<targetBlocks; i++)
        {
            if(i>0) {
                pattern.append("\\.\\d{1,3}");
            } else {
                pattern.append("\\d{1,3}");
            }
        }
    }
    
}
