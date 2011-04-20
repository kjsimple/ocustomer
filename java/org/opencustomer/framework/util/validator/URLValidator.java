package org.opencustomer.framework.util.validator;

public final class URLValidator extends DomainValidator
{
    protected final static String PORT = "(:[0-9]+)?";
    
    protected final static String PATH = "(/"+AVAILABLE_CHARS+"+)*/?";
    
    protected final static String PARAMETER = "("+AVAILABLE_CHARS+"+\\="+AVAILABLE_CHARS+"+)";
    
    protected final static String PARAMETER_LIST = "(\\?"+PARAMETER+"(&"+PARAMETER+")*)?";
    
    protected final static String HOMEPAGE = SERVER+PORT+PATH+PARAMETER_LIST;
    
    private static URLValidator instance;
    
    public static URLValidator getInstance() {
        if(instance == null)
            instance = new URLValidator();
        
        return instance;
    }
    
    public boolean validate(String homepage) {
        boolean isValid = false;
        
        if(homepage != null) {
            isValid = homepage.toLowerCase().matches(HOMEPAGE);
        }
        
        return isValid;
    }

    public boolean validateServer(String server) {
        boolean isValid = false;
        
            if(server != null) {
                isValid = server.toLowerCase().matches(SERVER+PORT);
            }    
        
        return isValid;
    }
}
