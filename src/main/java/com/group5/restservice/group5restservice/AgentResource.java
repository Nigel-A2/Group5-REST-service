package com.group5.restservice.group5restservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group5.model.AgentMin;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Minimal resource for getting agent list item data
 * @author Nate Penner
 * */
@Path("/agent")
public class AgentResource {
    public AgentResource() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Path("/listitem")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAgents() {
        EntityManager manager = Persistence.createEntityManagerFactory("default")
                .createEntityManager();

        List<AgentMin> agent = manager.createQuery("select a from AgentMin a").getResultList();
        Gson gson = new Gson();
        Type type = new TypeToken<List<AgentMin>>(){}.getType();

        return gson.toJson(agent, type);
    }
}
