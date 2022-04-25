package com.group5.restservice.group5restservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

/**
 * An API to product a list of provinces
 * @author Nate Penner
 * */
@Path("/region")
public class RegionResource {
    @Path("/province/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getProvinces() {
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedHashMap<String, String>>(){}.getType();
        return gson.toJson(new LinkedHashMap<String, String>() {
            {
                put("Alberta", "AB");
                put("British Columbia", "BC");
                put("Manitoba", "MB");
                put("New Brunswick", "NB");
                put("Newfoundland and Labrador", "NL");
                put("Northwest Territories", "NT");
                put("Nova Scotia", "NS");
                put("Nunavut", "NU");
                put("Ontario", "ON");
                put("Prince Edward Island", "PE");
                put("Quebec", "QC");
                put("Saskatchewan", "SK");
                put("Yukon", "YT");
            }
        }, type);
    }
}
