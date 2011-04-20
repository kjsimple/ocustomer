package org.opencustomer.framework.webapp.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.webapp.panel.EntityPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.struts.Action;

public abstract class ChooseAction<E extends ChooseForm> extends Action<E>
{
    private static Logger log = Logger.getLogger(ChooseAction.class);
    
    public final static String LIST_KEY = "list";

    public final static String LISTSCROLL_KEY = "listScroll";
    
    @Override
    public final ActionForward execute(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug("choose action");

        EntityPanel panel = null;
        
        if(Panel.getPanelStack(request).isTypeOnTop(Panel.Type.CHOOSE)) {
            panel = (EntityPanel)Panel.getPanelStack(request).peek();
        } else if(!isUnderEditPanel() || Panel.getPanelStack(request).isTypeOnTop(Panel.Type.EDIT)) {
            EntityPanel mainPanel = (EntityPanel)Panel.getPanelStack(request).peek();

            panel = createPanel(mainPanel, form, request, response);
            
            Panel.getPanelStack(request).push(panel);
        } else {
            log.error("missing valid panel: "+Panel.getPanelStack(request).peek());
        }

        boolean back = false;
        ActionMessages errors = new ActionMessages();
        
        if (form.getId() > 0) {
            if (log.isDebugEnabled())
                log.debug("choose entity");
            
            chooseEntity(panel, form, errors, request, response);
            if(errors.isEmpty() && Panel.getPanelStack(request).peek(2) != null)
                back = true;
        }
        else if (form.getDoBack().isSelected()) {
            if (log.isDebugEnabled())
                log.debug("go back");
            
            back = true;
        }

        if (!back)
            search(panel, form, errors, request, response);

        if(!errors.isEmpty())
            this.saveErrors(request, errors);
        
        ActionForward forward = mapping.getInputForward();
        if(back){
            Panel.getPanelStack(request).pop();
            forward = Panel.getForward(Panel.getPanelStack(request).peek().getPath(), request);
        }

        if (log.isDebugEnabled())
            log.debug("forward: "+forward);
        
        return forward;
    }
    
    protected Sort extractSort(Panel panel, E form, int defaultSort) {
        Sort sort = (Sort)panel.getAttribute("sort");
        
        if (form.getSort() != null) {
            sort = Sort.parseParam(form.getSort());
        }
        
        if(sort == null) {
            sort = new Sort(defaultSort, true);
        }
        
        panel.setAttribute("sort", sort);
        
        return sort;
    }
    
    protected abstract void chooseEntity(EntityPanel panel, E form, ActionMessages errors, HttpServletRequest request, HttpServletResponse response);
    
    protected abstract void search(EntityPanel panel, E form, ActionMessages errors, HttpServletRequest request, HttpServletResponse response);
    
    protected abstract EntityPanel createPanel(EntityPanel mainPanel, E form, HttpServletRequest request, HttpServletResponse response);   

    protected boolean isUnderEditPanel() {
        return true;
    }
}
