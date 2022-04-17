package com.group5.restservice.group5restservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group5.model.Customer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


@Path("/customer")
public class CustomerResource {
    // Tomcat won't work without this constructor
    public CustomerResource()
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

    /**
     * Endpoint that retrieves a list of customers
     * @author Nate Penner
     * */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomerList() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select c from Customer c");
        List<Customer> list = query.getResultList();

        Gson gson = new Gson();
        Type type = new TypeToken<List<Customer>>() {
        }.getType();
        entityManager.close();
        return gson.toJson(list, type);
    }

    // this method will retrieve the customer's data based on the ID number specified in the URL
    @GET
    // websiteURL/api/customer/getcustomer/customerId
    @Path("/getcustomer/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomer(@PathParam("customerId") int customerId)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Customer customer = entityManager.find(Customer.class, customerId);
        Gson gson = new Gson();
        entityManager.close();
        return gson.toJson(customer);
    }

    // this method will update the customer's data
    // it must receive data from the website and app in order to update the data accordingly
    @POST
    // websiteURL/api/customer/updatecustomer
    @Path("/updatecustomer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String updateCustomer(String jsonString)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Gson gson = new Gson();
        Customer customer = gson.fromJson(jsonString, Customer.class);
        entityManager.getTransaction().begin();
        Customer mergedCustomer = entityManager.merge(customer);
        if (mergedCustomer != null)
        {
            entityManager.getTransaction().commit();
            entityManager.close();
            return "{ 'message':'Update successful' }";
        }
        else
        {
            entityManager.getTransaction().rollback();
            entityManager.close();
            return "{ 'message':'Update failed' }";
        }
    }

    // this method will create a new customer account and add it to the database
    @PUT
    // websiteURL/api/customer/createcustomer
    @Path("createcustomer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String createCustomer(String jsonString)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Gson gson = new Gson();
        Customer customer = gson.fromJson(jsonString, Customer.class);
        entityManager.getTransaction().begin();
        entityManager.persist(customer);
        entityManager.getTransaction().commit();
        entityManager.close();
        // set the customer id to 0 when making this request
        return "{ 'message':'Insert successful' }";
    }

    // this method will delete a customer account based on the Id number provided
    @DELETE
    // websiteURL/api/customer/deletecustomer/customerId
    @Path("/deletecustomer/{ customerId }")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteCustomer(@PathParam("customerId") int customerId)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Customer customer = entityManager.find(Customer.class, customerId);
        entityManager.getTransaction().begin();
        entityManager.remove(customer);
        if (!entityManager.contains(customer))
        {
            entityManager.getTransaction().commit();
            entityManager.close();
            return "{ 'message':'Delete successful' }";
        }
        else
        {
            entityManager.getTransaction().rollback();
            entityManager.close();
            return "{ 'message':'Delete failed' }";
        }
    }
}