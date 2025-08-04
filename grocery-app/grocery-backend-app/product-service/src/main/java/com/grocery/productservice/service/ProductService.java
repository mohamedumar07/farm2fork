package com.grocery.productservice.service;

import com.grocery.productservice.dto.ProductResponseDto;
import com.grocery.productservice.dto.ProductSaveDto;
import com.grocery.productservice.dto.ProductUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto addProduct(ProductSaveDto productSaveDto);

    ProductResponseDto getProductById(Long productId);

    ProductResponseDto updateProduct(Long productId,
                                     ProductUpdateDto productUpdateDto);

    void deleteProductById(Long productId);

    List<ProductResponseDto> sortProductsByPrice(String orderBy);

    List<ProductResponseDto> getProductsByCategory(String category);

    List<String> getAllCategories();

    List<ProductResponseDto> sortProductsByCategory(String Categroy, String orderBy);

    List<ProductResponseDto> sortProductsByCategoryByPrice(String Category,String orderBy);

    List<String> getAllBrands();

    List<ProductResponseDto> getProductsByBrand(String brand);

    List<ProductResponseDto> sortProductsByBrand(String brand,String orderBy);

    List<ProductResponseDto> sortProductsByBrandByPrice(String brand,String orderBy);

    ProductResponseDto getProductByProductName(String productName);

}
