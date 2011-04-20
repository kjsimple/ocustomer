package org.opencustomer.framework.webapp.panel;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class Action implements Serializable
{
    private static final long serialVersionUID = 381475910328771019L;
    
    private String action;
    
    private boolean selectable;

    public static enum Type
    {
        DELETE,
        SAVE;
    }
    
    public Action()
    {
    }

    public Action(String action, boolean selectable)
    {
        this.action = action;
        this.selectable = selectable;
    }
    
    public final String getAction()
    {
        return action;
    }

    public final void setAction(String action)
    {
        this.action = action;
    }

    public final boolean isSelectable()
    {
        return selectable;
    }

    public final void setSelectable(boolean selectable)
    {
        this.selectable = selectable;
    }
    
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("action", action);
        builder.append("selectable", selectable);
        
        return builder.toString();
    }
}
