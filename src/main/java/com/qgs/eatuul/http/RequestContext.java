package com.qgs.eatuul.http;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

public class RequestContext extends ConcurrentHashMap<String, Object> {
    protected static Class<? extends RequestContext> contextClass = RequestContext.class;
    protected static final ThreadLocal<? extends RequestContext> threadLocal = new ThreadLocal<RequestContext>() {
        @Override
        protected RequestContext initialValue() {
            try {
                return contextClass.newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static RequestContext getCurrentContext() {
        RequestContext context = threadLocal.get();
        return context;
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) get("request");
    }

    public void setRequest(HttpServletRequest request) {
        put("request", request);
    }

    public HttpServletResponse getResponse() {
        return (HttpServletResponse) get("response");
    }

    public void setResponse(HttpServletResponse response) {
        set("response", response);
    }


    public void setRequestEntity(RequestEntity requestEntity){
        set("requestEntity",requestEntity);
    }

    public RequestEntity getRequestEntity() {
        return (RequestEntity) get("requestEntity");
    }

    public void setResponseEntity(ResponseEntity responseEntity){
        set("responseEntity",responseEntity);
    }

    public ResponseEntity getResponseEntity() {
        return (ResponseEntity) get("responseEntity");
    }

    /**
     * returns a set throwable
     *
     * @return a set throwable
     */
    public Throwable getThrowable() {
        return (Throwable) get("throwable");

    }

    /**
     * sets a throwable
     *
     * @param th
     */
    public void setThrowable(Throwable th) {
        put("throwable", th);

    }

    public void set(String key, Object value) {
        if (value != null)
            put(key, value);
        else
            remove(key);
    }

    public void unset() {
        threadLocal.remove();
    }

}