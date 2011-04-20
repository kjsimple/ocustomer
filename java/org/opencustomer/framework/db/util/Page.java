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


public final class Page {
    long step;

    long page;

    /**
     * Creates a new instance.
     * @param step the number of entries on a page.
     * @param page the page number.
     */
    public Page(long step, long page) {
        this.setStep(step);
        this.setPage(page);
    }

    public final long getPage() {
        return page;
    }

    public final void setPage(long page) {
        if (page < 1)
            this.page = 1;
        else
            this.page = page;
    }

    public final long getStep()
    {
        return step;
    }

    public final void setStep(long step) {
        this.step = step;
    }

    public final long getFirstEntry() {
        return getLastEntry() - step + 1;
    }

    public final long getLastEntry() {
        return step * page;
    }

}
