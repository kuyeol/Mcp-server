package org.acme.panache.exception;

public class ModelException extends RuntimeException {

    private Object[] parameters;

    public ModelException() {
    }

    public ModelException(String message) {
        super(message);
    }

    public ModelException(String message, Object ... parameters) {
        super(message);
        this.parameters = parameters;
    }

    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}