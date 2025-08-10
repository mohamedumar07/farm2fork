package com.grocery.cartservice.service;

import com.grocery.cartservice.client.ProductClient;
import com.grocery.cartservice.client.ProductResponseDto;
import com.grocery.cartservice.dto.CartResponseDto;
import com.grocery.cartservice.dto.CreateProductInCartDto;
import com.grocery.cartservice.entity.Cart;
import com.grocery.cartservice.exception.ProdAlreadyExistInCartException;
import com.grocery.cartservice.exception.ProductNotFoundException;
import com.grocery.cartservice.exception.ProductNotFoundInCartException;
import com.grocery.cartservice.mapper.CartMapper;
import com.grocery.cartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImp implements CartService {

    @Value("${product.client.url}")
    private String productBaseUrl;

    private final CartRepository cartRepository;

    private final ProductClient productClient;

    private final CartMapper cartMapper;

    public CartServiceImp(CartRepository cartRepository, ProductClient productClient, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.productClient = productClient;
        this.cartMapper = cartMapper;
    }

    @Override
    public CartResponseDto addProductToCart(CreateProductInCartDto createProductInCartDto) {

        boolean existingCart = cartRepository.existsByUserIdAndProductId(createProductInCartDto.getUserId(),
                                    createProductInCartDto.getProductId());

        if(existingCart==true){
            throw new ProdAlreadyExistInCartException("Product already exists in the Cart");
        }

        var getProductDetailsById = productClient.getProductById(createProductInCartDto.getProductId());

        if(getProductDetailsById==null){
            throw new ProductNotFoundException("Product Not Found in the Database");
        }

        Cart newProdAdded = cartMapper.createCartDtoToCart(createProductInCartDto);
        newProdAdded.setProductName(getProductDetailsById.getProductName());
        double totalPrice = getProductDetailsById.getPrice()*newProdAdded.getQuantity();
        newProdAdded.setTotalPrice(totalPrice);

        Cart cartAdded = cartRepository.save(newProdAdded);

        CartResponseDto responseDto = cartMapper.cartToCartResponseDto(cartAdded);

        responseDto.setProductCategory(getProductDetailsById.getProductCategory());
        responseDto.setProductDescription(getProductDetailsById.getProductDescription());
        responseDto.setBrandName(getProductDetailsById.getBrandName());

        String productPosterUrl = productBaseUrl + "/api/v1/file/" + getProductDetailsById.getProductPoster();
        responseDto.setProductPoster(productPosterUrl);

        return responseDto;
    }

    @Override
    public List<CartResponseDto> fetchAllCartDetailsByUserId(Long userId) {

        List<Cart> getAllCartDetailsByUser = cartRepository.findAllByUserId(userId);

        List<CartResponseDto> cartResponseListByUserId = new ArrayList<>();

        for(Cart cart : getAllCartDetailsByUser){
            CartResponseDto responseDto = cartMapper.cartToCartResponseDto(cart);

            var getProductDetailsById = productClient.getProductById(cart.getProductId());
            responseDto.setProductName(getProductDetailsById.getProductName());
            responseDto.setProductCategory(getProductDetailsById.getProductCategory());
            responseDto.setProductDescription(getProductDetailsById.getProductDescription());
            responseDto.setBrandName(getProductDetailsById.getBrandName());
            responseDto.setTotalPrice(getProductDetailsById.getPrice()*cart.getQuantity());

            String productPosterUrl = productBaseUrl + "/api/v1/file/" + getProductDetailsById.getProductPoster();
            responseDto.setProductPoster(productPosterUrl);

            cartResponseListByUserId.add(responseDto);
        }

        return cartResponseListByUserId;
    }

    @Override
    public void deleteProductFromCart(Long userId,Long productId) {

        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(userId, productId);

        if(existingCart.isEmpty()){
            throw new ProductNotFoundInCartException("Product Not found in the Cart");
        }

        cartRepository.delete(existingCart.get());
    }

    @Override
    public CartResponseDto updateProductQuantityInCart(Long userId, Long productId, Long quantity) {

        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(userId, productId);

        if(existingCart.isEmpty()){
            throw new ProductNotFoundInCartException("Product Not found in the Cart");
        }

        var productExists = productClient.getProductById(productId);

        if (productExists==null){
            throw new ProductNotFoundException("Product Not found in the Database");
        }

        existingCart.get().setQuantity(quantity);
        double totalPrice = productExists.getPrice()*quantity;
        existingCart.get().setTotalPrice(totalPrice);
        Cart updatedCartDetails = cartRepository.save(existingCart.get());

        CartResponseDto responseDto = cartMapper.cartToCartResponseDto(updatedCartDetails);
        responseDto.setProductCategory(productExists.getProductCategory());
        responseDto.setProductDescription(productExists.getProductDescription());
        responseDto.setBrandName(productExists.getBrandName());
        responseDto.setTotalPrice(totalPrice);
        String productPosterUrl =productBaseUrl + "/api/v1/file/" + productExists.getProductPoster();
        responseDto.setProductPoster(productPosterUrl);

        return responseDto;
    }

    @Override
    public void deleteAllProductsFromCart(Long userId) {
        List<Cart> getAllProductsInCartByUser = cartRepository.findAllByUserId(userId);
        cartRepository.deleteAll(getAllProductsInCartByUser);
    }
}
