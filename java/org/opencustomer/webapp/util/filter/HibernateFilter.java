package org.opencustomer.webapp.util.filter;

import javax.servlet.http.HttpServletRequest;

import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.webapp.filter.HibernateContextFilter;
import org.opencustomer.webapp.Globals;

public class HibernateFilter extends HibernateContextFilter
{
    @Override
    protected void processPreOperation(HttpServletRequest request)
    {
        UserVO user = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        if(user != null) {
            setAttribute("user", user); 
        }
    }
    
    @Override
    protected void processPostOperation(HttpServletRequest request)
    {
        removeAttribute("user");
    }
}
