package org.opencustomer.framework.db.util.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.engine.Order.OrderColumn;
import org.opencustomer.framework.db.util.engine.configuration.Configuration;
import org.opencustomer.framework.db.util.engine.configuration.Join;
import org.opencustomer.framework.db.util.engine.configuration.Property;

public final class TableEngine implements Cloneable {

    private final static Logger log = Logger.getLogger(TableEngine.class);
    
    private final static String ALIAS_PATTERN = "[^[\\p{Alnum}\\.]]*\\p{Alnum}+\\.+\\p{Alnum}+[^[\\p{Alnum}\\.]]*";
    
    private Configuration configuration;
    
    private String baseHql;

    public TableEngine(Configuration configuration) {
        this.configuration = configuration;
        
        this.baseHql      = createBaseHQL();
    }

    public void optimizeQuery() {
        if(log.isDebugEnabled())
            log.debug("optimize query for table engine");
        
        HashSet<String> aliases = new HashSet<String>();
        Pattern pattern = Pattern.compile(ALIAS_PATTERN);
        for(Property property : configuration.getProperties()) {
            Matcher matcher = pattern.matcher(property.getName());
            while(matcher.find()) {
                String name = property.getName().substring(matcher.start(), matcher.end());
                
                aliases.add(name.substring(0, name.indexOf(".")));
            }
            
            if(property.getAltName() != null) {
                Matcher matcher2 = pattern.matcher(property.getAltName());
                while(matcher2.find()) {
                    String name = property.getAltName().substring(matcher2.start(), matcher2.end());
                    
                    aliases.add(name.substring(0, name.indexOf(".")));
                }
            }
        }

        baseHql = createBaseHQL(aliases);        
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public List<Row> getList() {
        return getList(null, null, null);
    }

    public List<Row> getList(Order order) {
        return getList(null, null, order);
    }

    public List<Row> getList(Page page) {
        return getList(null, page, null);
    }

    public List<Row> getList(Restriction[] restrictions, Page page, Order order) {
        StringBuilder hql = new StringBuilder();

        hql.append(" select ");
        hql.append(configuration.getId().getName());
        for(Property property : configuration.getProperties()) {
            String column = property.getName();
            if(property.getAltName() != null) {
                column = "case when "+property.getName()+" is not null then "+property.getName()+" else "+property.getAltName()+" end ";
            }
            
            if(property.getAlias() != null)
                column += " as "+property.getAlias();
            
            hql.append(", ").append(column);
        }
        
        hql.append(baseHql);
        
        addRestrictionsToHql(hql, restrictions);

        addGroupToHql(hql, configuration.getGroupProperties());
        addHavingToHql(hql, restrictions);
        
        if(order != null) {
            hql.append(" order by ");
            boolean isFirst = true;
            for(OrderColumn orderColumn : order.getOrderColumns()) {
                if(isFirst) {
                    isFirst = false;
                } else {
                  hql.append(", ");  
                }
                hql.append((orderColumn.getColumn()+2));
                if(orderColumn.isAscending()) { 
                    hql.append(" asc");
                } else {
                    hql.append(" desc");
                }
            }
        }

        Query query = HibernateContext.getSession().createQuery(hql.toString());
        addRestrictionsToQuery(query, restrictions);
        
        if (page != null) {
            query.setFirstResult((int)((page.getPage() - 1) * page.getStep()));
            query.setMaxResults((int)page.getStep());
        }
        
        List<Object[]> result = (List<Object[]>)query.list();
        List<Row> rows = new ArrayList<Row>();
        for(Object[] values : result) {
            Row row = new Row();
            row.setId((Integer)values[0]);
            Object[] columns = new Object[values.length-1];
            for(int i=1; i<values.length; i++) {
                columns[i-1] = values[i];
            }
            row.setColumns(columns);
            
            rows.add(row);
        }

        return rows;
    }
    
    public Long getCount() {
        return this.getCount(null);
    }
    
    public Long getCount(Restriction[] restrictions) {
        StringBuilder hql = new StringBuilder();
        
        hql.append(" select count(").append(configuration.getId().getName()).append(")");

        hql.append(baseHql);
        
        addRestrictionsToHql(hql, restrictions);
        
        addGroupToHql(hql, configuration.getGroupProperties());
        addHavingToHql(hql, restrictions);

        Query query = HibernateContext.getSession().createQuery(hql.toString());
        addRestrictionsToQuery(query, restrictions);
        
        if(configuration.getGroupProperties().isEmpty() || configuration.getGroupProperties().size() == configuration.getProperties().size()) {
            return (Long)query.uniqueResult();
        } else {
            return new Long(query.list().size());
        }
    }

    private String createBaseHQL() {
        return createBaseHQL(null);
    }
    
    /**
     * Identifies aliases used by joins (e.g. help tables).
     * 
     * @param aliases the available aliases
     */
    private void identifyAliases(Set<String> aliases) {
        boolean found = false;
        
        if(aliases != null) {
            for(Join join : configuration.getJoins()) {
                if(aliases.contains(join.getAlias())) {
                    String usedAlias = join.getName().substring(0, join.getName().indexOf('.'));
                    if(!aliases.contains(usedAlias)) {
                        aliases.add(usedAlias);
                        found = true;
                    }
                }
            }
        }
        
        if(found) {
            identifyAliases(aliases);
        }
    }
    
    private String createBaseHQL(Set<String> aliases) {
        StringBuilder hql = new StringBuilder();
        
        identifyAliases(aliases);
        
        hql.append(" FROM ").append(configuration.getEntity().getClazz().getName()).append(" e ");
        
        for(Join join : configuration.getJoins()) {
            if(aliases == null || aliases.contains(join.getAlias())) {
                String table = join.getName();
                if(join.getAlias() != null)
                    table += " "+join.getAlias(); 
                
                if(Join.Type.LEFT_JOIN.equals(join.getType()))
                    hql.append(" left join ").append(table);
            }
        }
        
        hql.append(" WHERE 1=1 ");
        if(aliases == null) {
            for(String restriction : configuration.getRestrictions()) {
                hql.append(" and ").append(restriction);
            }
        } else {
            Pattern pattern = Pattern.compile(ALIAS_PATTERN);
            
            for(String restriction : configuration.getRestrictions()) {
                boolean valid = true;
                Matcher matcher = pattern.matcher(restriction);
                while(matcher.find()) {
                    String name = restriction.substring(matcher.start(), matcher.end());
                    String alias = name.substring(0, name.indexOf("."));

                    if(!aliases.contains(alias)) {
                        valid = false;
                    }
                }
                
                if(valid) {
                    hql.append(" and ").append(restriction);
                }
            }
        }
        
        return hql.toString();
    }
    
    private void addRestrictionsToHql(StringBuilder hql, Restriction[] restrictions) {
        if(restrictions != null) {
            boolean isGrouped = !configuration.getGroupProperties().isEmpty();
            
            for(int i=0; i<restrictions.length; i++) {
                if(!isGrouped || (isGrouped && restrictions[i].isGroup())) {
                    hql.append(" and "+replaceParams(restrictions[i].getHql(), i));
                }
            }
        }
    }

    private void addHavingToHql(StringBuilder hql, Restriction[] restrictions) {
        if(restrictions != null) {
            boolean isGrouped = !configuration.getGroupProperties().isEmpty();
            
            if(isGrouped) {
                boolean havingNeeded = false;
                for(Restriction restriction : restrictions) {
                    if(!restriction.isGroup()) {
                        havingNeeded = true;
                        break;
                    }
                }
                
                if(havingNeeded) {                
                    hql.append(" having ");
                    boolean isFirst = true;
                    for(int i=0; i<restrictions.length; i++) {
                        if(!restrictions[i].isGroup()) {
                            if(isFirst) {
                                isFirst = false;
                            } else {
                              hql.append(" and ");  
                            }
                            hql.append(replaceParams(restrictions[i].getHql(), i));
                        }
                    }
                }
            }
        }
    }
    
    private void addRestrictionsToQuery(Query query, Restriction[] restrictions) {
        if(restrictions != null) {
            Pattern pattern = Pattern.compile("\\{\\d\\}");
            
            for(int i=0; i<restrictions.length; i++) {
                
                Matcher matcher = pattern.matcher(restrictions[i].getHql());
                while(matcher.find()) {
                    String match = restrictions[i].getHql().substring(matcher.start(), matcher.end());
                    String param = replaceParams(match, i).substring(1);
                    int pos      = Integer.parseInt(match.substring(1, match.length()-1));
                    
                    query.setParameter(param, restrictions[i].getValues()[pos]);
                }
            }
        }
    }
    
    private void addGroupToHql(StringBuilder hql, List<Property> groupProperties) {
        if(!groupProperties.isEmpty() && groupProperties.size() != configuration.getProperties().size()) {
            hql.append(" group by ");
            boolean isFirst = true;
            for(Property property : groupProperties) {
                if(isFirst) {
                    isFirst = false;
                } else {
                  hql.append(", ");  
                }
                hql.append(property.getName());
            }
        }
    }
    
    private String replaceParams(String value, int pos) {
        return value.replaceAll("\\{", ":param_"+pos+"_").replaceAll("\\}", "");
    }
    
    public static String decorate(Object[] array) {
        ToStringBuilder builder = new ToStringBuilder(array);
        
        for(int i=0; i<array.length; i++) {
            builder.append(String.valueOf(i), array[i]);
        }
        
        return builder.toString();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("configuration", configuration.getName());
        
        return builder.toString();
    }
    
    @Override
    public Object clone() {
        try {
            TableEngine engine = (TableEngine)super.clone();
            
            engine.configuration = (Configuration)this.configuration.clone(); 

            return engine;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
