package com.qgs.eatuul.http;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qgs.eatuul.filter.EatuulFilter;
import com.qgs.eatuul.filter.post.SendResponseFilter;
import com.qgs.eatuul.filter.pre.RequestWrapperFilter;
import com.qgs.eatuul.filter.route.RoutingFilter;

public class EatRunner {
    //静态写死过滤器
    private ConcurrentHashMap<String, List<EatuulFilter>> hashFiltersByType = new ConcurrentHashMap<String, List<EatuulFilter>>(){{  
        put("pre",new ArrayList<EatuulFilter>(){{
            add(new RequestWrapperFilter());
        }});
        put("route",new ArrayList<EatuulFilter>(){{
            add(new RoutingFilter());
        }});
        put("post",new ArrayList<EatuulFilter>(){{
            add(new SendResponseFilter());
        }});
     }};

    public void init(HttpServletRequest req, HttpServletResponse resp) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setRequest(req);
        ctx.setResponse(resp);
    }

    public void preRoute() throws Throwable {
        runFilters("pre");  
    }

    public void route() throws Throwable{
        runFilters("route");    
    }

    public void postRoute() throws Throwable{
        runFilters("post");
    }

     public void runFilters(String sType) throws Throwable {
            List<EatuulFilter> list = this.hashFiltersByType.get(sType);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    EatuulFilter zuulFilter = list.get(i);
                    zuulFilter.run();
                }
            }
     }
}