package com.formento.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class RedirectPreFilter extends ZuulFilter {
    private static final Logger LOG = LoggerFactory.getLogger(RedirectPreFilter.class);

    @Autowired
    private ProxyRequestHelper helper;

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
        try {
            route();
        } catch (IOException e) {
            LOG.error("Error on router", e);
            throw new UnsupportedOperationException();
        }

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


//
//        final RequestContext currentContext = getCurrentContext();
////        final HttpServletRequest request = currentContext.getRequest();
//        try {
//            final String spec = "http://localhost:9000/api/v2".concat(currentContext.getRequest().getRequestURI());
//            LOG.info(String.format("change host to %s", spec));
//            currentContext.removeRouteHost();
//            final URL routeHost = new URL(spec);
//            currentContext.setRouteHost(routeHost);
//
//
//            currentContext.setRequest(uriComponents);
//        } catch (MalformedURLException e) {
//            LOG.error("Error when set route host", e);
//            throw new UnsupportedOperationException("Something is wrong with your request");
//        }
//
//
//        LOG.info(String.format("%s redirect to %s", currentContext.getRequest().getMethod(), currentContext.getRequest().getRequestURL().toString()));
        return null;
    }

    private RequestContext getCurrentContext() {
        return RequestContext.getCurrentContext();
    }

    private void route() throws IOException {
        OkHttpClient httpClient = new OkHttpClient
                .Builder()
                // customize
                .build();


        RequestContext context = getCurrentContext();
        HttpServletRequest request = context.getRequest();

        String method = request.getMethod();

        final String uri = "http://localhost:9000/api/v2" + this.helper.buildZuulRequestURI(request);

        Headers.Builder headers = new Headers.Builder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Enumeration<String> values = request.getHeaders(name);

            while (values.hasMoreElements()) {
                String value = values.nextElement();
                headers.add(name, value);
            }
        }

        InputStream inputStream = request.getInputStream();

        RequestBody requestBody = null;
        if (inputStream != null && HttpMethod.permitsRequestBody(method)) {
            MediaType mediaType = null;
            if (headers.get("Content-Type") != null) {
                mediaType = MediaType.parse(headers.get("Content-Type"));
            }
            requestBody = RequestBody.create(mediaType, StreamUtils.copyToByteArray(inputStream));
        }

        Request.Builder builder = new Request.Builder()
                .headers(headers.build())
                .url(uri)
                .method(method, requestBody);

        Response response = httpClient.newCall(builder.build()).execute();

        LinkedMultiValueMap<String, String> responseHeaders = new LinkedMultiValueMap<>();

        for (Map.Entry<String, List<String>> entry : response.headers().toMultimap().entrySet()) {
            responseHeaders.put(entry.getKey(), entry.getValue());
        }

        this.helper.setResponse(response.code(), response.body().byteStream(),
                responseHeaders);
        context.setRouteHost(null); // prevent SimpleHostRoutingFilter from running
    }

}
