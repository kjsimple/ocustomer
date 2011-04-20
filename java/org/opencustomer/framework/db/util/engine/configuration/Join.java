package org.opencustomer.framework.db.util.engine.configuration;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.criterion.CriteriaSpecification;

public final class Join implements Cloneable {

    public static enum Type {
        INNER_JOIN (CriteriaSpecification.INNER_JOIN),
        LEFT_JOIN (CriteriaSpecification.LEFT_JOIN);
        
        private int hibernateJoinType;
        
        private Type(int hibernateJoinType) {
            this.hibernateJoinType = hibernateJoinType;
        }
        
        public int getHibernateJoinType() {
            return hibernateJoinType;
        }
    }
    
    private String name;
    
    private String alias;
    
    private String messageKey;
    
    private Type type;
    
    public Join() {
    }

    public Join(String name, String alias, Type type) {
        this.name  = name;
        this.alias = alias;
        this.type  = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("name",       name);
        builder.append("alias",      alias);
        builder.append("type",       type);
        builder.append("messageKey", messageKey);
        
        return builder.toString();
    }

    @Override
    protected Object clone() {
        try {
            Join join     = (Join)super.clone();
            
            join.name       = this.name; 
            join.alias      = this.alias; 
            join.messageKey = this.messageKey; 
            join.type       = this.type; 
            
            
            return join;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

}