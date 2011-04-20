package org.opencustomer.framework.db.util.engine;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class Row {

    private Integer id;
    
    private Object[] columns;

    public Object[] getColumns() {
        return columns;
    }

    public void setColumns(Object[] columns) {
        this.columns = columns;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("id", id);
        builder.append("columns", columns);
        
        return builder.toString();
    }
}
