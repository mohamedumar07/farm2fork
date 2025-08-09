package com.grocery.wishlist.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateWishlistDto {
        private Long userId;
        private Long productId;

}
