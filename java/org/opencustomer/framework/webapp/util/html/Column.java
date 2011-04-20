package org.opencustomer.framework.webapp.util.html;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.util.engine.configuration.Search;

public final class Column {

    private int position;
    
    private String messageKey;
    
    private boolean sortable;
    
    private Formatter formatter;
    
    private Search search;
    
    public Column() {
    }
    
    public Column(int position, String messageKey, boolean sortable) {
        this.position   = position;
        this.messageKey = messageKey;
        this.sortable   = sortable;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
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

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("messageKey", messageKey);
        builder.append("sortable",   sortable);
        builder.append("formatter",  formatter);
        builder.append("search",     search);
        builder.append("position",   position);
        
        return builder.toString();
    }

}
