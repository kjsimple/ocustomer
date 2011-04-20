package org.opencustomer.framework.util.validator;

public final class EmailAddressValidator extends DomainValidator
{
    protected final static String EMAIL_ADDRESS = AVAILABLE_CHARS+"+@"+SERVER;
    
    private static EmailAddressValidator instance;
    
    public static EmailAddressValidator getInstance() {
        if(instance == null)
            instance = new EmailAddressValidator();
        
        return instance;
    }
    
    public boolean validate(String emailAddress) {
        boolean isValid = false;
        
        if(emailAddress != null) {
            isValid = emailAddress.toLowerCase().matches(EMAIL_ADDRESS);
        }
        
        return isValid;
    }
}
