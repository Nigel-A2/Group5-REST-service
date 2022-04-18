package com.group5.restservice.group5restservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group5.model.Booking;

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

    // this is returning null, is it because customerId has @Basic instead of @ManyToOne?
    @Path("/list/{customerId}")
    // websiteURL/api/booking/list/customerId
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBookingByCustomer(@PathParam("customerId") int customerId)
    {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();

        Booking customer = manager.find(Booking.class, customerId);
        manager.close();
        Gson gson = new Gson();

        return gson.toJson(customer);
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
}
