package org.opencustomer.framework.webapp.struts;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;

public final class StrutsUtility {

    private StrutsUtility() {
    }
    
    public static String buildContextPath(HttpServletRequest request, String path) 
    {
        StringBuffer value = new StringBuffer();

        // Use our servlet mapping, if one is specified
        String servletMapping =
                (String) request.getSession().getServletContext().getAttribute(
                        Globals.SERVLET_KEY);

        if (servletMapping != null) {

            String queryString = null;
            int question = path.indexOf("?");
            if (question >= 0) {
                queryString = path.substring(question);
            }

            String actionMapping = TagUtils.getInstance().getActionMappingName(path);
            if (servletMapping.startsWith("*.")) {
                value.append(actionMapping);
                value.append(servletMapping.substring(1));

            } else if (servletMapping.endsWith("/*")) {
                value.append(
                        servletMapping.substring(0, servletMapping.length() - 2));
                value.append(actionMapping);

            } else if (servletMapping.equals("/")) {
                value.append(actionMapping);
            }
            if (queryString != null) {
                value.append(queryString);
            }
        }

        // Otherwise, assume extension mapping is in use and extension is
        // already included in the action property
        else {
            if (!path.startsWith("/")) {
                value.append("/");
            }
            value.append(path);
        }

        return value.toString();
    }

}
