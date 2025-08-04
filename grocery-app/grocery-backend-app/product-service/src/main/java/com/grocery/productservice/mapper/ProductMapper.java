package com.grocery.productservice.mapper;

import com.grocery.productservice.dto.ProductResponseDto;
import com.grocery.productservice.dto.ProductSaveDto;
import com.grocery.productservice.model.Products;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static ProductResponseDto productToProductResponseDto(Products products){
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setProductId(products.getProductId());
        responseDto.setProductName(products.getProductName());
        responseDto.setProductCategory(products.getProductCategory());
        responseDto.setPrice(products.getPrice());
        responseDto.setBrandName(products.getBrandName());
        responseDto.setProductImageUrl(products.getProductImageUrl());
        return responseDto;
    }

    public static Products productSaveDtoToProducts(ProductSaveDto productSaveDto){
        Products newProduct = new Products();
        newProduct.setProductName(productSaveDto.getProductName());
        newProduct.setProductCategory(productSaveDto.getProductCategory());
        newProduct.setPrice(productSaveDto.getPrice());
        newProduct.setBrandName(productSaveDto.getBrandName());
        newProduct.setProductImageUrl(productSaveDto.getProductImageUrl());
        return newProduct;
    }
}
