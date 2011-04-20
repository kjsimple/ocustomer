package org.opencustomer.framework.db.dao;

import org.opencustomer.framework.db.util.Page;

public abstract class AbstractDAO
{
    protected final static String adjustWildcards(String input)
    {
        if (input != null)
            return "%"+input.replace('*', '%')+"%";
        else
            return null;
    }

    protected final static String toLower(String input)
    {
        if (input != null)
            return input.toLowerCase();
        else
            return null;
    }

    protected final static int getMaxResults(Page page)
    {
        return (int)page.getStep();
    }

    protected final static int getFirstResult(Page page)
    {
        return (int)((page.getPage() - 1) * page.getStep());
    }
}
