package com.grocery.wishlist.mapper;

import com.grocery.wishlist.dto.CreateWishlistDto;
import com.grocery.wishlist.dto.WishlistResponseDto;
import com.grocery.wishlist.entity.Wishlist;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class WishlistMapper {

    public static Wishlist createWishlistDtoToWishlist(CreateWishlistDto createWishlistDto) {
        Wishlist wishlist = new Wishlist();
        wishlist.setProductId(createWishlistDto.getProductId());
        wishlist.setUserId(createWishlistDto.getUserId());
        wishlist.setAddedAt(LocalDateTime.now());
        return wishlist;
    }

    public static WishlistResponseDto wishlistToWishlistResponseDto(Wishlist wishlist) {
        WishlistResponseDto wishlistResponseDto = new WishlistResponseDto();
        wishlistResponseDto.setProductId(wishlist.getProductId());
        wishlistResponseDto.setUserId(wishlist.getUserId());
        return wishlistResponseDto;
    }

}
