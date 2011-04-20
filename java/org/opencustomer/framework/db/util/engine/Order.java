package org.opencustomer.framework.db.util.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

public final class Order {

    private final static Logger log = Logger.getLogger(Order.class);
    
    private ArrayList<OrderColumn> orderColumns = new ArrayList<OrderColumn>();
    
    public Order() {
    }
    
    public Order(int column, boolean ascending) {
        add(column, ascending);
    }
    
    public void clear() {
        orderColumns.clear();
    }
    
    public void add(int column, boolean ascending) {
        Iterator<OrderColumn> it = orderColumns.iterator();
        while(it.hasNext()) {
            OrderColumn oc = it.next();
            if(oc.getColumn() == column) {
                it.remove();
            }
        }
        orderColumns.add(0, new OrderColumn(column, ascending));
    }

    List<OrderColumn> getOrderColumns() {
        return orderColumns;
    }
    
    /** 
     * Adds a order parameter with the structure {order}_{column}.
     * @param orderParam the unparsed parameter.
     */
    public void add(String orderParam){
        if(orderParam != null) {
            String[] parts = orderParam.split("_", 2);
            if(parts.length == 2) {
                boolean ascending = true;
                if(parts[0].equals("asc")) {
                    ascending = true;
                } else if(parts[0].equals("desc")) {
                    ascending = false;
                } else {
                    throw new IllegalArgumentException("found invalid order (position 1): "+parts[0]);
                }

                try {
                    this.add(Integer.parseInt(parts[1]), ascending);
                } catch(NumberFormatException e) {
                    throw new IllegalArgumentException("found invalid column (position 2): "+parts[1]);
                }
            } else {
                throw new IllegalArgumentException("invalid number of paramaeter parts found: "+parts.length+" (expected: 2)");
            }
        }
    }
    
    static class OrderColumn {

        private int column;
        
        private boolean ascending;
        
        public OrderColumn() {
        }

        public OrderColumn(int column, boolean ascending) {
            this.column    = column;
            this.ascending = ascending;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public boolean isAscending() {
            return ascending;
        }
        
        public void setAscending(boolean ascending) {
            this.ascending = ascending;
        }
        
        @Override
        public String toString() {
            return column+"-"+ascending;
        }
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("orderColumns", orderColumns);
        
        return builder.toString();
    }
}
