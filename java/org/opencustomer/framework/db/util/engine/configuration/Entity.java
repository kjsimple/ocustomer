package org.opencustomer.framework.db.util.engine.configuration;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.framework.db.vo.BaseVO;

public class Entity implements Cloneable {

    private Class<? extends BaseVO> clazz;
    
    private String messageKey;
    
    private String alias;
    
    public Entity() {
    }
    
    public Entity(Class<? extends BaseVO> clazz, String alias) {
        this.clazz = clazz;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Class<? extends BaseVO> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends BaseVO> clazz) {
        this.clazz = clazz;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("clazz",      clazz);
        builder.append("alias",      alias);
        builder.append("messageKey", messageKey);
        
        return builder.toString();
    }
    
    @Override
    protected Object clone() {
        try {
            Entity entity     = (Entity)super.clone();
            
            entity.clazz      = this.clazz; 
            entity.alias      = this.alias; 
            entity.messageKey = this.messageKey; 
            
            return entity;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

}
