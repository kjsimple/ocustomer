package org.opencustomer.framework.db.util.engine;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.opencustomer.framework.db.util.engine.configuration.Configuration;
import org.opencustomer.framework.db.util.engine.configuration.DateFormatter;
import org.opencustomer.framework.db.util.engine.configuration.DateSearch;
import org.opencustomer.framework.db.util.engine.configuration.DefaultFormatter;
import org.opencustomer.framework.db.util.engine.configuration.Entity;
import org.opencustomer.framework.db.util.engine.configuration.EnumFormatter;
import org.opencustomer.framework.db.util.engine.configuration.EnumSearch;
import org.opencustomer.framework.db.util.engine.configuration.Join;
import org.opencustomer.framework.db.util.engine.configuration.ListSelectSearch;
import org.opencustomer.framework.db.util.engine.configuration.Property;
import org.opencustomer.framework.db.util.engine.configuration.Search;
import org.opencustomer.framework.db.util.engine.configuration.StringFormatter;
import org.opencustomer.framework.db.util.engine.configuration.TextSearch;
import org.opencustomer.framework.db.util.engine.configuration.TextSelectSearch;
import org.opencustomer.framework.db.vo.BaseVO;
import org.opencustomer.framework.util.EnumUtility;
import org.opencustomer.framework.webapp.util.html.Formatter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableEngineFactory {

    private final static Logger log = Logger.getLogger(TableEngineFactory.class);
    
    private Hashtable<String, TableEngine> engines = new Hashtable<String, TableEngine>();
    
    public TableEngineFactory(File packageDir) {
        
        File[] configurationFiles = packageDir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if(file.isFile() && file.getName().endsWith(".xml")) {
                    return true;
                } else {
                    return false;
                }
            };
        });
        
        for(File file : configurationFiles) {
            loadConfiguration(file);
        }
    }
    
    public final Configuration getConfiguration(String name) {
        return engines.get(name).getConfiguration();
    }
    
    public final TableEngine getEngine(String name) {
        return getEngine(name, (Integer[])null);
    }

    public final TableEngine getEngine(String name, Integer... columns) {
        TableEngine engine = (TableEngine)engines.get(name).clone();
        
        if(columns != null) {
            List<Property> newProperties = new ArrayList<Property>();
            
            for(Integer column : columns) {
                newProperties.add(engine.getConfiguration().getProperties().get(column));
            }
            
            engine.getConfiguration().setProperties(newProperties);
        } else {
            engine.getConfiguration().getProperties().clear();
            engine.getConfiguration().getProperties().addAll(engine.getConfiguration().getDefaultProperties().values());
        }
        
        // get group properties
        for(Property property : engine.getConfiguration().getProperties()) {
            if(property.isGroup())
                engine.getConfiguration().getGroupProperties().add(property);
        }

        engine.optimizeQuery();
        
        return engine;
    }

    private void loadConfiguration(File file) throws TableEngineException {
        if(log.isDebugEnabled()) {
            log.debug("load list configuraton: "+file);
        }
        
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(in);
            Node root = document.getDocumentElement();
            if("configuration".equals(root.getNodeName())) {
                Configuration conf = new Configuration(file.getName().substring(0, file.getName().indexOf(".")));
                
                NodeList nodes = root.getChildNodes();
                for(int i=0; i<nodes.getLength(); i++) {
                    if(nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        if("entity".equals(nodes.item(i).getNodeName())) {
                            conf.setEntity(parseEntity(nodes.item(i)));
                        } else if("join".equals(nodes.item(i).getNodeName())) {
                            conf.getJoins().add(parseJoin(nodes.item(i)));                            
                        } else if("restriction".equals(nodes.item(i).getNodeName())) {
                            conf.getRestrictions().add(parseRestriction(nodes.item(i)));                            
                        } else if("property".equals(nodes.item(i).getNodeName())) {
                            Property property = parseProperty(conf, nodes.item(i));
                            if(property.isId()) {
                                if(conf.getId() == null) {
                                    conf.setId(property);
                                } else {
                                    throw new TableEngineException("found duplicate id property");
                                }
                            }
                            property.setAlias("alias_"+conf.getProperties().size());
                            property.setPosition(conf.getProperties().size());
                            conf.getProperties().add(property);                            
                        } 
                    }
                }
                
                if(log.isDebugEnabled())
                    log.debug("add list renderer: "+conf.getName());
                
                engines.put(conf.getName(), new TableEngine(conf));
            } else {
                throw new TableEngineException("invalid attribute found: '"+root.getNodeName()+"' (need: 'configuration')");
            }
        } catch(Exception e) {
            if(in != null) {
                try {
                    in.close();
                } catch(IOException e2) {
                }
            }
            throw new TableEngineException("could not load engine", e);
        }
    }
    
    private Entity parseEntity(Node node) throws TableEngineException {
        Entity entity = new Entity();
        
        NamedNodeMap attributes = node.getAttributes();
        for(int i=0; i<attributes.getLength(); i++) {
            Node attributeNode = attributes.item(i);
            if(attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                String name = attributeNode.getNodeName();
                if("class".equals(name)) {
                    try {
                    entity.setClazz((Class<? extends BaseVO>)Class.forName(attributeNode.getNodeValue()));
                    } catch(ClassNotFoundException e) {
                        throw new TableEngineException(e);
                    }
                } else if("alias".equals(name)) {
                    entity.setAlias(attributeNode.getNodeValue());
                } else if("messageKey".equals(name)) {
                    entity.setMessageKey(attributeNode.getNodeValue());
                }
            }
        }
        
        return entity;
    }
    
    private String parseRestriction(Node node) throws TableEngineException {
        String restriction = null;
        
        NamedNodeMap attributes = node.getAttributes();
        for(int i=0; i<attributes.getLength(); i++) {
            Node attributeNode = attributes.item(i);
            if(attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                String name = attributeNode.getNodeName();
                if("hql".equals(name)) {
                    restriction = attributeNode.getNodeValue();
                }
            }
        }
        
        return restriction;
    }
    
    private Join parseJoin(Node node) throws TableEngineException {
        Join join = new Join();
        
        NamedNodeMap attributes = node.getAttributes();
        for(int i=0; i<attributes.getLength(); i++) {
            Node attributeNode = attributes.item(i);
            if(attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                String name = attributeNode.getNodeName();
                if("name".equals(name)) {
                    join.setName(attributeNode.getNodeValue());
                } else if("alias".equals(name)) {
                    join.setAlias(attributeNode.getNodeValue());
                } else if("type".equals(name)) {
                    join.setType(EnumUtility.valueOf(Join.Type.class, attributeNode.getNodeValue()));
                } else if("messageKey".equals(name)) {
                    join.setMessageKey(attributeNode.getNodeValue());
                }
            }
        }
        
        return join;
    }
    
    private Property parseProperty(Configuration configuration, Node node) throws TableEngineException {
        Property property = new Property();
        
        NamedNodeMap attributes = node.getAttributes();
        for(int i=0; i<attributes.getLength(); i++) {
            Node attributeNode = attributes.item(i);
            if(attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                String name = attributeNode.getNodeName();
                if("name".equals(name)) {
                    property.setName(attributeNode.getNodeValue());
                } else if("altName".equals(name)) {
                    property.setAltName(attributeNode.getNodeValue());
                } else if("messageKey".equals(name)) {
                    property.setMessageKey(attributeNode.getNodeValue());
                } else if("entityMessageKey".equals(name)) {
                    property.setEntityMessageKey(attributeNode.getNodeValue());
                } else if("type".equals(name)) {
                    property.setSortable(Boolean.parseBoolean(attributeNode.getNodeValue()));
                } else if("id".equals(name)) {
                    property.setId(Boolean.parseBoolean(attributeNode.getNodeValue()));
                } else if("sortable".equals(name)) {
                    property.setSortable(Boolean.parseBoolean(attributeNode.getNodeValue()));
                } else if("default".equals(name)) {
                    configuration.getDefaultProperties().put(Integer.parseInt(attributeNode.getNodeValue()), property);
                } else if("group".equals(name)) {
                    property.setGroup(Boolean.parseBoolean(attributeNode.getNodeValue()));
                }
            }
        }
        
        NodeList nodes = node.getChildNodes();
        for(int i=0; i<nodes.getLength(); i++) {
            if(nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                if("format".equals(nodes.item(i).getNodeName())) {
                    Formatter format = parseFormat(nodes.item(i));
                    if(format == null) {
                        format = new DefaultFormatter();
                    }
                    property.setFormatter(format);
                } else if("search".equals(nodes.item(i).getNodeName())) {
                    property.setSearch(parseSearch(nodes.item(i)));
                }

            }
        }
        
        return property;
    }
    
    private Formatter parseFormat(Node node) throws TableEngineException {
        Formatter format = null;

        NamedNodeMap attributes = node.getAttributes();
        for(int i=0; i<attributes.getLength(); i++) {
            Node attributeNode = attributes.item(i);
            if(attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                String name = attributeNode.getNodeName();
                if("type".equals(name)) {
                    if("date".equals(attributeNode.getNodeValue())) {
                        format = new DateFormatter(attributes.getNamedItem("formatKey").getNodeValue());
                    } else if("enum".equals(attributeNode.getNodeValue())) {
                        try {
                            EnumFormatter enumFormat = new EnumFormatter((Class<? extends Enum>)Class.forName(attributes.getNamedItem("class").getNodeValue()));
                            NodeList nodes = node.getChildNodes();
                            for(int j=0; j<nodes.getLength(); j++) {
                                if(nodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                    if("value".equals(nodes.item(j).getNodeName())) {
                                        parseValue(enumFormat, nodes.item(j));
                                    }
                                }
                           }
                            
                           format = enumFormat;
                        } catch(ClassNotFoundException e) {
                            throw new TableEngineException("could get attribute 'class'", e);
                        }
                    } else if("string".equals(attributeNode.getNodeValue())) {
                        StringFormatter stringFormat = new StringFormatter();
                        NodeList nodes = node.getChildNodes();
                        for(int j=0; j<nodes.getLength(); j++) {
                            if(nodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                if("value".equals(nodes.item(j).getNodeName())) {
                                    parseValue(stringFormat, nodes.item(j));
                                }
                            }
                       }
                        
                       format = stringFormat;
                    }
                }
            }
        }
        
        return format;
    }
    
    private void parseValue(EnumFormatter format, Node node) {
        Enum[] enums = format.getClazz().getEnumConstants();
        
        Enum type         = null;
        String messageKey = null;
        
        NamedNodeMap attributes = node.getAttributes();
        for(int i=0; i<attributes.getLength(); i++) {
            Node attributeNode = attributes.item(i);
            if(attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                String name = attributeNode.getNodeName();
                if("type".equals(name)) {
                    for(Enum e : enums) {
                        if(attributeNode.getNodeValue().equals(e.name())) {
                            type = e;
                        }
                    }
                } else if("messageKey".equals(name)) {
                    messageKey = attributeNode.getNodeValue();
                }
            }
        }
        
        format.getMessageKeys().put(type, messageKey);
    }

    private void parseValue(StringFormatter format, Node node) {
        String type       = null;
        String messageKey = null;
        
        NamedNodeMap attributes = node.getAttributes();
        for(int i=0; i<attributes.getLength(); i++) {
            Node attributeNode = attributes.item(i);
            if(attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                String name = attributeNode.getNodeName();
                if("type".equals(name)) {
                    type = attributeNode.getNodeValue();
                } else if("messageKey".equals(name)) {
                    messageKey = attributeNode.getNodeValue();
                }
            }
        }
        
        format.getMessageKeys().put(type, messageKey);
    }
    
    private Search parseSearch(Node node) throws TableEngineException {
        Search search = null;

        NamedNodeMap attributes = node.getAttributes();
        for(int i=0; i<attributes.getLength(); i++) {
            Node attributeNode = attributes.item(i);
            if(attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                String name = attributeNode.getNodeName();
                if("type".equals(name)) {
                    if("enum".equals(attributeNode.getNodeValue())) {
                        try {
                            EnumSearch enumSearch = new EnumSearch((Class<? extends Enum>)Class.forName(attributes.getNamedItem("class").getNodeValue()));
                            NodeList nodes = node.getChildNodes();
                            for(int j=0; j<nodes.getLength(); j++) {
                                if(nodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                    if("value".equals(nodes.item(i).getNodeName())) {
                                        parseValue(enumSearch, nodes.item(j));
                                    }
                                }
                            }
                            
                            search = enumSearch;
                        } catch(ClassNotFoundException e) {
                            throw new TableEngineException("could get attribute 'class'", e);
                        }
                    } else if("text".equals(attributeNode.getNodeValue())) {
                        search = new TextSearch(attributes.getNamedItem("pattern").getNodeValue());
                    } else if("date".equals(attributeNode.getNodeValue())) {
                        search = new DateSearch(attributes.getNamedItem("formatKey").getNodeValue());
                    } else if("text.select".equals(attributeNode.getNodeValue())) {
                        TextSelectSearch textSelectSearch = new TextSelectSearch();
                        if(attributes.getNamedItem("hql") != null)
                            textSelectSearch.setHql(attributes.getNamedItem("hql").getNodeValue().equalsIgnoreCase("true") ? true : false);
                        
                        NodeList nodes = node.getChildNodes();
                        for(int j=0; j<nodes.getLength(); j++) {
                            if(nodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                if("value".equals(nodes.item(i).getNodeName())) {
                                    parseValue(textSelectSearch, nodes.item(j));
                                }
                            }
                        }
                        
                        search = textSelectSearch;
                    } else if("list.select".equals(attributeNode.getNodeValue())) {
                        String clazz = attributes.getNamedItem("class").getNodeValue();
                        try {
                            ListSelectSearch listSelectSearch = new ListSelectSearch();
                            listSelectSearch.setClazz(Class.forName(clazz));
                            if("true".equalsIgnoreCase(attributes.getNamedItem("all").getNodeValue()))
                                listSelectSearch.setAllAvailable(true);
                            else
                                listSelectSearch.setAllAvailable(false);
                            listSelectSearch.setSearchProperty(attributes.getNamedItem("searchProperty").getNodeValue());
                            
                            search = listSelectSearch;
                        } catch(ClassNotFoundException e) {
                            throw new TableEngineException("could not create/use dao: "+clazz, e);
                        }
                    }
                }
            }
        }
        
        return search;
    }

    private void parseValue(EnumSearch search, Node node) {
        Enum[] enums = search.getClazz().getEnumConstants();
        
        Enum type         = null;
        String messageKey = null;
        boolean isDefault = false;
        
        NamedNodeMap attributes = node.getAttributes();
        for(int i=0; i<attributes.getLength(); i++) {
            Node attributeNode = attributes.item(i);
            if(attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                String name = attributeNode.getNodeName();
                if("type".equals(name)) {
                    for(Enum e : enums) {
                        if(attributeNode.getNodeValue().equals(e.name())) {
                            type = e;
                        }
                    }
                } else if("messageKey".equals(name)) {
                    messageKey = attributeNode.getNodeValue();
                } else if("default".equals(name)) {
                    if("true".equalsIgnoreCase(attributeNode.getNodeValue())) {
                        isDefault = true;
                    }
                }
            }
        }
        
        search.getMessageKeys().put(type, messageKey);
        if(isDefault) {
            search.setDefaultValue(type);
        }
    }

    private void parseValue(TextSelectSearch search, Node node) {
        String type       = null;
        String messageKey = null;
        String hql        = null;
        boolean isDefault = false;
        
        NamedNodeMap attributes = node.getAttributes();
        for(int i=0; i<attributes.getLength(); i++) {
            Node attributeNode = attributes.item(i);
            if(attributeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                String name = attributeNode.getNodeName();
                if("type".equals(name)) {
                    type = attributeNode.getNodeValue();
                } else if("messageKey".equals(name)) {
                    messageKey = attributeNode.getNodeValue();
                } else if("hql".equals(name) && search.isHql()) {
                    hql = attributeNode.getNodeValue();
                } else if("default".equals(name)) {
                    if("true".equalsIgnoreCase(attributeNode.getNodeValue())) {
                        isDefault = true;
                    }
                }
            }
        }
        
        search.getBeans().put(type, new TextSelectSearch.Bean(messageKey, hql));
        if(isDefault) {
            search.setDefaultValue(type);
        }
    }
}
