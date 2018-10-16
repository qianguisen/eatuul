package com.qgs.eatuul.config;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.FilterFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qgs.eatuul.filter.EatuulFilter;

/**
 * This class is one of the core classes in Zuul. It compiles, loads from a File, and checks if source code changed.
 * It also holds ZuulFilters by filterType.
 *
 * @author qianguisen
 */
public class FilterLoader {
    final static FilterLoader INSTANCE = new FilterLoader();

    private static final Logger LOG = LoggerFactory.getLogger(FilterLoader.class);

    private final ConcurrentHashMap<String, Long> filterClassLastModified = new ConcurrentHashMap<String, Long>();
    private final ConcurrentHashMap<String, String> filterClassCode = new ConcurrentHashMap<String, String>();
    private final ConcurrentHashMap<String, String> filterCheck = new ConcurrentHashMap<String, String>();
    private final ConcurrentHashMap<String, List<EatuulFilter>> hashFiltersByType = new ConcurrentHashMap<String, List<EatuulFilter>>();

    private FilterRegistry filterRegistry = FilterRegistry.instance();

   

    // overidden by tests
    public void setFilterRegistry(FilterRegistry r) {
        this.filterRegistry = r;
    }

    
    /**
     * @return Singleton FilterLoader
     */
    public static FilterLoader getInstance() {
        return INSTANCE;
    }

    /**
     * @return the total number of Zuul filters
     */
    public int filterInstanceMapSize() {
        return filterRegistry.size();
    }


 
    /**
     * Returns a list of filters by the filterType specified
     *
     * @param filterType
     * @return a List<ZuulFilter>
     */
    public List<EatuulFilter> getFiltersByType(String filterType) {

        List<EatuulFilter> list = hashFiltersByType.get(filterType);
        if (list != null) return list;

        list = new ArrayList<EatuulFilter>();

        Collection<EatuulFilter> filters = filterRegistry.getAllFilters();
        for (Iterator<EatuulFilter> iterator = filters.iterator(); iterator.hasNext(); ) {
        	EatuulFilter filter = iterator.next();
            if (filter.filterType().equals(filterType)) {
                list.add(filter);
            }
        }
        Collections.sort(list); // sort by priority

        hashFiltersByType.putIfAbsent(filterType, list);
        return list;
    }


}