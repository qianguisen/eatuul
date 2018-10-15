package com.qgs.eatuul.filter;
public abstract class EatuulFilter {

    abstract public String filterType();

    abstract public int filterOrder();

    abstract public void run();
}