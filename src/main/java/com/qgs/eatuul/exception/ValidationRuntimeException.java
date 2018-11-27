package com.qgs.eatuul.exception;

/**
 * @Description: TODO
 * @author: qianguisen
 * @Date: 2018/11/8 10:35
 **/
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
