package com.qgs.eatuul.filter.route;

import com.qgs.eatuul.constants.FilterOrder;
import com.qgs.eatuul.constants.FilterType;
import com.qgs.eatuul.filter.EatuulFilter;
import com.qgs.eatuul.http.RequestContext;
import com.qgs.eatuul.log.Log;
import org.slf4j.Logger;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
public class RestRoutingFilter extends EatuulFilter{
	@Log
	Logger log;
	
    @Override
    public String filterType() {
        // TODO Auto-generated method stub
        return FilterType.ROUTE;
    }

    @Override
    public int filterOrder() {
        // TODO Auto-generated method stub
        return FilterOrder.RESTROUTINGFILTER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run(){
    	log.info("=========RestRoutingFilter Start=========");
        RequestContext ctx = RequestContext.getCurrentContext();
        RequestEntity requestEntity = ctx.getRequestEntity();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,byte[].class);
        ctx.setResponseEntity(responseEntity);
        return null;
    }


}