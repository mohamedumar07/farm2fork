package com.grocery.wishlist.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WishlistResponseDto {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private LocalDateTime addedAt;
}
