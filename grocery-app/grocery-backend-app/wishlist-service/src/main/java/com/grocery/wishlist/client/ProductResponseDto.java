package com.grocery.wishlist.client;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductResponseDto {
    private long productId;

    private String productName;

    private String productCategory;

    private String productDescription;

    private double price;

    private String brandName;

    private String productPoster;

}
