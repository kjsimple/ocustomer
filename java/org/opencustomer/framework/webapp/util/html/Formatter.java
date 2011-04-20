package org.opencustomer.framework.webapp.util.html;

import java.util.Locale;

import org.apache.struts.util.MessageResources;

public interface Formatter extends Cloneable {

    public String format(MessageResources resources, Locale locale, Object value);
    
    public Object clone();
}
