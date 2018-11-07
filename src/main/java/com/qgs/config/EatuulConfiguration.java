package com.qgs.config;

import java.util.Map;

import com.qgs.eatuul.filter.error.SendErrorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qgs.eatuul.config.FilterLoader;
import com.qgs.eatuul.config.FilterRegistry;
import com.qgs.eatuul.config.ZuulFilterInitializer;
import com.qgs.eatuul.filter.EatuulFilter;
import com.qgs.eatuul.filter.post.SendResponseFilter;
import com.qgs.eatuul.filter.pre.RequestWrapperFilter;
import com.qgs.eatuul.filter.pre.TestPreFilter;
import com.qgs.eatuul.filter.route.RestRoutingFilter;
/**
 * 
 * @Description: 网关配置 
 * @author qianguisen
 * 2018年10月16日
 */
@Configuration
@EnableConfigurationProperties({ ZuulProperties.class })
public class EatuulConfiguration {
	
	@Bean
	public RequestWrapperFilter requestWrapperFilter() {
		return new RequestWrapperFilter();
	}
	
	@Bean
	public TestPreFilter testPreFilter() {
		return new TestPreFilter();
	}
	
	@Bean
	public RestRoutingFilter routingFilter() {
		return new RestRoutingFilter();
	}
	
	@Bean
	public SendResponseFilter sendResponseFilter() {
		return new SendResponseFilter();
	}

    @Bean
    public SendErrorFilter sendErrorFilter() {
        return new SendErrorFilter();
    }

	@Configuration
	protected static class ZuulFilterConfiguration {

		@Autowired
		private Map<String, EatuulFilter> filters;

		@Bean
		public ZuulFilterInitializer zuulFilterInitializer() {
			FilterLoader filterLoader = FilterLoader.getInstance();
			FilterRegistry filterRegistry = FilterRegistry.instance();
			return new ZuulFilterInitializer(this.filters, filterLoader, filterRegistry);
		}

	}
	
}
