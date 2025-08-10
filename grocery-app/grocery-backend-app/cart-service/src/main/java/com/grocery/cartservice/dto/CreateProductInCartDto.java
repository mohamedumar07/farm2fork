package com.grocery.cartservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductInCartDto {

    @NotNull(message = "Customer Id Required")
    private long userId;

    @NotNull(message = "Product Id Required")
    private long productId;

}
