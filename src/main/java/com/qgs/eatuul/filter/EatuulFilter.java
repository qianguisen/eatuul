package com.qgs.eatuul.filter;
public abstract class EatuulFilter implements Comparable<EatuulFilter>{

    abstract public String filterType();

    abstract public int filterOrder();

    abstract public void run();
    
	public int compareTo(EatuulFilter filter) {
		return Integer.compare(filterOrder(), filter.filterOrder());
	}
}