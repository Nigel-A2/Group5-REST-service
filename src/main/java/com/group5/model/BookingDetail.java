package com.group5.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bookingdetails")
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingDetailId", nullable = false)
    private Integer id;

    @Column(name = "ItineraryNo")
    private Double itineraryNo;

    @Column(name = "TripStart")
    private String tripStart;

    @Column(name = "TripEnd")
    private String tripEnd;

    @Lob
    @Column(name = "Description")
    private String description;

    @Lob
    @Column(name = "Destination")
    private String destination;

    @Column(name = "BasePrice", precision = 19, scale = 4)
    private int basePrice;

    @Column(name = "AgencyCommission", precision = 19, scale = 4)
    private int agencyCommission;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "BookingId")
    private Integer bookingId;

    @Column(name = "ProductSupplierId")
    private Integer productSupplierId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getItineraryNo() {
        return itineraryNo;
    }

    public void setItineraryNo(Double itineraryNo) {
        this.itineraryNo = itineraryNo;
    }

    public String getTripStart() {
        return tripStart;
    }

    public void setTripStart(String tripStart) {
        this.tripStart = tripStart;
    }

    public String getTripEnd() {
        return tripEnd;
    }

    public void setTripEnd(String tripEnd) {
        this.tripEnd = tripEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getAgencyCommission() {
        return agencyCommission;
    }

    public void setAgencyCommission(int agencyCommission) {
        this.agencyCommission = agencyCommission;
    }

    public Integer getBooking() {
        return bookingId;
    }

    public void setBooking(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getProductSupplierId() {
        return productSupplierId;
    }

    public void setProductSupplierId(Integer productSupplierId) {
        this.productSupplierId = productSupplierId;
    }

}