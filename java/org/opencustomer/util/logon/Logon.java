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

package org.opencustomer.util.logon;

import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.system.UserVO;

public abstract class Logon {
    
    public static enum Type {
        WEBAPP,
        WEBDAV,
        WEBSERVICE;
    }
    
    public final UserVO validate(String userName, String password, Type type, ActionMessages errors) {
        return this.validate(userName, password, null, type, errors);
    }

    public abstract UserVO validate(String userName, String password, String ipAddress, Type type, ActionMessages errors);
}
