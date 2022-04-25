package com.group5.model;

import javax.persistence.*;

@Entity
@Table(name = "agents")
public class AgentMin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AgentId", nullable = false)
    private Integer id;

    @Column(name = "AgtFirstName", length = 20)
    private String agtFirstName;

    @Column(name = "AgtLastName", length = 20)
    private String agtLastName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgtFirstName() {
        return agtFirstName;
    }

    public void setAgtFirstName(String agtFirstName) {
        this.agtFirstName = agtFirstName;
    }

    public String getAgtLastName() {
        return agtLastName;
    }

    public void setAgtLastName(String agtLastName) {
        this.agtLastName = agtLastName;
    }

}