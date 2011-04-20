package org.opencustomer.db.listener;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.def.DefaultSaveOrUpdateEventListener;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;

public final class InsertOrUpdateListener extends DefaultSaveOrUpdateEventListener {

    private static final long serialVersionUID = 851039360879873520L;
    
    private final static Logger log = Logger.getLogger(InsertOrUpdateListener.class);
    
    @Override
    protected Serializable performSaveOrUpdate(SaveOrUpdateEvent event)
    {
        if(event.getEntity() instanceof InsertOrUpdate) {
            InsertOrUpdate entity = (InsertOrUpdate)event.getEntity();
            
            UserVO user = (UserVO)HibernateContext.getAttribute("user");
            
            entity.onInsertOrUpdate(user);
        }
        
        return super.performSaveOrUpdate(event);
    }
}