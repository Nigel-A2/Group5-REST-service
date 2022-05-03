package com.group5.restservice.group5restservice;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Imported
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        containerRequestContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        containerRequestContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        containerRequestContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        containerRequestContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        containerRequestContext.getHeaders().add("Access-Control-Max-Age", "1209600");
    }
}

