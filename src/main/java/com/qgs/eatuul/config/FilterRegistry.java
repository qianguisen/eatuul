package com.qgs.eatuul.config;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.qgs.eatuul.filter.EatuulFilter;

/**
 * 
 * @Description: 注册filter
 * @author qianguisen
 * 2018年10月16日
 */
public class FilterRegistry {

    private static final FilterRegistry INSTANCE = new FilterRegistry();

    public static final FilterRegistry instance() {
        return INSTANCE;
    }

    private final ConcurrentHashMap<String, EatuulFilter> filters = new ConcurrentHashMap<String, EatuulFilter>();

    private FilterRegistry() {
    }

    public EatuulFilter remove(String key) {
        return this.filters.remove(key);
    }

    public EatuulFilter get(String key) {
        return this.filters.get(key);
    }

    public void put(String key, EatuulFilter filter) {
        this.filters.putIfAbsent(key, filter);
    }

    public int size() {
        return this.filters.size();
    }

    public Collection<EatuulFilter> getAllFilters() {
        return this.filters.values();
    }

}