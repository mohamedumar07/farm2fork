package com.grocery.wishlist.controller;

import com.grocery.wishlist.client.ProductResponseDto;
import com.grocery.wishlist.dto.CreateWishlistDto;
import com.grocery.wishlist.dto.WishlistResponseDto;
import com.grocery.wishlist.entity.Wishlist;
import com.grocery.wishlist.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@CrossOrigin("*")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProductToWishlist(@RequestBody CreateWishlistDto createWishlistDto) {
        WishlistResponseDto responseDto = wishlistService.addProductToWishlist(createWishlistDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/fetch/{customer_id}")
    public ResponseEntity<?> getAllWishlists(@PathVariable("customer_id") long userId) {
        List<ProductResponseDto> fetchWishlistByUser = wishlistService.fetchAllWishlistByUserId(userId);
        return new ResponseEntity<>(fetchWishlistByUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{customer_id}/{product_id}")
    public ResponseEntity<?> deleteProductFromWishlist(
            @PathVariable("customer_id") Long userId,
            @PathVariable("product_id") Long product_id) {

        wishlistService.deleteProductFromWishlist(userId, product_id);
        return new ResponseEntity<>("Product removed from the Wishlist",HttpStatus.NO_CONTENT);
    }
}
