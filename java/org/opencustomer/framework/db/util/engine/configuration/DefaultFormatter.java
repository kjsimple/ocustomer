package org.opencustomer.framework.db.util.engine.configuration;

import java.util.Locale;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;
import org.opencustomer.framework.webapp.util.html.Formatter;

public class DefaultFormatter implements Formatter {

    private final static String KEY_PATTERN = "\\{key:.+\\}";
    
    public DefaultFormatter() {
    }

    public String format(MessageResources resources, Locale locale, Object value) {
        String formattedValue = "";
        
        if(value != null) {
            String myValue = value.toString();
            
            if(myValue.matches(KEY_PATTERN)) {
                myValue = resources.getMessage(locale, myValue.substring(5,myValue.length()-1));
            }
            
            formattedValue = TagUtils.getInstance().filter(myValue);
        }

        return formattedValue;
    }
    
    @Override
    public Object clone() {
        try {
            DefaultFormatter format   = (DefaultFormatter)super.clone();
            
            return format;
        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
