package com.qgs.eatuul.filter.pre;

import org.slf4j.Logger;

import com.qgs.eatuul.constants.FilterType;
import com.qgs.eatuul.filter.EatuulFilter;
import com.qgs.eatuul.http.RequestContext;
import com.qgs.eatuul.log.Log;

public class TestPreFilter extends EatuulFilter{

	@Log
	Logger log;
	
	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return FilterType.PRE;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void run() {
		log.info("=========TestPreFilter Start=========");
		// TODO Auto-generated method stub
		RequestContext context = RequestContext.getCurrentContext();
		System.out.println("Test" + context);
	}

}
