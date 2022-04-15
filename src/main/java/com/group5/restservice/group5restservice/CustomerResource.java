package com.group5.restservice.group5restservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/customer")
public class CustomerResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
}