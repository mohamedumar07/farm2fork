package com.grocery.cartservice.service;

import com.grocery.cartservice.dto.CartResponseDto;
import com.grocery.cartservice.dto.CreateProductInCartDto;

import java.util.List;

public interface CartService {

    CartResponseDto addProductToCart(CreateProductInCartDto createProductInCartDto);
    List<CartResponseDto> fetchAllCartDetailsByUserId(Long userId);
    void deleteProductFromCart(Long productId, Long userId);
    CartResponseDto updateProductQuantityInCart(Long userId, Long productId, Long quantity);
    void deleteAllProductsFromCart(Long userId);

}
