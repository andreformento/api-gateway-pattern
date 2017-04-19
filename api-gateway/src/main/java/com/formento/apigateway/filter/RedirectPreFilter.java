package com.formento.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.util.HTTPRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.net.MalformedURLException;
import java.net.URL;

import static org.bouncycastle.asn1.ua.DSTU4145NamedCurves.params;

public class RedirectPreFilter extends ZuulFilter {
    private static final Logger LOG = LoggerFactory.getLogger(RedirectPreFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
         return getCurrentContext().getRequest().getRequestURI().matches("/.*");
    }

    @Override
    public Object run() {

        // Groovy example code
        /*
        RequestContext.currentContext.routeHost = new URL("http://example.com");
        if (HTTPRequestUtils.getInstance().getQueryParams() == null) {
            RequestContext.getCurrentContext().setRequestQueryParams(new HashMap<String, List<String>>());
        }
        HTTPRequestUtils.getInstance().getQueryParams().put("debugRequest", ["true"])
        RequestContext.currentContext.setDebugRequest(true)
        RequestContext.getCurrentContext().zuulToZuul = true
        * */


        final RequestContext currentContext = getCurrentContext();
//        final HttpServletRequest request = currentContext.getRequest();
        try {
            final String spec = "http://localhost:9000/api/v2".concat(currentContext.getRequest().getRequestURI());
            LOG.info(String.format("change host to %s", spec));
            currentContext.removeRouteHost();
            final URL routeHost = new URL(spec);
            currentContext.setRouteHost(routeHost);


            currentContext.setRequest(uriComponents);
        } catch (MalformedURLException e) {
            LOG.error("Error when set route host", e);
            throw new UnsupportedOperationException("Something is wrong with your request");
        }


        LOG.info(String.format("%s redirect to %s", currentContext.getRequest().getMethod(), currentContext.getRequest().getRequestURL().toString()));
        return null;
    }

    private RequestContext getCurrentContext() {
        return RequestContext.getCurrentContext();
    }

}
