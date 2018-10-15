package com.qgs.eatuul.filter.route;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.qgs.eatuul.filter.EatuulFilter;
import com.qgs.eatuul.http.RequestContext;

public class RoutingFilter extends EatuulFilter{

    @Override
    public String filterType() {
        // TODO Auto-generated method stub
        return "route";
    }

    @Override
    public int filterOrder() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void run(){
        RequestContext ctx = RequestContext.getCurrentContext();
        RequestEntity requestEntity = ctx.getRequestEntity();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,byte[].class);
        ctx.setResponseEntity(responseEntity);
    }


}