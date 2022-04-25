package com.group5.restservice.group5restservice;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.group5.model.AgentMin;
import org.mariadb.jdbc.client.result.Result;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
