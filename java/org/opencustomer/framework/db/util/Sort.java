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

package org.opencustomer.framework.db.util;

import javax.servlet.http.HttpServletRequest;

import org.opencustomer.framework.webapp.util.RequestUtility;

public final class Sort
{
    private static final String ASC_INNER = "asc";

    public static final String ASC = RequestUtility.DIVIDER + ASC_INNER;

    private static final String DESC_INNER = "desc";

    public static final String DESC = RequestUtility.DIVIDER + DESC_INNER;

    private int field;

    private boolean ascending;

    public Sort()
    {

    }

    public Sort(int field, boolean ascending)
    {
        this.field = field;
        this.ascending = ascending;
    }

    public final boolean isAscending()
    {
        return ascending;
    }

    public final void setAscending(boolean ascending)
    {
        this.ascending = ascending;
    }

    public final int getField()
    {
        return field;
    }

    public final void setField(int field)
    {
        this.field = field;
    }

    public String toString()
    {
        StringBuffer buff = new StringBuffer();

        buff.append("Sort [");
        buff.append("field=").append(field);
        buff.append(", ascending=").append(ascending);
        buff.append("]");

        return buff.toString();
    }

    public static Sort parseParam(String param)
    {
        Sort sort = new Sort();

        if (param != null)
        {
            int pos = param.indexOf(RequestUtility.DIVIDER);

            if (pos >= 0)
            {
                // auswerten der Zahl
                try
                {
                    sort.setField(Integer.parseInt(param.substring(0, pos)));
                }
                catch (NumberFormatException e)
                {
                }

                // auswerten der Sortierung
                if (DESC_INNER.equalsIgnoreCase(param.substring(pos + 1)))
                    sort.setAscending(false);
                else
                    sort.setAscending(true);
            }
        }

        if (sort.getField() <= 0)
            return null;
        else
            return sort;
    }

    public final static String parseRequest(HttpServletRequest request, String param)
    {
        return RequestUtility.parseRequest(request, param, "sort");
    }
}
