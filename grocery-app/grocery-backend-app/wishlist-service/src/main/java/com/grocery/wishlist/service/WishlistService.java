package com.grocery.wishlist.service;

import com.grocery.wishlist.client.ProductResponseDto;
import com.grocery.wishlist.dto.CreateWishlistDto;
import com.grocery.wishlist.dto.WishlistResponseDto;
import com.grocery.wishlist.entity.Wishlist;

import java.util.List;

public interface WishlistService {

    WishlistResponseDto addProductToWishlist(CreateWishlistDto createWishlistDto);
    List<ProductResponseDto> fetchAllWishlistByUserId(Long userId);
    void deleteProductFromWishlist(Long wishlistId, Long userId);

}
