package org.opencustomer.framework.db.hibernate;

import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.event.FlushEntityEvent;
import org.hibernate.event.def.DefaultFlushEntityEventListener;

public class IgnoreDirtyPropertiesListener extends DefaultFlushEntityEventListener {
    
    private static final long serialVersionUID = -4543093247808255749L;

    private final static Logger log = Logger.getLogger(IgnoreDirtyPropertiesListener.class);
    
    protected void dirtyCheck(FlushEntityEvent event) throws HibernateException {
        super.dirtyCheck(event);
        
        if ((event.getDirtyProperties() != null) && areOnlyIgnoredPropertiesDirty(event)) {
            //We only have dirty properties which should be ignored
            event.setDirtyProperties(null);

            if(log.isDebugEnabled())
                log.debug("suppress dirty for "+event.getEntity());
        }
    }
    
    private static boolean areOnlyIgnoredPropertiesDirty(FlushEntityEvent event) {
        boolean ignoreDirty = false;
        
        //Are there any properties to ignore configured for the current class?
        if (event.getEntity() instanceof IgnoreDirtyProperties) {
            Set<String> propsToIgnore = ((IgnoreDirtyProperties)event.getEntity()).getPropertiesToIgnoreOnDirtyCheck();
            int i = 0;
            String propName;
            String[] propertyNames = event.getEntityEntry().getPersister().getPropertyNames();
            
            ignoreDirty = true;
            
            //Loop through dirty properties
            while ((i < event.getDirtyProperties().length) && ignoreDirty) {
                propName = propertyNames[event.getDirtyProperties()[i++]];

                if (!propsToIgnore.contains(propName)) {
                    //This property is dirty but not on the ignore list
                    ignoreDirty = false;
                }
            }
        }
        
        return ignoreDirty;
    }
}