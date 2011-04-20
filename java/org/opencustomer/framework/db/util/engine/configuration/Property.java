package org.opencustomer.framework.db.util.engine.configuration;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.webapp.util.html.Formatter;


public final class Property implements Cloneable {

    private int position;
    
    private String name;

    private String altName;
    
    private String alias;
    
    private String messageKey;
    
    private String entityMessageKey;
    
    private boolean sortable;
    
    private boolean isId;
    
    private Formatter formatter = new DefaultFormatter();
    
    private Search search = null;
    
    private boolean group = false;
    
    public Property() {
    }
    
    public Property(String name) {
        this(name, null);
    }
    
    public Property(String name, String alias) {
        this.alias = alias;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isId() {
        return isId;
    }

    public void setId(boolean isId) {
        this.isId = isId;
    }

    public Formatter getFormatter() {
        return formatter;
    }
    
    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }
    
    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }

    public String getEntityMessageKey() {
        return entityMessageKey;
    }

    public void setEntityMessageKey(String entityMessageKey) {
        this.entityMessageKey = entityMessageKey;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("position",    position);
        builder.append("name",        name);
        builder.append("altName",     altName);
        builder.append("alias",       alias);
        builder.append("entityMessageKey", entityMessageKey);
        builder.append("messageKey",  messageKey);
        builder.append("sortable",    sortable);
        builder.append("group",       group);
        builder.append("isId",        isId);
        builder.append("formatter",   formatter);
        builder.append("search",      search);
        
        return builder.toString();
    }
    
    @Override
    protected Object clone() {
        try {
            Property property    = (Property)super.clone();
            property.position    = this.position; 
            property.name        = this.name; 
            property.altName     = this.altName; 
            property.alias       = this.alias; 
            property.entityMessageKey = this.entityMessageKey; 
            property.messageKey  = this.messageKey; 
            property.sortable    = this.sortable; 
            property.group    = this.group; 
            property.isId        = this.isId; 
            property.formatter   = (Formatter)this.formatter.clone();
            if(search != null) {
                property.search = (Search)this.search.clone();
            }
            
            return property;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
