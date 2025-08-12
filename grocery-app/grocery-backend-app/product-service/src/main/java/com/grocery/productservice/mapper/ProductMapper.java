package com.grocery.productservice.mapper;

import com.grocery.productservice.dto.ProductResponseDto;
import com.grocery.productservice.dto.ProductSaveDto;
import com.grocery.productservice.model.Products;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static ProductResponseDto productToProductResponseDto(Products products){
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setProductId(products.getId());
        responseDto.setProductName(products.getProductName());
        responseDto.setProductCategory(products.getProductCategory());
        responseDto.setProductDescription(products.getProductDescription());
        responseDto.setPrice(products.getPrice());
        responseDto.setBrandName(products.getBrandName());
        responseDto.setProductPoster(products.getProductPoster());
        return responseDto;
    }

    public static Products productSaveDtoToProducts(ProductSaveDto productSaveDto){
        Products newProduct = new Products();
        newProduct.setProductName(productSaveDto.getProductName());
        newProduct.setProductCategory(productSaveDto.getProductCategory());
        newProduct.setProductDescription(productSaveDto.getProductDescription());
        newProduct.setPrice(productSaveDto.getPrice());
        newProduct.setBrandName(productSaveDto.getBrandName());
        newProduct.setProductPoster(productSaveDto.getProductPoster());
        return newProduct;
    }
}
