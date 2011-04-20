package org.opencustomer.webapp.util.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.opencustomer.webapp.Globals;

public class ContextInitialization implements ServletContextListener {

    private final static Logger log = Logger.getLogger(ContextInitialization.class);
    
    public ContextInitialization() {
    }

    public void contextInitialized(ServletContextEvent event) {
        if(log.isDebugEnabled())
            log.debug("initialize context");

        ServletContext context = event.getServletContext();
        
        context.setAttribute(Globals.SESSION_MONITOR_KEY, new SessionMonitor());
    }

    public void contextDestroyed(ServletContextEvent event) {
        if(log.isDebugEnabled())
            log.debug("destroy context");

    }

}
