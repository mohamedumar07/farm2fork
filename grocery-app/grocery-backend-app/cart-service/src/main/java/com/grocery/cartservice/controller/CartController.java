package com.grocery.cartservice.controller;

import com.grocery.cartservice.dto.CartResponseDto;
import com.grocery.cartservice.dto.CreateProductInCartDto;
import com.grocery.cartservice.entity.Cart;
import com.grocery.cartservice.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProductInCart(@RequestBody CreateProductInCartDto createProductInCartDto) {
        CartResponseDto cartResponseDto = cartService.addProductToCart(createProductInCartDto);
        return new ResponseEntity<>(cartResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/fetch/{user_id}")
    public ResponseEntity<?> fetchCartProductsByUserId(@PathVariable("user_id") Long userId) {
        List<CartResponseDto> fetchCartList = cartService.fetchAllCartDetailsByUserId(userId);
        return new ResponseEntity<>(fetchCartList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{user_id}/{product_id}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable("user_id") Long userId,
                                                   @PathVariable("product_id") Long id) {
        cartService.deleteProductFromCart(userId, id);
        return new ResponseEntity<>("Product removed from cart successfully",
                HttpStatus.OK);
    }

    @PutMapping("/update/{user_id}/{product_id}/{quantity}")
    public ResponseEntity<?> updateProductFromCart(
            @PathVariable("user_id") Long userId,
            @PathVariable("product_id") Long productId,
            @PathVariable("quantity") Long quantity) {
      CartResponseDto updateResponse = cartService.updateProductQuantityInCart(userId,productId,quantity);
      return new ResponseEntity<>(updateResponse, HttpStatus.OK);
    }

    @DeleteMapping("/deleteCartAfterOrder/{user_id}")
    public ResponseEntity<?> deleteCartAfterOrder(@PathVariable("user_id") Long userId) {
        cartService.deleteAllProductsFromCart(userId);
        return new ResponseEntity<>("Product removed from cart successfully after Ordering",HttpStatus.OK);
    }
}
