package org.opencustomer.framework.db.util.engine.configuration;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.util.MessageResources;
import org.opencustomer.framework.webapp.util.html.Formatter;

public class StringFormatter implements Formatter {

    private DefaultFormatter defaultFormatter = new DefaultFormatter();
    
    private LinkedHashMap<String, String> messageKeys = new LinkedHashMap<String, String>();
    
    public StringFormatter() {
    }

    public String format(MessageResources resources, Locale locale, Object value) {
        String formattedValue = "";
        
        if(value != null) {
            String messageKey = messageKeys.get(value);
            if(messageKey != null) {
                formattedValue = resources.getMessage(locale, messageKey);
            } else {
                return defaultFormatter.format(resources, locale, value);
            }
        }
        
        return formattedValue;
    }

    public Map<String, String> getMessageKeys() {
        return messageKeys;
    }

    public void setMessageKeys(Map<String, String> messageKeys) {
        this.messageKeys.clear();
        this.messageKeys.putAll(messageKeys);
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("messageKeys", messageKeys);
        
        return builder.toString();
    }
    
    @Override
    public Object clone() {
        try {
            StringFormatter format = (StringFormatter)super.clone();

            format.messageKeys = (LinkedHashMap<String, String>)this.messageKeys.clone();
            
            return format;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

}
