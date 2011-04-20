package org.opencustomer.framework.util.validator;

/**
 * This class validates a telephone number.
 * @author fbreske
 *
 */
public class TelephoneNumberValidator implements Validator
{
    final static String TELEPHONE_NUMBER = "([0-9+\\.])+";
    
    private static TelephoneNumberValidator telephoneNumberValidator;
    
    public static TelephoneNumberValidator getInstance()
    {
        if(telephoneNumberValidator == null)
            telephoneNumberValidator = new TelephoneNumberValidator();
        return telephoneNumberValidator;
    }
    
    /**
     * @return true if the telephone number format is valid, otherwise false.
     */
    public boolean validate(String telephoneNumber){
        boolean isValid = false;
        
        if(telephoneNumber != null){
            isValid = telephoneNumber.toLowerCase().matches(TELEPHONE_NUMBER);
        }
        
        return isValid;
    }
}
