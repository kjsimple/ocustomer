package org.opencustomer.framework.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyUtility
{
    private final static Logger log = Logger.getLogger(PropertyUtility.class);
    
    public static String getPath(Class clazz)
    {
        if(clazz.getPackage() == null)
            return "";
        else
            return clazz.getPackage().getName().replace('.', '/')+'/';
    }
    
    public static Properties getProperties(Class clazz)
    {
        Properties props = new Properties();
        
        String resourceName = getPath(clazz)+clazz.getSimpleName()+".properties";

        if(log.isDebugEnabled())
            log.debug("load properties for class: "+clazz+" ("+resourceName+")");

        try
        {
            InputStream in = clazz.getClassLoader().getResourceAsStream(resourceName);
            props.load(in);
        }
        catch(Exception e)
        {
            log.error("could not load properties for class: "+clazz, e);
        }
        
        if(log.isDebugEnabled())
            log.debug("loaded: "+props);
        
        // trim values
        Enumeration keys = props.keys();
        while(keys.hasMoreElements())
        {
            String key = (String)keys.nextElement();
            props.setProperty(key, props.getProperty(key).trim());
        }
        
        return props;
    }
}