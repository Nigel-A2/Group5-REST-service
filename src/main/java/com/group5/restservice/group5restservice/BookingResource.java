package com.group5.restservice.group5restservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group5.model.Booking;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.List;

@Path("/booking")
public class BookingResource {
    public BookingResource()
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
    // websiteURL/api/booking/list
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBookingList() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select b from Booking b");
        List<Booking> list = query.getResultList();

        Gson gson = new Gson();
        Type type = new TypeToken<List<Booking>>() {
        }.getType();
        entityManager.close();
        return gson.toJson(list, type);
    }


    @Path("/get/{bookingId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBooking(@PathParam("bookingId") int bookingId)
    {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();

        Booking booking = manager.find(Booking.class, bookingId);
        manager.close();
        Gson gson = new Gson();

        return gson.toJson(booking);
    }

    /**
     * Get a customer's bookings from their ID
     * @author Nate Penner
     * */
    @Path("/get/by-customer/{customerId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBookingsByCustomer(@PathParam("customerId") int customerId) {
        EntityManager manager = Persistence.createEntityManagerFactory("default")
                .createEntityManager();
        List<Booking> bookingsList = manager.createQuery("select b from Booking b where b.customerId=?1", Booking.class)
                .setParameter(1, customerId)
                .getResultList();

        Gson gson = new Gson();
        Type type = new TypeToken<List<Booking>>(){}.getType();
        return gson.toJson(bookingsList, type);
    }


    // this method will update the booking's data
    // it must receive data from the website and app in order to update the data accordingly
    @POST
    // websiteURL/api/booking/update
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String updateBooking(String jsonString)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Gson gson = new Gson();
        Booking booking = gson.fromJson(jsonString, Booking.class);
        entityManager.getTransaction().begin();
        Booking mergedBooking = entityManager.merge(booking);
        if (mergedBooking != null)
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

    // this method will create a new booking and add it to the database
    @PUT
    // websiteURL/api/booking/create
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public String createBooking(String jsonString)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Gson gson = new Gson();
        Booking booking = gson.fromJson(jsonString, Booking.class);
        entityManager.getTransaction().begin();
        entityManager.persist(booking);
        entityManager.getTransaction().commit();
        entityManager.close();
        // set the booking id to 0 or null when making this request
        return "{ 'message':'Insert successful' }";
    }

    // this method will delete a booking based on the Id number provided
    @DELETE
    // websiteURL/api/booking/delete/bookingId
    @Path("/delete/{ bookingId }")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteBooking(@PathParam("bookingId") int bookingId)
    {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Booking booking = entityManager.find(Booking.class, bookingId);
        entityManager.getTransaction().begin();
        entityManager.remove(booking);
        if (!entityManager.contains(booking))
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
