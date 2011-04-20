package org.opencustomer.webapp.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.struts.Action;
import org.opencustomer.framework.webapp.struts.ActionForm;

public abstract class EditSaveAction extends Action
{
    private static Logger log = Logger.getLogger(EditSaveAction.class);

    @Override
    public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if(log.isDebugEnabled())
            log.debug("execute save action "+getPanel(request).getName());
        
        if(!(getPanel(request) instanceof EditPanel)) {
            if(log.isDebugEnabled())
                log.debug("back button use detected");
            
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.backbutton"));
            saveErrors(request, errors);
            
            return Panel.getForward(getPanel(request).getPath(), request);
        }

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        
        EditPanel panel = (EditPanel)getPanel(request);
        
        if(!panel.isEditable()) 
        {
            if(log.isDebugEnabled())
                log.debug("no validation / panel is not editable");
            
            forward = executeForward(panel, errors, mapping, request, response);
        }
        else if (validateData(panel, errors, request))
        {
            if(log.isDebugEnabled())
                log.debug("entity validated: true");
            
            saveEntity(panel, errors, request);
            
            if (errors.isEmpty())
                forward = executeForward(panel, errors, mapping, request, response);
            else
                forward = Panel.getForward(panel.getActivePage().getAction(), request);
        }
        else
        {
            if(log.isDebugEnabled())
                log.debug("entity validated: false");
            
            forward = Panel.getForward(panel.getActivePage().getAction(), request);
        }
        
        if (!errors.isEmpty())
            saveErrors(request, errors);

        if(log.isDebugEnabled())
            log.debug("forward : "+forward);
        
        return forward;
    }
    
    private ActionForward executeForward(EditPanel panel, ActionMessages errors, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response)
    {
        ActionForward forward = null;
        
        forward = getActionForward(mapping, request, response);
        if(forward == null)
        {
            if(Panel.getPanelStack(request).getSize() > 1) {
                if(log.isDebugEnabled())
                    log.debug("remove panel from top of the stack");

                Panel.getPanelStack(request).pop();
            }
            
            forward = Panel.getForward(getPanel(request).getPath(), request);
        }
        
        return forward;
    }
    
    protected abstract void saveEntity(EditPanel panel, ActionMessages errors, HttpServletRequest request);
    
    protected boolean validateData(EditPanel panel, ActionMessages errors, HttpServletRequest request)
    {
        return true;
    }
    
    protected ActionForward getActionForward(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response)
    {
        return null;
    }
    
    protected Panel getPanel(HttpServletRequest request) {
        return Panel.getPanelStack(request).peek();
    }
}
