package com.group5.restservice.group5restservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group5.model.Package;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Package resource - an endpoint for read operations on TravelExperts packages
 * @author Nigel Awrey
 * */
@Path("/package")
public class PackageResource {
    private final Type mapType;
    public PackageResource()
    {
        mapType = new TypeToken<HashMap<String, Object>>(){}.getType();
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

    /**
     * Creates a new package
     * @author Nate Penner
     * @param pkgData The json data to create the package from
     * */
    @Path("/create")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String createPackage(String pkgData) {
        EntityManager manager = Persistence
                .createEntityManagerFactory("default")
                .createEntityManager();

        Gson gson = new Gson();
        Package pkg = gson.fromJson(pkgData, Package.class);
        manager.getTransaction().begin();
        manager.persist(pkg);

        if (manager.contains(pkg)) {
            manager.getTransaction().commit();
            manager.close();

            return gson.toJson(new LinkedHashMap<String, Object>() {
                {
                    put("message", "success");
                    put("id", pkg.getId());
                }
            }, mapType);
        } else {
            manager.getTransaction().rollback();
            manager.close();

            return gson.toJson(new HashMap<String, Object>() {
                {
                    put("message", "failure");
                }
            }, mapType);
        }
    }

    /**
     * Updates an existing package
     * @author Nate Penner
     * @param pkgData json data to update the package with
     * */
    @Path("/update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String updatePackage(String pkgData) {
        EntityManager manager = Persistence
                .createEntityManagerFactory("default")
                .createEntityManager();

        Gson gson = new Gson();
        Package pkg = gson.fromJson(pkgData, Package.class);
        manager.getTransaction().begin();
        Package added = manager.merge(pkg);

        if (added != null) {
            manager.getTransaction().commit();
            manager.close();

            return gson.toJson(new HashMap<String, Object>() {
                {
                    put("message", "success");
                }
            }, mapType);
        } else {
            manager.getTransaction().rollback();
            manager.close();

            return gson.toJson(new HashMap<String, Object>() {
                {
                    put("message", "failure");
                }
            }, mapType);
        }
    }

    /**
     * Deletes a package
     * @author Nate Penner
     * @param packageId The id of the package to be deleted
     * */
    @Path("/delete/{packageId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePackage(@PathParam("packageId") int packageId) {
        EntityManager manager = Persistence
                .createEntityManagerFactory("default")
                .createEntityManager();

        Package pkg = manager.find(Package.class, packageId);
        Gson gson = new Gson();

        try {
            manager.getTransaction().begin();
            manager.remove(pkg);

            if (!manager.contains(pkg)) {
                manager.getTransaction().commit();
                manager.close();

                return gson.toJson(new HashMap<String, Object>() {
                    {
                        put("message", "success");
                    }
                }, mapType);
            } else {
                manager.getTransaction().rollback();
                manager.close();

                return gson.toJson(new HashMap<String, Object>() {
                    {
                        put("message", "failure");
                    }
                }, mapType);
            }
        } catch (Exception e) {
            return gson.toJson(new HashMap<String, Object>() {
                {
                    put("message", "failure");
                }
            }, mapType);
        }
    }
}
