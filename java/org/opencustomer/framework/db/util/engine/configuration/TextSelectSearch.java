package org.opencustomer.framework.db.util.engine.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.html.Column;

public final class TextSelectSearch implements Search {

    private LinkedHashMap<String, Bean> beans = new LinkedHashMap<String, Bean>();
    
    private String defaultValue;
    
    private String value;
    
    private boolean hql;
    
    public TextSelectSearch() {
    }
    
    public Map<String, Bean> getBeans() {
        return beans;
    }

    public void setBeans(Map<String, Bean> beans) {
        this.beans.clear();
        this.beans.putAll(beans);
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        
        this.value = defaultValue;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isHql() {
        return hql;
    }

    public void setHql(boolean hql) {
        this.hql = hql;
    }

    public void load(Column column, ActionMessages errors, HttpServletRequest request) {
        String value = FormUtility.adjustParameter(request.getParameter("search_"+column.getPosition()));
        
        if(value != null) {
            for(String e : beans.keySet()) {
                if(e == null) {
                    if(value == null || value.trim().length() == 0) {
                        this.value = null;
                    }
                } else if(e.equals(value)) {
                    this.value = e;
                }
            }
        } else {
            this.value = defaultValue;
        }
    }
    
    public void reset() {
        this.value = defaultValue;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("hql",          hql);
        builder.append("defaultValue", defaultValue);
        builder.append("value",        value);
        builder.append("beans",        beans);
        
        return builder.toString();
    }
    
    @Override
    public Object clone() {
        try {
            TextSelectSearch objectClone = (TextSelectSearch)super.clone();

            objectClone.beans  = (LinkedHashMap<String, Bean>)this.beans.clone();
            
            return objectClone;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public static class Bean implements Cloneable {
        private String messageKey;
        
        private String hql;
        
        public Bean() {
        }
        
        public Bean(String messageKey, String hql) {
            this.messageKey = messageKey;
            this.hql        = hql;
        }
        
        public String getHql() {
            return hql;
        }

        public void setHql(String hql) {
            this.hql = hql;
        }

        public String getMessageKey() {
            return messageKey;
        }

        public void setMessageKey(String messageKey) {
            this.messageKey = messageKey;
        }

        public String toString() {
            ToStringBuilder builder = new ToStringBuilder(this);
            
            builder.append("messageKey", messageKey);
            builder.append("hql", hql);
            
            return builder.toString();
        }

        @Override
        public Object clone() {
            try {
                return super.clone();

            } catch(CloneNotSupportedException e) {
                throw new InternalError();
            }
        }
    }
}
