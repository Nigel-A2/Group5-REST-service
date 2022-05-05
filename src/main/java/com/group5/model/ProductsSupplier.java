package com.group5.model;

import javax.persistence.*;

@Entity
@Table(name = "products_suppliers")
public class ProductsSupplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductSupplierId", nullable = false)
    private Integer id;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ProductId")
    private Integer product;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "SupplierId")
    private Integer supplier;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public Integer getSupplier() {
        return supplier;
    }

    public void setSupplier(Integer supplier) {
        this.supplier = supplier;
    }

}