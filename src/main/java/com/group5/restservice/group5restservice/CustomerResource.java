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
import java.util.List;

@Path("/customer")
public class CustomerResource {
    // Tomcat won't work without this constructor
    public CustomerResource()
    {
        try
        {
            Class.forName("org.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomers() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select c from Customer c");
    }*/

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
}