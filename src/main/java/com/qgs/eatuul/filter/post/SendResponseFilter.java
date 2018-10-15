package com.qgs.eatuul.filter.post;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.qgs.eatuul.filter.EatuulFilter;
import com.qgs.eatuul.http.RequestContext;

public class SendResponseFilter extends EatuulFilter{

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 1000;
    }

    @Override
    public void run() {
        try {
            addResponseHeaders();
            writeResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addResponseHeaders() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse servletResponse = ctx.getResponse();
        ResponseEntity responseEntity = ctx.getResponseEntity();
        HttpHeaders httpHeaders = responseEntity.getHeaders();
        for(Map.Entry<String, List<String>> entry:httpHeaders.entrySet()) {
            String headerName = entry.getKey();
            List<String> headerValues = entry.getValue();
            for(String headerValue:headerValues) {
                servletResponse.addHeader(headerName, headerValue);
            }
        }
    }

    private void writeResponse()throws Exception {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse servletResponse = ctx.getResponse();
        if (servletResponse.getCharacterEncoding() == null) { // only set if not set
            servletResponse.setCharacterEncoding("UTF-8");
        }
        ResponseEntity responseEntity = ctx.getResponseEntity();
        if(responseEntity.hasBody()) {
            byte[] body = (byte[]) responseEntity.getBody();
            ServletOutputStream outputStream = servletResponse.getOutputStream();
            outputStream.write(body);
            outputStream.flush();
        }
    }

}