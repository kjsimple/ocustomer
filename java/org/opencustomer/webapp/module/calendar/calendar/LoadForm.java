package org.opencustomer.webapp.module.calendar.calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.webapp.action.EditLoadForm;

public final class LoadForm extends EditLoadForm
{
    private static final long serialVersionUID = 1154797393867250985L;

    @Override
    protected void analyzeRequest(ActionMapping mapping, ActionMessages error, HttpServletRequest request)
    {
        super.analyzeRequest(mapping, error, request);
    }
}
