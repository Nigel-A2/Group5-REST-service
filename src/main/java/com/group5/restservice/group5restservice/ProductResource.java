package com.group5.restservice.group5restservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group5.model.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Product resource - an endpoint for CRUD operations on TravelExperts products
 * @author Nate Penner
 * */
@Path("/product")
public class ProductResource {
    private Type mapType;

    public ProductResource() {
        mapType = new TypeToken<HashMap<String, String>>(){}.getType();
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
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getProductList() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select p from Product p");
        List<Product> list = query.getResultList();

        Gson gson = new Gson();
        Type type = new TypeToken<List<Product>>() {
        }.getType();
        entityManager.close();
        return gson.toJson(list, type);
    }

    @Path("/get/{productId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getProduct(@PathParam("productId") int productId) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();

        Product product = manager.find(Product.class, productId);
        manager.close();
        Gson gson = new Gson();

        return gson.toJson(product);
    }

    @Path("/create")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String createProduct(String jsonString) {
        EntityManager manager = Persistence
                .createEntityManagerFactory("default")
                .createEntityManager();

        Gson gson = new Gson();
        Product product = gson.fromJson(jsonString, Product.class);
        manager.getTransaction().begin();
        manager.persist(product);
        manager.getTransaction().commit();
        manager.close();

        return gson.toJson(new HashMap<String, String>() {
            {
                put("message", "success");
            }
        }, mapType);
    }

    @Path("/update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String updateProduct(String jsonString) {
        EntityManager manager = Persistence
                .createEntityManagerFactory("default")
                .createEntityManager();

        Gson gson = new Gson();
        Product product = gson.fromJson(jsonString, Product.class);
        manager.getTransaction().begin();
        Product added = manager.merge(product);

        if (added != null) {
            manager.getTransaction().commit();
            manager.close();

            return gson.toJson(new HashMap<String, String>() {
                {
                    put("message", "success");
                }
            }, mapType);
        } else {
            manager.getTransaction().rollback();
            manager.close();

            return gson.toJson(new HashMap<String, String>() {
                {
                    put("message", "failure");
                }
            }, mapType);
        }
    }

    @DELETE
    @Path("/delete/{ productId }")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteProduct(@PathParam("productId") int productId) {
        EntityManager manager = Persistence
                .createEntityManagerFactory("default")
                .createEntityManager();

        Product product = manager.find(Product.class, productId);
        manager.getTransaction().begin();
        manager.remove(product);

        Gson gson = new Gson();

        if (!manager.contains(product)) {
            manager.getTransaction().commit();
            manager.close();

            return gson.toJson(new HashMap<String, String>() {
                {
                    put("message", "success");
                }
            }, mapType);
        } else {
            manager.getTransaction().rollback();
            manager.close();

            return gson.toJson(new HashMap<String, String>() {
                {
                    put("message", "failure");
                }
            }, mapType);
        }
    }
}
