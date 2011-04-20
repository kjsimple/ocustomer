package org.opencustomer.framework.webapp.util.html;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.engine.Order;
import org.opencustomer.framework.db.util.engine.Restriction;
import org.opencustomer.framework.db.util.engine.Row;
import org.opencustomer.framework.db.util.engine.TableEngine;
import org.opencustomer.framework.db.util.engine.configuration.DateSearch;
import org.opencustomer.framework.db.util.engine.configuration.EnumSearch;
import org.opencustomer.framework.db.util.engine.configuration.ListSelectSearch;
import org.opencustomer.framework.db.util.engine.configuration.Property;
import org.opencustomer.framework.db.util.engine.configuration.TextSearch;
import org.opencustomer.framework.db.util.engine.configuration.TextSelectSearch;

public class Table implements Pagination {

    private final static Logger log = Logger.getLogger(Table.class);
    
    private TableEngine engine;
    
    private Column[] columns;
    
    private long count;
    
    private List<Row> rows;
    
    private Order order = new Order(0, true);
    
    private Page page = new Page(20, 1);
    
    private ArrayList<Restriction> restrictions = new ArrayList<Restriction>();
    
    public Table(TableEngine engine) {
        this.engine = engine;
    
        columns = new Column[engine.getConfiguration().getProperties().size()];
        for(int i=0; i<columns.length; i++) {
            Property property = engine.getConfiguration().getProperties().get(i);
            columns[i] = new Column(i, property.getMessageKey(), property.isSortable());
            columns[i].setFormatter(property.getFormatter());
            columns[i].setSearch(property.getSearch());
            
            order.add(columns.length-1-i, true);
        }
    }

    public final Order getOrder() {
        return order;
    }

    public final Page getPage() {
        return page;
    }
    
    public final long getCount() {
        return count;
    }
    
    public final Column[] getColumns() {
        return columns;
    }
    
    public final List<Row> getRows() {
        return rows;
    }
    
    public final void loadSearch(ActionMessages errors, HttpServletRequest request) {
        for(Column column : columns) {
            if(column.getSearch() != null) {
                column.getSearch().load(column, errors, request);
            }
        }
    }
    
    
    public final void resetSearch() {
        for(Column column : columns) {
            if(column.getSearch() != null) {
                column.getSearch().reset();
            }
        }
    }

    public final String getName() {
        return engine.getConfiguration().getName();
    }
    
    public final void query() {
        query(false);
    }
    
    public final void query(boolean ignorePage) {
        if(log.isDebugEnabled())
            log.debug("load table");
        
        Restriction[] dynamicRestrictions = createRestrictions();
        
        count = engine.getCount(dynamicRestrictions);
     
        if(ignorePage) {
            rows = engine.getList(dynamicRestrictions, null, order);
        } else {
            if(count < page.getFirstEntry()) {
                page.setPage(count/page.getStep()+1);
            }
            
            rows = engine.getList(dynamicRestrictions, page, order);
        }
    }
    
    private Restriction[] createRestrictions() {
        ArrayList<Restriction> dynamicRestrictions = new ArrayList<Restriction>();
        if(log.isDebugEnabled())
            log.debug("create criterions ("+columns.length+")");

        dynamicRestrictions.addAll(restrictions);
        
        for(Column column : columns) {
            Property property = engine.getConfiguration().getProperties().get(column.getPosition());
            
            if(column.getSearch() instanceof TextSearch) {
                TextSearch search = (TextSearch)column.getSearch();
                if(search.getValue() != null) {
                    String critValue = search.getPattern().replace('*', '%').replaceAll("\\{search\\}", search.getValue());
                    
                    if(log.isDebugEnabled())
                        log.debug("add text criterion for "+property.getName()+": "+critValue);
                    
                    if(property.getAltName() == null) {
                        dynamicRestrictions.add(new Restriction("lower("+property.getName()+") like lower({0})", property.isGroup(), critValue));
                    } else {
                        dynamicRestrictions.add(new Restriction("(lower("+property.getName()+") like lower({0}) or ("+property.getName()+" is null and lower("+property.getAltName()+") like lower({0})))", property.isGroup(), critValue));
                    }
                }
            } else if(column.getSearch() instanceof EnumSearch) {
                EnumSearch search = (EnumSearch)column.getSearch();

                if(search.getValue() != null) {
                    dynamicRestrictions.add(new Restriction(property.getName()+" = {0}", property.isGroup(), search.getValue()));
                }
            } else if(column.getSearch() instanceof TextSelectSearch) {
                TextSelectSearch search = (TextSelectSearch)column.getSearch();
                
                if(search.isHql()) {
                    TextSelectSearch.Bean bean = search.getBeans().get(search.getValue());
                    if(bean.getHql() != null)
                        dynamicRestrictions.add(new Restriction(bean.getHql(), property.isGroup()));
                } else {
                    if(search.getValue() != null) {
                        dynamicRestrictions.add(new Restriction(property.getName()+" = {0}", property.isGroup(), search.getValue()));
                    }
                }
            } else if(column.getSearch() instanceof ListSelectSearch) {
                ListSelectSearch search = (ListSelectSearch)column.getSearch();
                
                if(search.getValue() != null)
                    dynamicRestrictions.add(new Restriction(search.getSearchProperty()+" = {0}", property.isGroup(), search.getValue()));
            } else if(column.getSearch() instanceof DateSearch) {
                DateSearch search = (DateSearch)column.getSearch();
                
                Date start = search.getValueStart();
                Date end   = search.getValueEnd();
                if(end != null) {
                    Calendar cal = GregorianCalendar.getInstance();
                    cal.setTime(end);
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    cal.add(Calendar.SECOND, -1);
                    end = cal.getTime();
                }
                
                if(start != null && end != null) {
                    dynamicRestrictions.add(new Restriction(property.getName()+" between {0} and {1}", property.isGroup(), start, end));
                } else if(start != null) {
                    dynamicRestrictions.add(new Restriction(property.getName()+" >= {0}", property.isGroup(), start));
                } else if(end != null) {
                    dynamicRestrictions.add(new Restriction(property.getName()+" <= {0}", property.isGroup(), end));
                }
            }
        }
        
        if(dynamicRestrictions.isEmpty()) {
            return null;
        } else {
            return dynamicRestrictions.toArray(new Restriction[dynamicRestrictions.size()]);
        }
    }
    
    protected void addRestriction(Restriction restriction) {
        restrictions.add(restriction);
    }
}
