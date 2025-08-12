package com.grocery.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {

    private long cartId;
    private long productId;
    private String productName;
    private String productCategory;
    private String productDescription;
    private String brandName;
    private long quantity;
    private double totalPrice;
    private String productPoster;

}
