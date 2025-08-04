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
    @Column(name = "product_id")
    private long productId;

    @Column(nullable = false, name = "product_name")
    private String productName;

    @Column(nullable = false, name = "product_category")
    private String productCategory;

    @Column(nullable = false, name = "price")
    private double price;

    @Column(nullable = false,name = "product_image_url")
    private String productImageUrl;

    @Column(nullable = false,name = "brand_name")
    private String brandName;

}
