package org.opencustomer.framework.db.util.engine.configuration;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.util.MessageResources;
import org.opencustomer.framework.webapp.util.html.Formatter;

public class EnumFormatter implements Formatter {

    private Class<? extends Enum> clazz;
    
    private LinkedHashMap<Enum, String> messageKeys = new LinkedHashMap<Enum, String>();
    
    public EnumFormatter() {
    }
    
    public EnumFormatter(Class<? extends Enum> clazz) {
        this.clazz = clazz;
    }

    public String format(MessageResources resources, Locale locale, Object value) {
        String formattedValue = "";
        
        if(value != null) {
            String messageKey = messageKeys.get(value);
            if(messageKey != null) {
                if(messageKey.length() > 0) {
                    formattedValue = resources.getMessage(locale, messageKey);
                }
            } else {
                return new DefaultFormatter().format(resources, locale, value);
            }
        }
        
        return formattedValue;
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

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("clazz",       clazz);
        builder.append("messageKeys", messageKeys);
        
        return builder.toString();
    }
    
    @Override
    public Object clone() {
        try {
            EnumFormatter format = (EnumFormatter)super.clone();

            format.clazz       = this.clazz;
            format.messageKeys = (LinkedHashMap<Enum, String>)this.messageKeys.clone();
            
            return format;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

}
