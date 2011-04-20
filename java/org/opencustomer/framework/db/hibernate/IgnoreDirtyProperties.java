package org.opencustomer.framework.db.hibernate;

import java.util.Set;

public interface IgnoreDirtyProperties
{
    public Set<String> getPropertiesToIgnoreOnDirtyCheck();
}
