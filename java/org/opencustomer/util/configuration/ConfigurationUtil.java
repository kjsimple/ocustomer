package org.opencustomer.util.configuration;

import java.util.List;

import org.opencustomer.db.vo.system.ConfigurationVO;

public final class ConfigurationUtil {

    private ConfigurationUtil() {
    }

    public static String getStringValue(ConfigurationKey key, List<ConfigurationVO> configurations) {
        ConfigurationVO vo = getConfiguration(key, configurations);
        
        if(vo.getValue() == null)
            if(key.getDefaultValue() == null)
                return null;
            else
                return (String)key.getDefaultValue();
        else
            return vo.getValue();
    }

    public static int getIntValue(ConfigurationKey key, List<ConfigurationVO> configurations) {
        ConfigurationVO vo = getConfiguration(key, configurations);
        
        if(vo.getValue() == null) {
            if(key.getDefaultValue() == null)
                return 0;
            else
                return (Integer)key.getDefaultValue();
        } else
            return Integer.parseInt(vo.getValue());
    }

    public static boolean getBooleanValue(ConfigurationKey key, List<ConfigurationVO> configurations) {
        ConfigurationVO vo = getConfiguration(key, configurations);
        
        if(vo.getValue() == null) {
            if(key.getDefaultValue() == null)
                return false;
            else
                return (Boolean)key.getDefaultValue();
        } else
            return Boolean.parseBoolean(vo.getValue());
    }
    
    public static ConfigurationVO getConfiguration(ConfigurationKey key, List<ConfigurationVO> configurations) {
        ConfigurationVO configuration = null;
        
        for(ConfigurationVO vo : configurations) {
            if(key.toString().equals(vo.getKey())) {
                configuration = vo;
            }        
        }
        
        if(configuration == null) {
            configuration = new ConfigurationVO();
            configuration.setKey(key.toString());
            if(key.getDefaultValue() != null)
                configuration.setValue(String.valueOf(key.getDefaultValue()));
            
            configurations.add(configuration);
        }
        
        return configuration;
    }
}
