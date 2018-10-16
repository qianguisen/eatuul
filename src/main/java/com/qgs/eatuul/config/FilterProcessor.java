package com.qgs.eatuul.config;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qgs.eatuul.filter.EatuulFilter;
import com.qgs.eatuul.http.RequestContext;

/**
 * This the the core class to execute filters.
 *
 * @author qianguisen
 */
public class FilterProcessor {

    static FilterProcessor INSTANCE = new FilterProcessor();
    protected static final Logger logger = LoggerFactory.getLogger(FilterProcessor.class);

    /**
     * @return the singleton FilterProcessor
     */
    public static FilterProcessor getInstance() {
        return INSTANCE;
    }

    /**
     * sets a singleton processor in case of a need to override default behavior
     *
     * @param processor
     */
    public static void setProcessor(FilterProcessor processor) {
        INSTANCE = processor;
    }

    /**
     * runs "post" filters which are called after "route" filters. ZuulExceptions from EatuulFilters are thrown.
     * Any other Throwables are caught and a ZuulException is thrown out with a 500 status code
     *
     * @throws ZuulException
     */
    public void postRoute() throws Exception {
      
            try {
				runFilters("post");
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       
    }

    /**
     * runs all "error" filters. These are called only if an exception occurs. Exceptions from this are swallowed and logged so as not to bubble up.
     */
    public void error() {
        try {
            runFilters("error");
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Runs all "route" filters. These filters route calls to an origin.
     *
     * @throws ZuulException if an exception occurs.
     */
    public void route() throws Exception {
        try {
            runFilters("route");
        } catch (Throwable e) {
        	e.printStackTrace();
        }
    }

    /**
     * runs all "pre" filters. These filters are run before routing to the orgin.
     *
     * @throws ZuulException
     */
    public void preRoute() throws Exception {
        try {
            runFilters("pre");
        } catch (Throwable e) {
            
        }
    }

    /**
     * runs all filters of the filterType sType/ Use this method within filters to run custom filters by type
     *
     * @param sType the filterType.
     * @return
     * @throws Throwable throws up an arbitrary exception
     */
    public Object runFilters(String sType) throws Throwable {
//        if (RequestContext.getCurrentContext().debugRouting()) {
//            Debug.addRoutingDebug("Invoking {" + sType + "} type filters");
//        }
        boolean bResult = false;
        List<EatuulFilter> list = FilterLoader.getInstance().getFiltersByType(sType);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                EatuulFilter eatuulFilter = list.get(i);
          //      Object result = processEatuulFilter(EatuulFilter);
                eatuulFilter.run();
//                if (result != null && result instanceof Boolean) {
//                    bResult |= ((Boolean) result);
//                }
                bResult = true;
            }
        }
        return bResult;
    }

    /**
     * Processes an individual EatuulFilter. This method adds Debug information. Any uncaught Thowables are caught by this method and converted to a ZuulException with a 500 status code.
     *
     * @param filter
     * @return the return value for that filter
     * @throws ZuulException
     */
//    public Object processEatuulFilter(EatuulFilter filter) throws Exception {
//
//        RequestContext ctx = RequestContext.getCurrentContext();
////        boolean bDebug = ctx.debugRouting();
//        final String metricPrefix = "zuul.filter-";
//        long execTime = 0;
//        String filterName = "";
//        try {
//            long ltime = System.currentTimeMillis();
//            filterName = filter.getClass().getSimpleName();
//            
//            RequestContext copy = null;
//            Object o = null;
//            Throwable t = null;
//
////            if (bDebug) {
////                Debug.addRoutingDebug("Filter " + filter.filterType() + " " + filter.filterOrder() + " " + filterName);
////                copy = ctx.copy();
////            }
//            
//            EatuulFilterResult result = filter.runFilter();
//            ExecutionStatus s = result.getStatus();
//            execTime = System.currentTimeMillis() - ltime;
//
//            switch (s) {
//                case FAILED:
//                    t = result.getException();
//                    ctx.addFilterExecutionSummary(filterName, ExecutionStatus.FAILED.name(), execTime);
//                    break;
//                case SUCCESS:
//                    o = result.getResult();
//                    ctx.addFilterExecutionSummary(filterName, ExecutionStatus.SUCCESS.name(), execTime);
//                    if (bDebug) {
//                        Debug.addRoutingDebug("Filter {" + filterName + " TYPE:" + filter.filterType() + " ORDER:" + filter.filterOrder() + "} Execution time = " + execTime + "ms");
//                        Debug.compareContextState(filterName, copy);
//                    }
//                    break;
//                default:
//                    break;
//            }
//            
//            if (t != null) throw t;
//
//            usageNotifier.notify(filter, s);
//            return o;
//
//        } catch (Throwable e) {
//            if (bDebug) {
//                Debug.addRoutingDebug("Running Filter failed " + filterName + " type:" + filter.filterType() + " order:" + filter.filterOrder() + " " + e.getMessage());
//            }
//            usageNotifier.notify(filter, ExecutionStatus.FAILED);
//            if (e instanceof ZuulException) {
//                throw (ZuulException) e;
//            } else {
//                ZuulException ex = new ZuulException(e, "Filter threw Exception", 500, filter.filterType() + ":" + filterName);
//                ctx.addFilterExecutionSummary(filterName, ExecutionStatus.FAILED.name(), execTime);
//                throw ex;
//            }
//        }
//    }


 
}