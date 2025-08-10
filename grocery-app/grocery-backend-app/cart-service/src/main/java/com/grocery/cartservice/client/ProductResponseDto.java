package com.grocery.cartservice.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private long productId;
    private String productName;
    private String productCategory;
    private String productDescription;
    private double price;
    private String brandName;
    private String productPoster;
}
