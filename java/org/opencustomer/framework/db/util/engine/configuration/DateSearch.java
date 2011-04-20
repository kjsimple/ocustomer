package org.opencustomer.framework.db.util.engine.configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.framework.webapp.util.html.Column;

public final class DateSearch implements Search {

    private String formatKey;

    private Date valueStart;
    
    private Date valueEnd;
    
    public DateSearch() {        
    }
    
    public DateSearch(String formatKey) {  
        this.formatKey = formatKey;
    }
    
    public String getFormatKey() {
        return formatKey;
    }

    public void setFormatKey(String formatKey) {
        this.formatKey = formatKey;
    }

    public Date getValueEnd() {
        return valueEnd;
    }

    public void setValueEnd(Date valueEnd) {
        this.valueEnd = valueEnd;
    }

    public Date getValueStart() {
        return valueStart;
    }

    public void setValueStart(Date valueStart) {
        this.valueStart = valueStart;
    }

    public void load(Column column, ActionMessages errors, HttpServletRequest request) {
        MessageResources resources = (MessageResources) request.getSession().getServletContext().getAttribute(Globals.MESSAGES_KEY);
        Locale locale              = (Locale) request.getSession().getAttribute(Globals.LOCALE_KEY);
        String format              = resources.getMessage(locale, this.getFormatKey());
        
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        String valueStart = FormUtility.adjustParameter(request.getParameter("search_"+column.getPosition()+"_start"));
        if(valueStart != null) {
            try {
                this.valueStart = sdf.parse(valueStart);
            } catch(ParseException e) {
                errors.add("search_"+column.getPosition(), new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, column.getMessageKey()), format));
            }
        }
        
        String valueEnd = FormUtility.adjustParameter(request.getParameter("search_"+column.getPosition()+"_end"));
        if(valueEnd != null) {
            try {
                this.valueEnd = sdf.parse(valueEnd);
            } catch(ParseException e) {
                if(errors.size("search_"+column.getPosition()) == 0)
                    errors.add("search_"+column.getPosition(), new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, column.getMessageKey()), format));
            }
        }
        
        if(this.valueStart != null && this.valueEnd != null) { 
            // TODO sollte auch noch mal abgefangen werden
        }
    }
    
    public void reset() {
        this.valueStart = null;
        this.valueEnd   = null;
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("formatKey", formatKey);
        builder.append("valueStart", valueStart);
        builder.append("valueEnd", valueEnd);
        
        return builder.toString();
    }
    
    @Override
    public Object clone() {
        try {
            DateSearch objectClone= (DateSearch)super.clone();
            
            if(this.valueStart != null)
                objectClone.valueStart = (Date)this.valueStart.clone();
            if(this.valueEnd != null)
                objectClone.valueEnd =   (Date)this.valueEnd.clone();
            
            return objectClone;

        } catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

}
