package com.group5.model;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerId", nullable = false)
    private Integer id;

    @Column(name = "CustFirstName", nullable = false, length = 25)
    private String custFirstName;

    @Column(name = "CustLastName", nullable = false, length = 25)
    private String custLastName;

    @Column(name = "CustAddress", nullable = false, length = 75)
    private String custAddress;

    @Column(name = "CustCity", nullable = false, length = 50)
    private String custCity;

    @Column(name = "CustProv", nullable = false, length = 2)
    private String custProv;

    @Column(name = "CustPostal", nullable = false, length = 7)
    private String custPostal;

    @Column(name = "CustCountry", length = 25)
    private String custCountry;

    @Column(name = "CustHomePhone", length = 20)
    private String custHomePhone;

    @Column(name = "CustBusPhone", nullable = false, length = 20)
    private String custBusPhone;

    @Column(name = "CustEmail", nullable = false, length = 50)
    private String custEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AgentId")
    private Agent agent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustFirstName() {
        return custFirstName;
    }

    public void setCustFirstName(String custFirstName) {
        this.custFirstName = custFirstName;
    }

    public String getCustLastName() {
        return custLastName;
    }

    public void setCustLastName(String custLastName) {
        this.custLastName = custLastName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustCity() {
        return custCity;
    }

    public void setCustCity(String custCity) {
        this.custCity = custCity;
    }

    public String getCustProv() {
        return custProv;
    }

    public void setCustProv(String custProv) {
        this.custProv = custProv;
    }

    public String getCustPostal() {
        return custPostal;
    }

    public void setCustPostal(String custPostal) {
        this.custPostal = custPostal;
    }

    public String getCustCountry() {
        return custCountry;
    }

    public void setCustCountry(String custCountry) {
        this.custCountry = custCountry;
    }

    public String getCustHomePhone() {
        return custHomePhone;
    }

    public void setCustHomePhone(String custHomePhone) {
        this.custHomePhone = custHomePhone;
    }

    public String getCustBusPhone() {
        return custBusPhone;
    }

    public void setCustBusPhone(String custBusPhone) {
        this.custBusPhone = custBusPhone;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

}