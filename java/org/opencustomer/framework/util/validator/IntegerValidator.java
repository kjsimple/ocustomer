package org.opencustomer.framework.util.validator;

public class IntegerValidator implements Validator {

    private Integer minInteger;
    
    private Integer maxInteger;

    public IntegerValidator() {
        this(null);
    }

    public IntegerValidator(Integer minInteger) {
        this(minInteger, null);
    }

    public IntegerValidator(Integer minInteger, Integer maxInteger) {
        this.minInteger = minInteger;
        this.maxInteger = maxInteger;
    }
    
    public boolean validate(String validatedString) {
        boolean valid = false;
        
        if(minInteger == null && maxInteger == null) {
           try {
               Integer.parseInt(validatedString);
               valid = true;
           } catch(NumberFormatException e) {
           }
        } else if(minInteger != null && maxInteger == null){
            try {
                int value = Integer.parseInt(validatedString);
                if(value >= minInteger.intValue())
                    valid = true;
            } catch(NumberFormatException e) {
            }
        } else if(minInteger != null && maxInteger != null){
            try {
                int value = Integer.parseInt(validatedString);
                if(value >= minInteger.intValue() && value <= maxInteger.intValue())
                    valid = true;
            } catch(NumberFormatException e) {
            }
        }
        
        return valid;
    }
}
