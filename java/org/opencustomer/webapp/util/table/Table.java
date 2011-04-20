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
 * Copyright (C) 2007 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.webapp.util.table;

import org.apache.log4j.Logger;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.util.engine.Restriction;
import org.opencustomer.framework.db.util.engine.TableEngine;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class Table extends org.opencustomer.framework.webapp.util.html.Table {

    private final static Logger log = Logger.getLogger(Table.class);
    
    public Table(TableEngine engine) {
        this(engine, null);
    }

    public Table(TableEngine engine, UserVO user) {
        super(engine);
        
        if(user != null) {
            for(Class entityInterfaces : engine.getConfiguration().getEntity().getClazz().getInterfaces()) {
                if(entityInterfaces.equals(EntityAccess.class)) {
                    if(log.isDebugEnabled())
                        log.debug("add user restriction for: "+user);
                    
                    String alias = engine.getConfiguration().getEntity().getAlias();
                    
                    StringBuilder hql = new StringBuilder();
                    hql.append(" ("+alias+".accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                    hql.append(" OR ("+alias+".accessGroup != '"+EntityAccess.Access.NONE+"' ");
                    hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as internalUser where internalUser.id = {0} and internalUser.profile.usergroups.id = "+alias+".ownerGroup)) ");
                    hql.append(" OR ("+alias+".accessUser != '"+EntityAccess.Access.NONE+"' ");
                    hql.append(" AND "+alias+".ownerUser = {0})) ");
                    
                    boolean groupsAvailable = !engine.getConfiguration().getGroupProperties().isEmpty();
                    
                    this.addRestriction(new Restriction(hql.toString(), groupsAvailable, user.getId()));
                }
            }
        }
    }
    
}
