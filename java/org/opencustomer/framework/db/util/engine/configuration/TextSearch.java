package org.opencustomer.framework.db.util.engine.configuration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.html.Column;

public final class TextSearch implements Search {

    private String pattern;

    private String value;
    
    public TextSearch() {        
    }
    
    public TextSearch(String pattern) {  
        this.pattern = pattern;
    }
    
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void load(Column column, ActionMessages errors, HttpServletRequest request) {
        this.value = FormUtility.adjustParameter(request.getParameter("search_"+column.getPosition()));
    }
    
    public void reset() {
        this.value = null;
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("pattern", pattern);
        builder.append("value", value);
        
        return builder.toString();
    }
    
    @Override
    public Object clone() {
        try {
            TextSearch objectClone= (TextSearch)super.clone();
            
            return objectClone;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

}
