package org.opencustomer.framework.util.validator;

public abstract class DomainValidator implements Validator
{
    public final static String OCTET = "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])";

    protected final static String IP_ADDRESS_V4 = "("+OCTET+"\\."+OCTET+"\\."+OCTET+"\\."+OCTET+")";
    
    protected final static String IP_ADDRESS_V6 = "("+OCTET+"\\."+OCTET+"\\."+OCTET+"\\."+OCTET+"\\."+OCTET+"\\."+OCTET+")";

    protected final static String TLD = "(biz|com|info|int|name|net|org|pro|arpa|aero|cat|coop|edu|gov|jobs|mil|mobi|museum|tel|travel|mil|gov|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv" +
        "|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id" +
        "|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu" +
        "|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|um|us|uy|uz|va" +
        "|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw)"; // TODO: missing TLDs (use all)
    
    protected final static String SUB_DOMAIN = "([a-z0-9\\-]+\\.)";
    
    protected final static String LOCAL_NAME = "([a-z0-9\\-]+)";
    
    protected final static String DOMAIN = "("+SUB_DOMAIN+"+"+TLD+")";
    
    protected final static String SERVER = "("+DOMAIN+"|"+LOCAL_NAME+"|"+IP_ADDRESS_V4+"|"+IP_ADDRESS_V6+")";
    
    protected final static String AVAILABLE_CHARS = "([a-z0-9\\-\\._\\,\\'+%\\$#~\\*\\^])";
}
