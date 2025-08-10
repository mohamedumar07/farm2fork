package com.grocery.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {

    private String cartId;
    private String productId;
    private String productName;
    private int quantity;
    private double totalPrice;

}
