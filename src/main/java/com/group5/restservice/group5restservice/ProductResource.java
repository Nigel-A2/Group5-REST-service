package com.group5.restservice.group5restservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group5.model.Product;

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
 * Product resource - an endpoint for CRUD operations on TravelExperts products
 * @author Nate Penner
 * */
@Path("/product")
public class ProductResource {
    public ProductResource() {
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
}
