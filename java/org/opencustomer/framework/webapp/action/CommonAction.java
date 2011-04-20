package org.opencustomer.framework.webapp.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.struts.Action;

public abstract class CommonAction<E extends CommonForm> extends Action<E>
{
    private static Logger log = Logger.getLogger(CommonAction.class);
        
    @Override
    @SuppressWarnings("unchecked")
    public ActionForward execute(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (log.isDebugEnabled())
            log.debug("common action");

        ActionMessages errors = new ActionMessages();
        Panel panel = Panel.getPanelStack(request).peek();
        
        if(panel == null || !getPanelName().equals(panel.getName())) {
            panel = createPanel(form, request, response);
            panel.setName(getPanelName());
            
            Panel.getPanelStack(request).push(panel);
        }
        
        execute(panel, form, mapping, request, response);
        
        if(!errors.isEmpty())
            this.saveErrors(request, errors);
        
        return findForward(mapping, form, request, response);
    }
    
    protected abstract void execute(Panel panel, E form, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response);
    
    protected abstract Panel createPanel(E form, HttpServletRequest request, HttpServletResponse response);
    
    protected final String getPanelName() {
        return this.getClass().getName();
    }
    
    protected ActionForward findForward(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return mapping.getInputForward();
    }
}
