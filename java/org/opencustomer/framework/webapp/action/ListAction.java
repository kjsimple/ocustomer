package org.opencustomer.framework.webapp.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.ImageButtonBean;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.struts.Action;

public abstract class ListAction<E extends ListForm> extends Action<E>
{
    private static Logger log = Logger.getLogger(ListAction.class);
    
    public final static String FORM_KEY = "form";
    
    public final static String TABLE_KEY = "table";
    
    public final static String LIST_KEY = "list";

    public final static String LISTSCROLL_KEY = "listScroll";
    
    @Override
    @SuppressWarnings("unchecked")
    public ActionForward execute(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (log.isDebugEnabled())
            log.debug("list action");

        ActionMessages errors = new ActionMessages();
        Panel panel           = Panel.getPanelStack(request).peek();
        boolean formCached    = false;
        
        if(panel == null || !getPanelName().equals(panel.getName())) {
            panel = createPanel(form, request, response);
            panel.setName(getPanelName());
            
            Panel.getPanelStack(request).push(panel);
        }

        if(!form.getDoSearch().isSelected() 
                && !form.getDoResetSearch().isSelected()
                && !isAdditionalButtonUsed(form)
                && form.getSort() == null
                && form.getOrder() == null
                && form.getPage() == 0
                && panel.getAttribute(FORM_KEY) != null) {
            if(log.isDebugEnabled())
                log.debug("load form from panel");
            
            form       = (E)panel.getAttribute(FORM_KEY);
            formCached = true;
            
            request.setAttribute(mapping.getAttribute(), form);
        } else if (panel.getAttribute(FORM_KEY) != null) {
            if(log.isDebugEnabled())
                log.debug("merge form with cached form");
            
            E cacheForm = (E)panel.getAttribute(FORM_KEY);
            
            if(form.getSort() == null)
                form.setSort(cacheForm.getSort());

            if(form.getPage() == 0)
                form.setPage(cacheForm.getPage());
        }

        search(panel, form, formCached, errors, request, response);
        
        form.setDoSearch(new ImageButtonBean());
        form.setDoResetSearch(new ImageButtonBean());
        panel.setAttribute(FORM_KEY, form);
        
        if(!errors.isEmpty())
            this.saveErrors(request, errors);
        
        return findForward(mapping, form, request, response);
    }
    
    protected abstract void search(Panel panel, E form, boolean formCached, ActionMessages errors, HttpServletRequest request, HttpServletResponse response);
    
    protected abstract Panel createPanel(E form, HttpServletRequest request, HttpServletResponse response);
    
    protected final String getPanelName() {
        return this.getClass().getName();
    }
    
    protected Sort extractSort(Panel panel, E form, Sort defaultSort) {
        return ListAction.extractSort(panel, "sort", form.getSort(), defaultSort);
    }
    
    public static Sort extractSort(Panel panel, String sortKey, String value, Sort defaultSort) {
        Sort sort = null;
        
        if (value != null)
            sort = Sort.parseParam(value);
        
        if(sort == null) {
            if(panel.getAttribute(sortKey) != null)
                sort = (Sort)panel.getAttribute(sortKey);
            else
                sort = defaultSort;

            panel.setAttribute(sortKey, sort);
        }
        
        return sort;
    }
    
    protected ActionForward findForward(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return mapping.getInputForward();
    }

    protected boolean isAdditionalButtonUsed(E form) {
        return false;
    }
    
}
