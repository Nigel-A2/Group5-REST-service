package com.group5.model;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingId", nullable = false)
    private Integer bookingId;

    @Column(name = "BookingDate")
    private String bookingDate; // was type Instant

    @Column(name = "BookingNo", length = 50)
    private String bookingNo;

    @Column(name = "TravelerCount")
    private Integer travelerCount;

    @Basic(fetch = FetchType.LAZY) // was ManyToOne
    @Column(name = "CustomerId") // was JoinColumn
    private Integer customerId;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "PackageId")
    private Integer packageId;

    public Integer getId() {
        return bookingId;
    }

    public void setId(Integer id) {
        this.bookingId = id;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String  bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public Integer getTravelerCount() {
        return travelerCount;
    }

    public void setTravelerCount(Integer travelerCount) {
        this.travelerCount = travelerCount;
    }

    public Integer getCustomer() {
        return customerId;
    }

    public void setCustomer(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer get_package() {
        return packageId;
    }

    public void set_package(Integer packageId) {
        this.packageId = packageId;
    }

}