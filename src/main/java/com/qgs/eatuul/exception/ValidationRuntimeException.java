package com.qgs.eatuul.exception;

/**
 * @author awlwen
 * @since 2017/5/16.
 */
public class ValidationRuntimeException extends RuntimeException{
    private Object[] args;

    public ValidationRuntimeException() {
    }

    public ValidationRuntimeException(String message) {
        super(message);
    }

    public ValidationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationRuntimeException(String message, Object[] args) {
        super(message);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

}
