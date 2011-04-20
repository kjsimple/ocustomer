package org.opencustomer.framework.webapp.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class DownloadAction<E extends org.opencustomer.framework.webapp.struts.ActionForm> extends org.apache.struts.actions.DownloadAction {

    private final static Logger log = Logger.getLogger(DownloadAction.class);
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forwardAction = null;

        try {
            forwardAction = super.execute(mapping, form, request, response);
        } catch (Throwable e) {
            log.error("unexpected error", e);
            forwardAction = mapping.findForward("error");
        }

        return forwardAction;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected final StreamInfo getStreamInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getStreamInfo(mapping, (E) form, request, response);
    }
    
    protected abstract StreamInfo getStreamInfo(ActionMapping mapping, E form, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
