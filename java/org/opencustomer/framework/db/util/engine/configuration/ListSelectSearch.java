package org.opencustomer.framework.db.util.engine.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.db.dao.ListDAO;
import org.opencustomer.framework.db.vo.ListVO;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.html.Column;

public final class ListSelectSearch implements Search {

    private final static Logger log = Logger.getLogger(ListSelectSearch.class);
    
    private LinkedHashMap<Integer, Bean> beans = new LinkedHashMap<Integer, Bean>();
    
    private Class clazz;
    
    private boolean allAvailable;
    
    private Integer value;
    
    private boolean loaded;
    
    private String searchProperty;
    
    public ListSelectSearch() {
    }
    
    public void loadValues() {
        if(!loaded) {
            if(log.isDebugEnabled()) {
                log.debug("load values: "+clazz);
            }
            
            if(allAvailable)
                beans.put(null, new Bean(null, null, "default.select.none"));
            
            try {
                ListDAO<ListVO> dao = (ListDAO<ListVO>)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
                for(ListVO vo : dao.getAll()) {
                    beans.put(vo.getId(), new Bean(vo.getId(), vo.getName(), vo.getNameKey()));
                }
                
                loaded = true; 
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public Map<Integer, Bean> getBeans() {
        if(!loaded)
            throw new RuntimeException("the beans have not been loaded [call: load()]");
            
        return beans;
    }

    public void setBeans(Map<Integer, Bean> beans) {
        this.beans.clear();
        this.beans.putAll(beans);
    }
    
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public boolean isAllAvailable() {
        return allAvailable;
    }

    public void setAllAvailable(boolean allAvailable) {
        this.allAvailable = allAvailable;
    }

    public String getSearchProperty() {
        return searchProperty;
    }

    public void setSearchProperty(String searchProperty) {
        this.searchProperty = searchProperty;
    }

    public void load(Column column, ActionMessages errors, HttpServletRequest request) {
        String strValue = FormUtility.adjustParameter(request.getParameter("search_"+column.getPosition()));
        Integer value = null;
        if(strValue != null)
            value = Integer.parseInt(strValue);
        
        if(value != null) {
            for(Integer id : beans.keySet()) {
                if(id == null) {
                    if(value == null) {
                        reset();
                    }
                } else if(id.equals(value)) {
                    if(log.isDebugEnabled())
                        log.debug("set value: "+id);
                    
                    this.value = id;
                }
            }
        } else {
            reset();
        }
    }
    
    public void reset() {
        if(log.isDebugEnabled())
            log.debug("reset");
        
        if(allAvailable) {
            this.value = null;
        } else {
            for(Integer myValue : this.beans.keySet()) {
                this.value = myValue;
                break;
            }
        }
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("clazz",          clazz);
        builder.append("allAvailable",   allAvailable);
        builder.append("searchProperty", searchProperty);
        builder.append("value",          value);
        builder.append("beans",          beans);
        
        return builder.toString();
    }
    
    @Override
    public Object clone() {
        try {
            ListSelectSearch objectClone = (ListSelectSearch)super.clone();

            objectClone.beans  = (LinkedHashMap<Integer, Bean>)this.beans.clone();
            
            return objectClone;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public static class Bean implements Cloneable {
        private Integer id;
        
        private String message;
        
        private String messageKey;
        
        public Bean() {
        }
        
        public Bean(Integer id, String message, String messageKey) {
            this.id         = id;
            this.message    = message;
            this.messageKey = messageKey;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessageKey() {
            return messageKey;
        }

        public void setMessageKey(String messageKey) {
            this.messageKey = messageKey;
        }

        public String toString() {
            ToStringBuilder builder = new ToStringBuilder(this);
            
            builder.append("id",         id);
            builder.append("message",    message);
            builder.append("messageKey", messageKey);
            
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
