package org.opencustomer.framework.db.util.engine.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class Configuration implements Cloneable {

    private String name;
    
    private Entity entity;
    
    private ArrayList<Join> joins = new ArrayList<Join>();
    
    private ArrayList<Property> properties = new ArrayList<Property>();
    
    private ArrayList<Property> groupProperties = new ArrayList<Property>();
    
    private TreeMap<Integer, Property> defaultProperties = new TreeMap<Integer, Property>();
    
    private ArrayList<String> restrictions = new ArrayList<String>();
    
    private Property id;
    
    public Configuration(String name) {
        this(name, null);
    }
    
    public Configuration(String name, Entity entity) {
        this.name = name;
        this.entity = entity;
    }
    
    public List<Join> getJoins() {
        return joins;
    }

    public void setJoins(List<Join> joins) {
        this.joins.clear();
        this.joins.addAll(joins);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties.clear();
        this.properties.addAll(properties);
    }

    public Map<Integer, Property> getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(Map<Integer, Property> defaultProperties) {
        this.defaultProperties.clear();
        this.defaultProperties.putAll(defaultProperties);
    }

    public ArrayList<Property> getGroupProperties() {
        return groupProperties;
    }

    public void setGroupProperties(ArrayList<Property> groupProperties) {
        this.groupProperties = groupProperties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Property getId() {
        return id;
    }

    public void setId(Property id) {
        this.id = id;
    }

    public ArrayList<String> getRestrictions() {
        return restrictions;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("name",                  name);
        builder.append("entity",                entity);
        builder.append("joins(size)",           joins == null ? 0 : joins.size());
        builder.append("properties(size)",      properties == null ? 0 : properties.size());
        builder.append("groupProperties(size)", groupProperties == null ? 0 : groupProperties.size());
        builder.append("restrictions(size)",    restrictions == null ? 0 : restrictions.size());
        builder.append("id",                    id);
        
        return builder.toString();
    }
    
    @Override
    public Object clone() {
        try {
            Configuration configuration = (Configuration)super.clone();
            configuration.name = this.name; 
            
            configuration.entity = (Entity)this.entity.clone();
            
            configuration.joins = (ArrayList<Join>)this.joins.clone(); 
            configuration.joins.clear();
            for(Join join : this.joins) {
                configuration.joins.add((Join)join.clone());
            }
            
            configuration.properties = (ArrayList<Property>)this.properties.clone();
            configuration.properties.clear();
            for(Property property : this.properties) {
                configuration.properties.add((Property)property.clone());
            }
            
            configuration.groupProperties = (ArrayList<Property>)this.groupProperties.clone();
            configuration.groupProperties.clear();
            for(Property property : this.groupProperties) {
                configuration.groupProperties.add((Property)property.clone());
            }

            configuration.restrictions = (ArrayList<String>)this.restrictions.clone();
            
            configuration.id = (Property)this.id.clone();
            
            return configuration;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

}
