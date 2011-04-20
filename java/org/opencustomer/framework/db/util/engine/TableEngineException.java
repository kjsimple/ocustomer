package org.opencustomer.framework.db.util.engine;

public class TableEngineException extends RuntimeException {

    private static final long serialVersionUID = -8516667532173890360L;

    public TableEngineException() {
        super();
    }

    public TableEngineException(String message) {
        super(message);
    }

    public TableEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public TableEngineException(Throwable cause) {
        super(cause);
    }

}
