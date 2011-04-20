/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is the OpenCustomer CRM.
 * 
 * The Initial Developer of the Original Code is Thomas Bader (Bader & Jene
 * Software-Ingenieurbüro). Portions created by the Initial Developer are
 * Copyright (C) 2006 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */
package org.opencustomer.framework.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.opencustomer.framework.db.HibernateContext;

public class HibernateContextFilter extends HibernateContext implements Filter {
    
    private final static Logger log = Logger.getLogger(HibernateContextFilter.class);
    
    private boolean commit = false;
    
    public final void init(FilterConfig config) throws ServletException {
        
        if("true".equalsIgnoreCase(config.getInitParameter("commit")))
            commit = true;
    }
    
    public final void destroy() {

        // there is nothing to do
    }
    
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        getSession();
        
        HttpServletRequest req = (HttpServletRequest)request;
        
        processPreOperation(req);
        
        try {
            chain.doFilter(request, response);
            
            if(commit)
                commitTransaction();
            
            processPostOperation(req);
        } finally {
            closeSession();
        }
    }
    
    protected void processPreOperation(HttpServletRequest request) {
        
    }
    
    protected void processPostOperation(HttpServletRequest request) {
        
    }
}
