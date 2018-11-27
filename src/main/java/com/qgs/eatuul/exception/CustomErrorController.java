package com.qgs.eatuul.exception;


import com.qgs.eatuul.http.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: TODO
 * @author: qianguisen
 * @Date: 2018/11/8 9:59
 **/
@Controller
public class CustomErrorController implements ErrorController {
    private static final String DEFAULT_CODE = "999999";
    private static final String DEFAULT_MSG = "exist error";
    private static final String UNDEFINED_ERROR = "undefined.error";
    private static final String DEFAULT_CTX_ERROR = "javax.servlet.error.exception";
    private static final String DEFAULT_CTX_ERROR_MSG = "javax.servlet.error.message";
    private static final String DEFAULT_CHARSET_NAME = "UTF-8";


    @RequestMapping("/error")
        @ResponseBody
        public OutboundPacket Json(HttpServletResponse response) throws ValidationRuntimeException{
            RequestContext context = RequestContext.getCurrentContext();
            HttpServletRequest request = context.getRequest();
            String message = DEFAULT_MSG;
        String code = DEFAULT_CODE;

        if(request == null){
            return resolveOutbound(code, message, DEFAULT_CHARSET_NAME, null, null);
        }

        Object t = request.getAttribute(DEFAULT_CTX_ERROR);
        Object[] args = null;
        if(t != null){
            if(request.getAttribute(DEFAULT_CTX_ERROR_MSG) != null){
               message = (String) request.getAttribute(DEFAULT_CTX_ERROR_MSG);
            }
            if(t instanceof ZuulException){
                if(((ZuulException) t).getCause() instanceof ValidationRuntimeException){
                    ValidationRuntimeException ex = (ValidationRuntimeException)((ZuulException) t).getCause();
                    message = ex.getMessage();
                    code = ex.getMessage();
                    args = ex.getArgs();
                }else{
                    message = ((ZuulException) t).getMessage();
                }
            }
            if(t instanceof ValidationRuntimeException){
                ValidationRuntimeException ex = (ValidationRuntimeException) t;
                message = ex.getMessage();
                code = ex.getMessage();
                args = ex.getArgs();
            }
        }

//        message = resolveMessage(message, args);
        OutboundPacket outboundPacket = resolveOutbound(code, message, request.getHeader(Dict.CHSET_H),
                request.getParameter(Dict.SEQUENCEID_H), String.valueOf(context.get(Dict.JNLID)));
        return outboundPacket;
    }

    private OutboundPacket resolveOutbound(String code, String message, String charset, String seq, String jnlId) {
        OutboundPacket outboundPacket = new OutboundPacket();
        outboundPacket.setCode(code);
        outboundPacket.setMessage(message);
        outboundPacket.setCharset(charset);
        outboundPacket.setTimestamp(System.currentTimeMillis());
        outboundPacket.setChannelSerialNo(seq);
        outboundPacket.setSerialNo(jnlId);
        return outboundPacket;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }


}
