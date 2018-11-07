package com.qgs.eatuul.filter;

public abstract class EatuulFilter implements Comparable<EatuulFilter> {

    abstract public String filterType();

    abstract public int filterOrder();

    abstract public Object run();

    abstract public boolean shouldFilter();

    /**
     * runFilter checks !isFilterDisabled() and shouldFilter(). The run() method is invoked if both are true.
     *
     * @return the return from ZuulFilterResult
     */
    public ZuulFilterResult runFilter() {
        ZuulFilterResult zr = new ZuulFilterResult();

        if (shouldFilter()) {
            try {
                Object res = run();
                zr = new ZuulFilterResult(res, ExecutionStatus.SUCCESS);
            } catch (Throwable e) {
                zr = new ZuulFilterResult(ExecutionStatus.FAILED);
                zr.setException(e);
            } finally {
            }
        } else {
            zr = new ZuulFilterResult(ExecutionStatus.SKIPPED);
        }

        return zr;
    }

    public int compareTo(EatuulFilter filter) {
        return Integer.compare(filterOrder(), filter.filterOrder());
    }
}