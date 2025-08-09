package com.grocery.productservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="products")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(nullable = false, name = "product_name")
    private String productName;

    @Column(nullable = false, name = "product_category")
    private String productCategory;

    @Column(nullable = false,name = "product_description")
    private String productDescription;

    @Column(nullable = false, name = "product_price")
    private double price;

    @Column(nullable = false,name = "brand_name")
    private String brandName;

    @Column(nullable = false,name = "product_poster")
    private String productPoster;



}
