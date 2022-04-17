package com.group5.model;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingId", nullable = false)
    private Integer id;

    @Column(name = "BookingDate")
    private Instant bookingDate;

    @Column(name = "BookingNo", length = 50)
    private String bookingNo;

    @Column(name = "TravelerCount")
    private Double travelerCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerId")
    private Customer customer;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "PackageId")
    private Integer _package;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Instant bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public Double getTravelerCount() {
        return travelerCount;
    }

    public void setTravelerCount(Double travelerCount) {
        this.travelerCount = travelerCount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer get_package() {
        return _package;
    }

    public void set_package(Integer _package) {
        this._package = _package;
    }

}