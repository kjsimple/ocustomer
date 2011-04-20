package org.opencustomer.framework.webapp.action;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.action.ActionForward;
import org.opencustomer.framework.db.vo.BaseVO;

public final class JumpBean
{
    private BaseVO entity;
    
    private ActionForward forward;

    public JumpBean() {
        
    }
    
    public JumpBean(BaseVO entity, ActionForward forward) {
        this.entity = entity;
        this.forward = forward;
    }

    public ActionForward getForward()
    {
        return forward;
    }

    public void setForward(ActionForward forward)
    {
        this.forward = forward;
    }

    public BaseVO getEntity()
    {
        return entity;
    }

    public void setEntity(BaseVO entity)
    {
        this.entity = entity;
    }
 
    @Override
    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("entity", entity);
        builder.append("forward", forward);
        
        return builder.toString();
    }
}
