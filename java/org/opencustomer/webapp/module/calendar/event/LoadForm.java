package org.opencustomer.webapp.module.calendar.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.webapp.action.EditLoadForm;

public final class LoadForm extends EditLoadForm
{
    private static final long serialVersionUID = -4341887531852043289L;

    private String date;
    
    @Override
    protected void analyzeRequest(ActionMapping mapping, ActionMessages error, HttpServletRequest request)
    {
        if(date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                sdf.parse(date);
            } catch(ParseException e) {
                date = null;
            }
        }
    }
    
    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
    
    
}
