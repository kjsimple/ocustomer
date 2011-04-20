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

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.util.engine.TableEngineFactory;
import org.opencustomer.framework.db.util.engine.configuration.Configuration;
import org.opencustomer.framework.db.util.engine.configuration.Property;
import org.opencustomer.framework.webapp.util.html.Table;

public final class TableFactory {

    private final static Logger log = Logger.getLogger(TableFactory.class);
    
    private static TableFactory instance;
    
    private TableEngineFactory engineFactory;
    
    private TableFactory() {
        engineFactory = new TableEngineFactory(new File(TableFactory.class.getClassLoader().getResource(TableFactory.class.getPackage().getName().replace(".", "/")).getFile()));
    }

    public static TableFactory getInstance() {
        if(instance == null) {
            instance = new TableFactory();
        }
        
        return instance;
    }
    
    public Configuration getConfiguration(String name) {
        return engineFactory.getConfiguration(name);
    }
    
    public List<Property> getTableConfiguration(String name) {
        return engineFactory.getEngine(name).getConfiguration().getProperties();
    }
    
    public Table createTable(String name) {
        return createTable(name, null, (Integer[])null);
    }
    
    public Table createTable(String name, Integer... columns) {
        return createTable(name, null, columns);
    }
    
    public Table createTable(String name, UserVO user, Integer... columns) {
        return new org.opencustomer.webapp.util.table.Table(engineFactory.getEngine(name, columns), user);
    }
}