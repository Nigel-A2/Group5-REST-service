package com.group5.restservice.group5restservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group5.model.Product;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Product resource - an endpoint for CRUD operations on TravelExperts products
 * @author Nate Penner
 * */
@Path("/product")
public class ProductResource {
    private Type mapType;

    public ProductResource() {
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

    /**
     * Gets a list of products
     * @return json string containing a product list
     * */
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

    /**
     * Get product by ID
     * @param productId the product to get info about
     * @return the product data as a json string
     * */
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

    /**
     * Creates a new product
     * @param jsonString The incoming data to be used in product creation
     * @return a response string, depending on what happened
     * */
    @Path("/create")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String createProduct(String jsonString) {
        EntityManager manager = Persistence
                .createEntityManagerFactory("default")
                .createEntityManager();

        Gson gson = new Gson();
        Logger logger = Logger.getGlobal();
        logger.info("================"+jsonString+"==============");
        Product product = gson.fromJson(jsonString, Product.class);
        manager.getTransaction().begin();
        manager.persist(product);
        manager.getTransaction().commit();
        manager.close();

        return gson.toJson(new HashMap<String, Object>() {
            {
                put("message", "success");
            }
        }, mapType);
    }

    /**
     * Updates a product
     * @param jsonString The new product data for the update
     * @return A response string
     * */
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
     * Delete an existing product by its ID
     * @param productId The ID of the product to be deleted
     * @return response string (the result of the deletion, status: success||failure)
     * */
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
            try {
                manager.getTransaction().commit();
            } catch (RollbackException e) {
                manager.close();
                return gson.toJson(new HashMap<String, Object>() {
                    {
                        put("status", "failure");
                        put("rowsAffected", 0);
                        put("err", "integrityConstraintViolation");
                    }
                }, mapType);
            }
            manager.close();

            return gson.toJson(new HashMap<String, Object>() {
                {
                    put("status", "success");
                    put("rowsAffected", 1);
                }
            }, mapType);
        } else {
            manager.getTransaction().rollback();
            manager.close();

            return gson.toJson(new HashMap<String, Object>() {
                {
                    put("status", "failure");
                    put("rowsAffected", 0);
                    put("err", "unknown");
                }
            }, mapType);
        }
    }
}
