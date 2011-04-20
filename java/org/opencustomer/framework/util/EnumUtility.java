package org.opencustomer.framework.util;

public final class EnumUtility
{
    private EnumUtility()
    {
    }
    
    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
        return valueOf(enumType, name, null);
    }

    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name, T defaultValue) {
        T foundConstant = null;
        
        T[] constants = enumType.getEnumConstants();
        for(T constant : constants) 
        {
           if(constant.toString().equals(name))
               foundConstant = constant;
        }
        
        if(foundConstant == null && defaultValue != null)
            foundConstant = defaultValue;
            
        return foundConstant;
    }

}
