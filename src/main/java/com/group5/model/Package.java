package com.group5.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "packages")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PackageId", nullable = false)
    private Integer id;

    @Column(name = "PkgName", nullable = false, length = 50)
    private String pkgName;

    @Column(name = "PkgStartDate")
    private Instant pkgStartDate;

    @Column(name = "PkgEndDate")
    private Instant pkgEndDate;

    @Column(name = "PkgDesc", length = 50)
    private String pkgDesc;

    @Column(name = "PkgBasePrice", nullable = false, precision = 19, scale = 4)
    private BigDecimal pkgBasePrice;

    @Column(name = "PkgAgencyCommission", precision = 19, scale = 4)
    private BigDecimal pkgAgencyCommission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public Instant getPkgStartDate() {
        return pkgStartDate;
    }

    public void setPkgStartDate(Instant pkgStartDate) {
        this.pkgStartDate = pkgStartDate;
    }

    public Instant getPkgEndDate() {
        return pkgEndDate;
    }

    public void setPkgEndDate(Instant pkgEndDate) {
        this.pkgEndDate = pkgEndDate;
    }

    public String getPkgDesc() {
        return pkgDesc;
    }

    public void setPkgDesc(String pkgDesc) {
        this.pkgDesc = pkgDesc;
    }

    public BigDecimal getPkgBasePrice() {
        return pkgBasePrice;
    }

    public void setPkgBasePrice(BigDecimal pkgBasePrice) {
        this.pkgBasePrice = pkgBasePrice;
    }

    public BigDecimal getPkgAgencyCommission() {
        return pkgAgencyCommission;
    }

    public void setPkgAgencyCommission(BigDecimal pkgAgencyCommission) {
        this.pkgAgencyCommission = pkgAgencyCommission;
    }

}