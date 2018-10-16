package com.qgs.eatuul.config;
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.util.ReflectionUtils;

import com.qgs.eatuul.filter.EatuulFilter;

/**
 * Initializes various Zuul components including {@link ZuulFilter}.
 *
 * @author qianguisen
 *
 */

public class ZuulFilterInitializer implements ServletContextListener {

	private final Map<String, EatuulFilter> filters;
	private final FilterLoader filterLoader;
	private final FilterRegistry filterRegistry;

	public ZuulFilterInitializer(Map<String, EatuulFilter> filters,
								 FilterLoader filterLoader,
								 FilterRegistry filterRegistry) {
		this.filters = filters;
		this.filterLoader = filterLoader;
		this.filterRegistry = filterRegistry;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {

//		log.info("Starting filter initializer context listener");


		for (Map.Entry<String, EatuulFilter> entry : this.filters.entrySet()) {
			filterRegistry.put(entry.getKey(), entry.getValue());
		}
	}


	private void clearLoaderCache() {
		Field field = ReflectionUtils.findField(FilterLoader.class, "hashFiltersByType");
		ReflectionUtils.makeAccessible(field);
		@SuppressWarnings("rawtypes")
		Map cache = (Map) ReflectionUtils.getField(field, filterLoader);
		cache.clear();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}