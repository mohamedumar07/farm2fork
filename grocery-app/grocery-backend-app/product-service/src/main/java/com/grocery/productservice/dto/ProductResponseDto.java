package com.grocery.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductResponseDto {

    private long productId;

    private String productName;

    private String productCategory;

    private String productDescription;

    private double price;

    private String brandName;

    private String productPoster;

    private String productImageUrl;
}
