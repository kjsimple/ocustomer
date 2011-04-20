package org.opencustomer.framework.webapp.util.html;

import org.opencustomer.framework.db.util.Page;

public interface Pagination {
    public long getCount();
    
    public Page getPage();
}
