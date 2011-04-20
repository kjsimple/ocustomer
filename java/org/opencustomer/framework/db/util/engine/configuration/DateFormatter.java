package org.opencustomer.framework.db.util.engine.configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;
import org.opencustomer.framework.webapp.util.html.Formatter;

public class DateFormatter implements Formatter {

    private String patternKey;
    
    public DateFormatter() {
    }
    
    public DateFormatter(String patternKey) {
        this.patternKey = patternKey;
    }

    public String format(MessageResources resources, Locale locale, Object value) {
        String formattedValue = "";
        
        if(value != null) {
            return TagUtils.getInstance().filter(new SimpleDateFormat(resources.getMessage(locale, patternKey), locale).format((Date)value));
        }
        
        return formattedValue;
    }

    public String getPatternKey() {
        return patternKey;
    }

    public void setPatternKey(String patternKey) {
        this.patternKey = patternKey;
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("patternKey", patternKey);
        
        return builder.toString();
    }
    
    @Override
    public Object clone() {
        try {
            DateFormatter format = (DateFormatter)super.clone();
            format.patternKey = this.patternKey;
            
            return format;
        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

}
