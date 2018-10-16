package com.qgs.eatuul.filter.route;
import org.assertj.core.api.filter.FilterOperator;
import org.slf4j.Logger;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.qgs.eatuul.constants.FilterOrder;
import com.qgs.eatuul.constants.FilterType;
import com.qgs.eatuul.filter.EatuulFilter;
import com.qgs.eatuul.http.RequestContext;
import com.qgs.eatuul.log.Log;

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
    public void run(){
    	log.info("=========RestRoutingFilter Start=========");
        RequestContext ctx = RequestContext.getCurrentContext();
        RequestEntity requestEntity = ctx.getRequestEntity();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,byte[].class);
        ctx.setResponseEntity(responseEntity);
    }


}