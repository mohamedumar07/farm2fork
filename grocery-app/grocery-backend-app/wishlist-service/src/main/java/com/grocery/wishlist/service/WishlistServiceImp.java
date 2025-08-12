package com.grocery.wishlist.service;


import com.grocery.wishlist.client.ProductClient;
import com.grocery.wishlist.client.ProductResponseDto;
import com.grocery.wishlist.dto.CreateWishlistDto;
import com.grocery.wishlist.dto.WishlistResponseDto;
import com.grocery.wishlist.entity.Wishlist;
import com.grocery.wishlist.exception.WishlistAlreadyAddedException;
import com.grocery.wishlist.mapper.WishlistMapper;
import com.grocery.wishlist.repository.WishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImp implements WishlistService {

    private final Logger log = LoggerFactory.getLogger(WishlistServiceImp.class);

    private final WishlistRepository wishlistRepository;

    private final WishlistMapper wishlistMapper;

    private final ProductClient productClient;

    @Value(("${product.client.url}"))
    private String productBaseUrl;

    public WishlistServiceImp(WishlistRepository wishlistRepository, WishlistMapper wishlistMapper, ProductClient productClient) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistMapper = wishlistMapper;
        this.productClient = productClient;
    }

    @Override
    public WishlistResponseDto addProductToWishlist(CreateWishlistDto createWishlistDto) {

        var getProductDetails = productClient.getProductById(createWishlistDto.getProductId());

        log.info("getProductDetails {}", getProductDetails);

        boolean wishlistExist = wishlistRepository.existsByUserIdAndProductId(createWishlistDto.getUserId(),createWishlistDto.getProductId());

        if (wishlistExist==true) {
            throw new WishlistAlreadyAddedException("Product already exists in the Wishlist");
        }

        Wishlist newWishlist = wishlistMapper.createWishlistDtoToWishlist(createWishlistDto);

        Wishlist addedWishlistProduct = wishlistRepository.save(newWishlist);

        return new WishlistResponseDto(addedWishlistProduct.getId(), addedWishlistProduct.getUserId(),
                addedWishlistProduct.getProductId(),getProductDetails.getProductName(),addedWishlistProduct.getAddedAt());
    }

    @Override
    public List<ProductResponseDto> fetchAllWishlistByUserId(Long userId) {

        List<Wishlist> wishlistListByUser = wishlistRepository.findAllByUserId(userId);

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Wishlist wishlist : wishlistListByUser) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            var getProductDetails = productClient.getProductById(wishlist.getProductId());

            productResponseDto.setProductId(getProductDetails.getProductId());
            productResponseDto.setProductName(getProductDetails.getProductName());
            productResponseDto.setProductCategory(getProductDetails.getProductCategory());
            productResponseDto.setProductDescription(getProductDetails.getProductDescription());
            productResponseDto.setPrice(getProductDetails.getPrice());
            productResponseDto.setBrandName(getProductDetails.getBrandName());

            String productPosterUrl = productBaseUrl + "/api/v1/file/" + getProductDetails.getProductPoster();
            productResponseDto.setProductPoster(productPosterUrl);
            productResponseDtoList.add(productResponseDto);
        }

        return productResponseDtoList;
    }

    @Override
    public void deleteProductFromWishlist(Long userId,Long productId) {

        Optional<Wishlist> wishlist = wishlistRepository.findByUserIdAndProductId(userId,productId);

        if(wishlist.isEmpty()){
            throw new RuntimeException("Wishlist does not exist");
        }
        wishlistRepository.delete(wishlist.get());
    }
}
