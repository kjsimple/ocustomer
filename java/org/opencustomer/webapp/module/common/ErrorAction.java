package org.opencustomer.webapp.module.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.opencustomer.framework.webapp.struts.Action;
import org.opencustomer.framework.webapp.struts.ActionForm;

public class ErrorAction extends Action
{

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        return mapping.getInputForward();
    }

}
