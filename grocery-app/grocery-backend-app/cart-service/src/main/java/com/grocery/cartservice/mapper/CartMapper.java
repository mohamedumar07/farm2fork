package com.grocery.cartservice.mapper;

import com.grocery.cartservice.dto.CartResponseDto;
import com.grocery.cartservice.dto.CreateProductInCartDto;
import com.grocery.cartservice.entity.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public static Cart createCartDtoToCart(CreateProductInCartDto createProductInCartDto){
        Cart cart = new Cart();
        cart.setProductId(createProductInCartDto.getProductId());
        cart.setUserId(createProductInCartDto.getUserId());
        cart.setQuantity(createProductInCartDto.getQuantity());
        return cart;
    }

    public static CartResponseDto cartToCartResponseDto(Cart cart){
        CartResponseDto cartResponseDto = new CartResponseDto();
        cartResponseDto.setCartId(cart.getId());
        cartResponseDto.setProductId(cart.getProductId());
        cartResponseDto.setQuantity(cart.getQuantity());
        cartResponseDto.setProductName(cart.getProductName());
        cartResponseDto.setTotalPrice(cart.getTotalPrice());
        return cartResponseDto;

    }
}
