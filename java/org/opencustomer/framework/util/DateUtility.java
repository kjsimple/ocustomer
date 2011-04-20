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

package org.opencustomer.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtility
{
    private DateUtility(){
    }

    public final static boolean isBetween(Date start, Date end, Date test)
    {
        boolean valid = false;

        if (!start.after(test) && !end.before(test))
            valid = true;

        return valid;
    }

    public final static boolean isMatching(Date start, Date end, Date testStart, Date testEnd)
    {
        boolean valid = false;

        if (DateUtility.isBetween(start, end, testStart) 
                || DateUtility.isBetween(start, end, testEnd) 
                || (!testStart.after(start) && !testEnd.before(end)))
            valid = true;

        return valid;
    }

    public final static Date getEndOfDay(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.SECOND, -1);
        return cal.getTime();
    }
    
    public final static String adjustDate(String format, String value) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        
        Date date = sdf.parse(value);
        
        Calendar cal = GregorianCalendar.getInstance();
        
        cal.setTime(date);
        if(cal.get(Calendar.YEAR) < 100)
            cal.add(Calendar.YEAR, 2000);
        
        return sdf.format(cal.getTime());
    }
    
    public final static Calendar getCalendar(Date date, int firstDayOfWeek) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setFirstDayOfWeek(firstDayOfWeek);
        cal.setTime(date);
        
        return cal;
    }
}
