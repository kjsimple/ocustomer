package org.opencustomer.framework.db.util.engine.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.html.Column;

public final class EnumSearch implements Search {

    private Class<? extends Enum> clazz;
    
    private LinkedHashMap<Enum, String> messageKeys = new LinkedHashMap<Enum, String>();
    
    private Enum defaultValue;
    
    private Enum value;
    
    public EnumSearch() {
    }
    
    public EnumSearch(Class<? extends Enum> clazz) {
        this.clazz = clazz;
    }

    public Map<Enum, String> getMessageKeys() {
        return messageKeys;
    }

    public void setMessageKeys(Map<Enum, String> messageKeys) {
        this.messageKeys.clear();
        this.messageKeys.putAll(messageKeys);
    }
    
    public Class<? extends Enum> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends Enum> clazz) {
        this.clazz = clazz;
    }
    
    public Enum getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(Enum defaultValue) {
        this.defaultValue = defaultValue;
        
        this.value = defaultValue;
    }
    
    public Enum getValue() {
        return value;
    }

    public void setValue(Enum value) {
        this.value = value;
    }

    public void load(Column column, ActionMessages errors, HttpServletRequest request) {
        String value = FormUtility.adjustParameter(request.getParameter("search_"+column.getPosition()));
        
        if(value != null) {
            for(Enum e : messageKeys.keySet()) {
                if(e == null) {
                    if(value == null || value.trim().length() == 0) {
                        this.value = null;
                    }
                } else if(e.name().equals(value)) {
                    this.value = e;
                }
            }
        } else {
            this.value = null;
        }
    }
    
    public void reset() {
        this.value = defaultValue;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("clazz",       clazz);
        builder.append("messageKeys", messageKeys);
        builder.append("defaultValue", defaultValue);
        builder.append("value", value);
        
        return builder.toString();
    }
    
    @Override
    public Object clone() {
        try {
            EnumSearch objectClone = (EnumSearch)super.clone();

            objectClone.clazz        = this.clazz;
            objectClone.messageKeys  = (LinkedHashMap<Enum, String>)this.messageKeys.clone();
            
            return objectClone;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

}
