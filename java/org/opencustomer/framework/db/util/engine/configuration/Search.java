package org.opencustomer.framework.db.util.engine.configuration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.webapp.util.html.Column;


public interface Search extends Cloneable {
    public void load(Column column, ActionMessages errors, HttpServletRequest request);
    
    public void reset();
    
    public Object clone();
}
