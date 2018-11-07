package com.qgs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author qianguisen
 * 2018/11/7 16:51
 * @Description: TEST
 */
@ConfigurationProperties("eatuul")
public class ZuulProperties {

    /**
     * Headers that are generally expected to be added by Spring Security, and hence often
     * duplicated if the proxy and the backend are secured with Spring. By default they
     * are added to the ignored headers if Spring Security is present and ignoreSecurityHeaders = true.
     */
    public static final List<String> SECURITY_HEADERS = Arrays.asList("Pragma",
            "Cache-Control", "X-Frame-Options", "X-Content-Type-Options",
            "X-XSS-Protection", "Expires");

    /**
     * A common prefix for all routes.
     */
    private String prefix = "";

    /**
     * Flag saying whether to strip the prefix from the path before forwarding.
     */
    private boolean stripPrefix = true;

    /**
     * Flag for whether retry is supported by default (assuming the routes themselves
     * support it).
     */
    private Boolean retryable = false;

    /**
     * Map of route names to properties.
     */
    private Map<String, ZuulRoute> routes = new LinkedHashMap<>();

    /**
     * Flag to determine whether the proxy adds X-Forwarded-* headers.
     */
    private boolean addProxyHeaders = true;

    /**
     * Flag to determine whether the proxy forwards the Host header.
     */
    private boolean addHostHeader = false;

    /**
     * Set of service names not to consider for proxying automatically. By default all
     * services in the discovery client will be proxied.
     */
    private Set<String> ignoredServices = new LinkedHashSet<>();

    private Set<String> ignoredPatterns = new LinkedHashSet<>();

    /**
     * Names of HTTP headers to ignore completely (i.e. leave them out of downstream
     * requests and drop them from downstream responses).
     */
    private Set<String> ignoredHeaders = new LinkedHashSet<>();

    /**
     * Flag to say that SECURITY_HEADERS are added to ignored headers if spring security is on the classpath.
     * By setting ignoreSecurityHeaders to false we can switch off this default behaviour. This should be used together with
     * disabling the default spring security headers
     * see https://docs.spring.io/spring-security/site/docs/current/reference/html/headers.html#default-security-headers
     */
    private boolean ignoreSecurityHeaders = true;

    /**
     * Flag to force the original query string encoding when building the backend URI in
     * SimpleHostRoutingFilter. When activated, query string will be built using
     * HttpServletRequest getQueryString() method instead of UriTemplate. Note that this
     * flag is not used in RibbonRoutingFilter with services found via DiscoveryClient
     * (like Eureka).
     */
    private boolean forceOriginalQueryStringEncoding = false;

    /**
     * Path to install Zuul as a servlet (not part of Spring MVC). The servlet is more
     * memory efficient for requests with large bodies, e.g. file uploads.
     */
    private String servletPath = "/zuul";

    private boolean ignoreLocalService = true;

    /**
     * Flag to say that request bodies can be traced.
     */
    private boolean traceRequestBody = true;

    /**
     * Flag to say that path elements past the first semicolon can be dropped.
     */
    private boolean removeSemicolonContent = true;

    /**
     * List of sensitive headers that are not passed to downstream requests. Defaults to a
     * "safe" set of headers that commonly contain user credentials. It's OK to remove
     * those from the list if the downstream service is part of the same system as the
     * proxy, so they are sharing authentication data. If using a physical URL outside
     * your own domain, then generally it would be a bad idea to leak user credentials.
     */
    private Set<String> sensitiveHeaders = new LinkedHashSet<>(
            Arrays.asList("Cookie", "Set-Cookie", "Authorization"));

    /**
     * Flag to say whether the hostname for ssl connections should be verified or not. Default is true.
     * This should only be used in test setups!
     */
    private boolean sslHostnameValidationEnabled = true;

    public Set<String> getIgnoredHeaders() {
        Set<String> ignoredHeaders = new LinkedHashSet<>(this.ignoredHeaders);
        if (ClassUtils.isPresent(
                "org.springframework.security.config.annotation.web.WebSecurityConfigurer",
                null) && Collections.disjoint(ignoredHeaders, SECURITY_HEADERS) && ignoreSecurityHeaders) {
            // Allow Spring Security in the gateway to control these headers
            ignoredHeaders.addAll(SECURITY_HEADERS);
        }
        return ignoredHeaders;
    }

    public void setIgnoredHeaders(Set<String> ignoredHeaders) {
        this.ignoredHeaders.addAll(ignoredHeaders);
    }

    @PostConstruct
    public void init() {
        for (Map.Entry<String, ZuulRoute> entry : this.routes.entrySet()) {
            ZuulRoute value = entry.getValue();
            if (!StringUtils.hasText(value.getLocation())) {
                value.serviceId = entry.getKey();
            }
            if (!StringUtils.hasText(value.getId())) {
                value.id = entry.getKey();
            }
            if (!StringUtils.hasText(value.getPath())) {
                value.path = "/" + entry.getKey() + "/**";
            }
        }
    }

    public static class ZuulRoute {

        /**
         * The ID of the route (the same as its map key by default).
         */
        private String id;

        /**
         * The path (pattern) for the route, e.g. /foo/**.
         */
        private String path;

        /**
         * The service ID (if any) to map to this route. You can specify a physical URL or
         * a service, but not both.
         */
        private String serviceId;

        /**
         * A full physical URL to map to the route. An alternative is to use a service ID
         * and service discovery to find the physical address.
         */
        private String url;

        /**
         * Flag to determine whether the prefix for this route (the path, minus pattern
         * patcher) should be stripped before forwarding.
         */
        private boolean stripPrefix = true;

        /**
         * Flag to indicate that this route should be retryable (if supported). Generally
         * retry requires a service ID and ribbon.
         */
        private Boolean retryable;

        /**
         * List of sensitive headers that are not passed to downstream requests. Defaults
         * to a "safe" set of headers that commonly contain user credentials. It's OK to
         * remove those from the list if the downstream service is part of the same system
         * as the proxy, so they are sharing authentication data. If using a physical URL
         * outside your own domain, then generally it would be a bad idea to leak user
         * credentials.
         */
        private Set<String> sensitiveHeaders = new LinkedHashSet<>();

        private boolean customSensitiveHeaders = false;

        public ZuulRoute() {
        }

        public ZuulRoute(String id, String path, String serviceId, String url,
                         boolean stripPrefix, Boolean retryable, Set<String> sensitiveHeaders) {
            this.id = id;
            this.path = path;
            this.serviceId = serviceId;
            this.url = url;
            this.stripPrefix = stripPrefix;
            this.retryable = retryable;
            this.sensitiveHeaders = sensitiveHeaders;
            this.customSensitiveHeaders = sensitiveHeaders != null;
        }
        public ZuulRoute(String text) {
            String location = null;
            String path = text;
            if (text.contains("=")) {
                String[] values = StringUtils
                        .trimArrayElements(StringUtils.split(text, "="));
                location = values[1];
                path = values[0];
            }
            this.id = extractId(path);
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            setLocation(location);
            this.path = path;
        }

        public ZuulRoute(String path, String location) {
            this.id = extractId(path);
            this.path = path;
            setLocation(location);
        }

        public String getLocation() {
            if (StringUtils.hasText(this.url)) {
                return this.url;
            }
            return this.serviceId;
        }

        public void setLocation(String location) {
            if (location != null
                    && (location.startsWith("http:") || location.startsWith("https:"))) {
                this.url = location;
            }
            else {
                this.serviceId = location;
            }
        }

        private String extractId(String path) {
            path = path.startsWith("/") ? path.substring(1) : path;
            path = path.replace("/*", "").replace("*", "");
            return path;
        }

        public void setSensitiveHeaders(Set<String> headers) {
            this.customSensitiveHeaders = true;
            this.sensitiveHeaders = new LinkedHashSet<>(headers);
        }

        public boolean isCustomSensitiveHeaders() {
            return this.customSensitiveHeaders;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isStripPrefix() {
            return stripPrefix;
        }

        public void setStripPrefix(boolean stripPrefix) {
            this.stripPrefix = stripPrefix;
        }

        public Boolean getRetryable() {
            return retryable;
        }

        public void setRetryable(Boolean retryable) {
            this.retryable = retryable;
        }

        public Set<String> getSensitiveHeaders() {
            return sensitiveHeaders;
        }

        public void setCustomSensitiveHeaders(boolean customSensitiveHeaders) {
            this.customSensitiveHeaders = customSensitiveHeaders;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ZuulRoute that = (ZuulRoute) o;
            return customSensitiveHeaders == that.customSensitiveHeaders &&
                    Objects.equals(id, that.id) &&
                    Objects.equals(path, that.path) &&
                    Objects.equals(retryable, that.retryable) &&
                    Objects.equals(sensitiveHeaders, that.sensitiveHeaders) &&
                    Objects.equals(serviceId, that.serviceId) &&
                    stripPrefix == that.stripPrefix &&
                    Objects.equals(url, that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customSensitiveHeaders, id, path, retryable,
                    sensitiveHeaders, serviceId, stripPrefix, url);
        }

        @Override public String toString() {
            return new StringBuilder("ZuulRoute{").append("id='").append(id).append("', ")
                    .append("path='").append(path).append("', ")
                    .append("serviceId='").append(serviceId).append("', ")
                    .append("url='").append(url).append("', ")
                    .append("stripPrefix=").append(stripPrefix).append(", ")
                    .append("retryable=").append(retryable).append(", ")
                    .append("sensitiveHeaders=").append(sensitiveHeaders).append(", ")
                    .append("customSensitiveHeaders=").append(customSensitiveHeaders).append(", ")
                    .append("}").toString();
        }

    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isStripPrefix() {
        return stripPrefix;
    }

    public void setStripPrefix(boolean stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    public Boolean getRetryable() {
        return retryable;
    }

    public void setRetryable(Boolean retryable) {
        this.retryable = retryable;
    }

    public Map<String, ZuulRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, ZuulRoute> routes) {
        this.routes = routes;
    }

    public boolean isAddProxyHeaders() {
        return addProxyHeaders;
    }

    public void setAddProxyHeaders(boolean addProxyHeaders) {
        this.addProxyHeaders = addProxyHeaders;
    }

    public boolean isAddHostHeader() {
        return addHostHeader;
    }

    public void setAddHostHeader(boolean addHostHeader) {
        this.addHostHeader = addHostHeader;
    }

    public Set<String> getIgnoredServices() {
        return ignoredServices;
    }

    public void setIgnoredServices(Set<String> ignoredServices) {
        this.ignoredServices = ignoredServices;
    }

    public Set<String> getIgnoredPatterns() {
        return ignoredPatterns;
    }

    public void setIgnoredPatterns(Set<String> ignoredPatterns) {
        this.ignoredPatterns = ignoredPatterns;
    }

    public boolean isIgnoreSecurityHeaders() {
        return ignoreSecurityHeaders;
    }

    public void setIgnoreSecurityHeaders(boolean ignoreSecurityHeaders) {
        this.ignoreSecurityHeaders = ignoreSecurityHeaders;
    }

    public boolean isForceOriginalQueryStringEncoding() {
        return forceOriginalQueryStringEncoding;
    }

    public void setForceOriginalQueryStringEncoding(
            boolean forceOriginalQueryStringEncoding) {
        this.forceOriginalQueryStringEncoding = forceOriginalQueryStringEncoding;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public boolean isIgnoreLocalService() {
        return ignoreLocalService;
    }
}
