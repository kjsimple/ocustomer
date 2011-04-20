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

package org.opencustomer.webapp.util.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ModuleConfig;
import org.opencustomer.webapp.auth.Authenticator;
import org.opencustomer.webapp.auth.Right;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class MenuFactory
{
    private final static Logger log = Logger.getLogger(MenuFactory.class);
    
    private Menu menu;
    
    private ModuleConfig moduleConfig;

    
    public MenuFactory(ModuleConfig moduleConfig) throws MenuFactoryException {
        this.moduleConfig = moduleConfig;

        InputStream in = null;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream("org/opencustomer/webapp/util/menu/menu.xml");
            
            initMenu(in);
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch(IOException e) {
                    log.error("could not close stream", e);
                }
            }
        }

    }
    
    private MenuFactory(ModuleConfig moduleConfig, InputStream in) throws MenuFactoryException {
        this.moduleConfig = moduleConfig;
        
        initMenu(in);
    }
    
    private MenuFactory(ModuleConfig moduleConfig, File confFile) throws FileNotFoundException, MenuFactoryException {
        this.moduleConfig = moduleConfig;
        
        FileInputStream in = null;
        try {
            in = new FileInputStream(confFile);
            initMenu(in);
        } finally {
            if(in != null) {
                try {
                  in.close();  
                } catch(IOException e) {
                    log.error("could not close stream", e);
                }
            }
        }
    }
    
    private void initMenu(InputStream in) throws MenuFactoryException {
        if(log.isDebugEnabled())
            log.debug("init menu");
        
        menu = load(in);
        
        debugMenu(menu);
    }
    
    private static void debugMenu(Menu menu) {
        if(log.isDebugEnabled()) {
            for(MenuItem item : menu.getItems()) {
                log.debug(item);
                for(MenuItem subItem : item.getChildItems()) {
                    log.debug("   "+subItem);
                    for(MenuItem subItem2 : subItem.getChildItems()) {
                        log.debug("      "+subItem2);
                    }
                }
            }
        }
    }
    
    public Menu getCustomizedMenu(Authenticator auth) {
        if(log.isDebugEnabled())
            log.debug("customize menu");

        Menu customizedMenu = (Menu)menu.clone();

        if(auth != null)
            customize(customizedMenu.getItems(), auth);

        customizedMenu.initialize();        
        
        debugMenu(customizedMenu);

        return customizedMenu;
    }
    
    private static void customize(List<MenuItem> items, Authenticator auth) {
        
        Iterator<MenuItem> itemsIt = items.iterator();
        while(itemsIt.hasNext()) {
            MenuItem item = itemsIt.next();
            
            customize(item.getChildItems(), auth);
            
            if (item.getChildItems().isEmpty()) {
                if (!auth.isValid(item.getRights())) {
                    if (log.isDebugEnabled())
                        log.debug("remove item with invalid rights: " + item);

                    itemsIt.remove();
                }
            }
        }
    }
    
    private Menu load(InputStream in) throws MenuFactoryException {
        if(log.isDebugEnabled())
            log.debug("load menu from xml");
        
        Menu menu = new Menu();
        
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(in);
            Node root = document.getDocumentElement();
            if("menu".equals(root.getNodeName())) {
            
                NodeList nodes = root.getChildNodes();
                for(int i=0; i<nodes.getLength(); i++) {
                    if(nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        menu.getItems().add(loadMenuItem(nodes.item(i), null));
                    }
                }
            } else {
                throw new MenuFactoryException("invalid attribute found: '"+root.getNodeName()+"' (need: 'menu')");
            }
        } catch(Exception e) {
            throw new MenuFactoryException("could not load menu", e);
        }
        
        return menu;
    }
    
    private MenuItem loadMenuItem(Node node, MenuItem parentItem) throws MenuFactoryException {
        MenuItem item = null;

        if("menuitem".equals(node.getNodeName())) {
            item = new MenuItem();
            item.setParentItem(parentItem);
            
            NamedNodeMap attributes = node.getAttributes();
            for(int i=0; i<attributes.getLength(); i++) {
                Node attributeNode = attributes.item(i);
                if(attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                    String name = attributeNode.getNodeName();
                    if("action".equals(name)) {
                        item.setAction(attributeNode.getNodeValue());
                        
                        if(moduleConfig != null) {
                            ActionConfig config = moduleConfig.findActionConfig(item.getAction());
                            item.setRights(toRightArray(config.getRoleNames()));
                        }
                    } else if("imageKey".equals(name)) {
                        item.setImageKey(attributeNode.getNodeValue());
                    } else if("altKey".equals(name)) {
                        item.setAltKey(attributeNode.getNodeValue());
                    } else if("messageKey".equals(name)) {
                        item.setMessageKey(attributeNode.getNodeValue());
                    } else if("titleKey".equals(name)) {
                        item.setTitleKey(attributeNode.getNodeValue());
                    } else {
                        throw new MenuFactoryException("invalid attribute found: '"+attributeNode.getNodeName()+"'");
                    }
                }
            }
            
            NodeList nodes = node.getChildNodes();
            for(int i=0; i<nodes.getLength(); i++) {
                if(nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    item.getChildItems().add(loadMenuItem(nodes.item(i), item));
                }
            }
            
            if((item.getImageKey() != null && item.getAltKey() == null) 
                    || (item.getImageKey() == null && item.getAltKey() != null)) {
                throw new MenuFactoryException("attributes 'imageKey' and 'altKey' have to be used together");
            } else if(item.getAction() == null && item.getChildItems().isEmpty()) {
                throw new MenuFactoryException("attribute 'action' have to be set for leaf nodes");
            } else if(item.getAction() != null && !item.getChildItems().isEmpty()) {
                throw new MenuFactoryException("attribute 'action' have not to be set for non leaf nodes");
            }
        } else {
            throw new MenuFactoryException("invalid node found: '"+node.getNodeName()+"' (need: 'menuitem')");
        }
        
        return item;
    }
    
    private static Right[] toRightArray(String[] roles) throws MenuFactoryException {
        List<Right> rights = new ArrayList<Right>();

        for (int i = 0; i < roles.length; i++) {
            try {
                rights.add(Right.parseRight(roles[i]));
            } catch (IllegalArgumentException e) {
                throw new MenuFactoryException("invalid right found in module config: '"+roles[i]+"'", e);
            }
        }

        return rights.toArray(new Right[rights.size()]);
    }
}
