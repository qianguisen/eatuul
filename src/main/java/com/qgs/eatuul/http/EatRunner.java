package com.qgs.eatuul.http;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qgs.eatuul.config.FilterProcessor;
/**
 * 
 * @Description: 执行filter （动态获取所有filter）
 * @author qianguisen
 * 2018年10月16日
 */
public class EatRunner {
//    //静态写死过滤器
//    private ConcurrentHashMap<String, List<EatuulFilter>> hashFiltersByType = new ConcurrentHashMap<String, List<EatuulFilter>>(){{  
//        put("pre",new ArrayList<EatuulFilter>(){{
//            add(new RequestWrapperFilter());
//        }});
//        put("route",new ArrayList<EatuulFilter>(){{
//            add(new RoutingFilter());
//        }});
//        put("post",new ArrayList<EatuulFilter>(){{
//            add(new SendResponseFilter());
//        }});
//     }};

    public void init(HttpServletRequest req, HttpServletResponse resp) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setRequest(req);
        ctx.setResponse(resp);
    }

    public void preRoute() throws Throwable {
    //    runFilters("pre");  
    	FilterProcessor.getInstance().preRoute();
    }

    public void route() throws Throwable{
     //   runFilters("route");   
    	FilterProcessor.getInstance().route();
    }

    public void postRoute() throws Throwable{
    //    runFilters("post");
    	FilterProcessor.getInstance().postRoute();
    }

//     public void runFilters(String sType) throws Throwable {
//            List<EatuulFilter> list = this.hashFiltersByType.get(sType);
//            if (list != null) {
//                for (int i = 0; i < list.size(); i++) {
//                    EatuulFilter zuulFilter = list.get(i);
//                    zuulFilter.run();
//                }
//            }
//     }
}