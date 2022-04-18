package com.group5.restservice.group5restservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group5.model.Package;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Package resource - an endpoint for CRUD operations on TravelExperts packages
 * @author Nigel Awrey
 * */
// This is supposed to represent a website that customers use
    // so I think we only need read functionality for packages and products
@Path("/package")
public class PackageResource {
    public PackageResource()
    {
        try
        {
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Path("/list")
    // websiteURL/api/package/list
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPackageList() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select p from Package p");
        List<Package> list = query.getResultList();

        Gson gson = new Gson();
        Type type = new TypeToken<List<Package>>() {
        }.getType();
        entityManager.close();
        return gson.toJson(list, type);
    }

    @Path("/get/{packageId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPackage(@PathParam("packageId") int packageId) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();

        Package p = manager.find(Package.class, packageId);
        manager.close();
        Gson gson = new Gson();

        return gson.toJson(p);
    }
}
