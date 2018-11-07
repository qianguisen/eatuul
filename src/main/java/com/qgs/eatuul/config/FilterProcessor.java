package com.qgs.eatuul.config;
import java.util.List;

import com.qgs.eatuul.filter.ExecutionStatus;
import com.qgs.eatuul.filter.ZuulFilterResult;
import com.qgs.eatuul.http.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qgs.eatuul.exception.ZuulException;
import com.qgs.eatuul.filter.EatuulFilter;

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
     * runs "post" filters which are called after "route" filters. ZuulExceptions from ZuulFilters are thrown.
     * Any other Throwables are caught and a ZuulException is thrown out with a 500 status code
     *
     * @throws ZuulException
     */
    public void postRoute() throws ZuulException {
        try {
            runFilters("post");
        } catch (ZuulException e) {
            throw e;
        } catch (Throwable e) {
            throw new ZuulException(e, 500, "UNCAUGHT_EXCEPTION_IN_POST_FILTER_" + e.getClass().getName());
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
    public void route() throws ZuulException {
        try {
            runFilters("route");
        } catch (ZuulException e) {
            throw e;
        } catch (Throwable e) {
            throw new ZuulException(e, 500, "UNCAUGHT_EXCEPTION_IN_ROUTE_FILTER_" + e.getClass().getName());
        }
    }

    /**
     * runs all "pre" filters. These filters are run before routing to the orgin.
     *
     * @throws ZuulException
     */
    public void preRoute() throws ZuulException {
        try {
            runFilters("pre");
        } catch (ZuulException e) {
            throw e;
        } catch (Throwable e) {
            throw new ZuulException(e, 500, "UNCAUGHT_EXCEPTION_IN_PRE_FILTER_" + e.getClass().getName());
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
                long ltime = System.currentTimeMillis();
                Object result = processEatuulFilter(eatuulFilter);
           //     Object result =  eatuulFilter.run();
               if (result != null && result instanceof Boolean) {
                   bResult |= ((Boolean) result);
               }
                long execTime = System.currentTimeMillis() - ltime;
                logger.info("[{}] execute {}ms", eatuulFilter.getClass().getName(), execTime);
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
    public Object processEatuulFilter(EatuulFilter filter) throws Exception {

        RequestContext ctx = RequestContext.getCurrentContext();
//        boolean bDebug = ctx.debugRouting();
        final String metricPrefix = "zuul.filter-";
        long execTime = 0;
        String filterName = "";
        try {
            long ltime = System.currentTimeMillis();
            filterName = filter.getClass().getSimpleName();

            RequestContext copy = null;
            Object o = null;
            Throwable t = null;

//            if (bDebug) {
//                Debug.addRoutingDebug("Filter " + filter.filterType() + " " + filter.filterOrder() + " " + filterName);
//                copy = ctx.copy();
//            }

            ZuulFilterResult result = filter.runFilter();
            ExecutionStatus s = result.getStatus();
            execTime = System.currentTimeMillis() - ltime;

           switch (s) {
               case FAILED:
                    t = result.getException();
                   break;
                case SUCCESS:
                    o = result.getResult();
                    break;
               default:
                    break;
           }

            if (t != null) throw t;

            return o;

        } catch (Throwable e) {
            if (e instanceof ZuulException) {
                throw (ZuulException) e;
            } else {
                ZuulException ex = new ZuulException(e, "Filter threw Exception", 500, filter.filterType() + ":" + filterName);
                throw ex;
            }
        }
    }


 
}