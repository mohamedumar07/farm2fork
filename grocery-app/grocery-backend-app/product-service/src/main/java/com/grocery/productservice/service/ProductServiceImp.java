package com.grocery.productservice.service;

import com.grocery.productservice.dto.ProductResponseDto;
import com.grocery.productservice.dto.ProductSaveDto;
import com.grocery.productservice.dto.ProductUpdateDto;
import com.grocery.productservice.exception.ProductAlreadyExistsException;
import com.grocery.productservice.exception.ProductNotFoundException;
import com.grocery.productservice.mapper.ProductMapper;
import com.grocery.productservice.model.Products;
import com.grocery.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    private static Logger logger = LoggerFactory.getLogger(ProductServiceImp.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductServiceImp(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Products> allProducts = productRepository.findAll();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        for (Products products : allProducts) {
            ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
            productResponseDtos.add(responseDto);
        }

        return productResponseDtos;
    }

    @Override
    public ProductResponseDto addProduct(ProductSaveDto productSaveDto) {

        Products getproducts = productRepository
                .findProductsByProductName(productSaveDto.getProductName());

        if (getproducts != null) {
           throw new ProductAlreadyExistsException("Product already exists with name " + productSaveDto.getProductName());
        }
        Products products = productMapper.productSaveDtoToProducts(productSaveDto);

        Products savedProduct = productRepository.save(products);

        return productMapper.productToProductResponseDto(savedProduct);

    }

    @Override
    public ProductResponseDto getProductById(Long productId) {
        Products getProductById = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with id: " + productId));

        return productMapper.productToProductResponseDto(getProductById);
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(Long productId, ProductUpdateDto productUpdateDto) {
        Products product = productRepository.findProductsByProductIdAndProductName(productId,productUpdateDto.getProductName())
                        .orElseThrow(()-> new ProductNotFoundException("Product not found with id: " + productId +" and name: " + productUpdateDto.getProductName()));

        product.setProductCategory(productUpdateDto.getProductCategory());
        product.setPrice(productUpdateDto.getPrice());
        product.setBrandName(productUpdateDto.getBrandName());
        product.setProductImageUrl(productUpdateDto.getProductImageUrl());

        Products updatedProduct = productRepository.save(product);

        return productMapper.productToProductResponseDto(updatedProduct);
    }


    @Override
    public void deleteProductById(Long productId) {

        Products products = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with id: " + productId));

        productRepository.delete(products);
    }

    @Override
    public List<ProductResponseDto> sortProductsByPrice(String orderBy) {

        if(orderBy.equalsIgnoreCase("ASC")) {
            List<Products> sortProducts = productRepository.findAllByOrderByPriceAsc();

            List<ProductResponseDto> productResponseDtosByAsc = new ArrayList<>();

            for(Products products : sortProducts) {
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                productResponseDtosByAsc.add(responseDto);
            }

            return productResponseDtosByAsc;
        }
        else if (orderBy.equalsIgnoreCase("DESC")) {
            List<Products> sortProducts = productRepository.findAllByOrderByPriceDesc();

            List<ProductResponseDto> productResponseDtosByDesc = new ArrayList<>();

            for(Products products : sortProducts) {
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                productResponseDtosByDesc.add(responseDto);
            }

            return productResponseDtosByDesc;
        }
        else{
            throw new RuntimeException("Invalid sort order");
        }
    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(String category) {
        List<Products> productsByCategory = productRepository.findAllByProductCategory(category);
        List<ProductResponseDto> productResponseByCategoryList = new ArrayList<>();
        for (Products products : productsByCategory) {
            ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
            productResponseByCategoryList.add(responseDto);
        }
        return productResponseByCategoryList;
    }

    @Override
    public List<String> getAllCategories() {
        List<String> allCategories = productRepository.getAllProductCategories();

        logger.info("Getting the list of all products categories", allCategories.stream().toList());

        return allCategories;
    }

    @Override
    public List<ProductResponseDto> sortProductsByCategory(String Category, String orderBy) {
        if(orderBy.equalsIgnoreCase("ASC")) {
            List<Products> sortProductsByCategory = productRepository.findAllByOrderByProductCategory();
            List<ProductResponseDto> productResponseDtosByAsc = new ArrayList<>();
            for(Products products : sortProductsByCategory) {
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                productResponseDtosByAsc.add(responseDto);
            }
            return productResponseDtosByAsc;
        }
        else if (orderBy.equalsIgnoreCase("DESC")) {
            List<Products> sortProductsByCategoryDesc = productRepository.findAllByOrderByProductCategoryDesc();
            List<ProductResponseDto> productResponseDtosByDesc = new ArrayList<>();
            for(Products products : sortProductsByCategoryDesc) {
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                productResponseDtosByDesc.add(responseDto);
            }
            return productResponseDtosByDesc;
        }
        else {
            throw new RuntimeException("Invalid sort order");
        }
    }

    @Override
    public List<ProductResponseDto> sortProductsByCategoryByPrice(String category, String orderBy) {
        if(orderBy.equalsIgnoreCase("ASC")) {
            List<Products> sortProductsByCategory = productRepository.findAllByOrderByProductCategoryAscByPriceAsc(category);
            List<ProductResponseDto> productResponseDtosByAsc = new ArrayList<>();
            for(Products products : sortProductsByCategory) {
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                productResponseDtosByAsc.add(responseDto);
            }
            return productResponseDtosByAsc;
        }
        else if (orderBy.equalsIgnoreCase("DESC")) {
            List<Products> sortProductsByCategoryDesc = productRepository.findAllByOrderByProductCategoryDescByPriceDesc(category);
            List<ProductResponseDto> productResponseDtosByDesc = new ArrayList<>();
            for(Products products : sortProductsByCategoryDesc) {
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                productResponseDtosByDesc.add(responseDto);
            }
            return productResponseDtosByDesc;
        }
        else {
            throw new RuntimeException("Invalid sort order");
        }
    }

    @Override
    public List<String> getAllBrands() {
        List<String> allBrands = productRepository.getAllBrandName();
        logger.info("Getting the list of all products brands", allBrands.stream().toList());
        return allBrands;
    }

    @Override
    public List<ProductResponseDto> getProductsByBrand(String brand) {
        List<Products> productsListByBrand = productRepository.findAllByProductCategory(brand);
        List<ProductResponseDto> productResponseDtosByBrand = new ArrayList<>();
        for (Products products : productsListByBrand) {
            ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
            productResponseDtosByBrand.add(responseDto);
        }
        return productResponseDtosByBrand;
    }

    @Override
    public List<ProductResponseDto> sortProductsByBrand(String brand, String orderBy) {
        if(orderBy.equalsIgnoreCase("ASC")) {
            List<Products> sortProductsListByBrand = productRepository.findAllByOrderByBrandNameAsc();
            List<ProductResponseDto> productResponseDtosByAsc = new ArrayList<>();
            for(Products products : sortProductsListByBrand) {
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                productResponseDtosByAsc.add(responseDto);
            }
            return productResponseDtosByAsc;
        }
        else if (orderBy.equalsIgnoreCase("DESC")) {
            List<Products> sortProductsListByBrandDesc = productRepository.findAllByOrderByBrandNameDesc();
            List<ProductResponseDto> productResponseDtosByDesc = new ArrayList<>();
            for(Products products : sortProductsListByBrandDesc) {
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                productResponseDtosByDesc.add(responseDto);
            }
            return productResponseDtosByDesc;
        }
        else{
            throw new RuntimeException("Invalid sort order");
        }
    }


    @Override
    public List<ProductResponseDto> sortProductsByBrandByPrice(String brand, String orderBy) {
        if(orderBy.equalsIgnoreCase("ASC")) {
            List<Products> sortProductsListByBrand = productRepository.findAllByOrderByBrandNameAscByPriceAsc(brand);
            List<ProductResponseDto> productResponseDtosByAsc = new ArrayList<>();
            for(Products products : sortProductsListByBrand) {
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                productResponseDtosByAsc.add(responseDto);
            }
            return productResponseDtosByAsc;
        }
        else if (orderBy.equalsIgnoreCase("DESC")) {
            List<Products> sortProductsListByBrandDesc = productRepository.findAllByOrderByBrandNameDescByPriceDesc(brand);
            List<ProductResponseDto> productResponseDtosByDesc = new ArrayList<>();
            for(Products products : sortProductsListByBrandDesc) {
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                productResponseDtosByDesc.add(responseDto);
            }
            return productResponseDtosByDesc;
        }
        else{
            throw new RuntimeException("Invalid sort order");
        }
    }


    @Override
    public ProductResponseDto getProductByProductName(String productName) {

        logger.info("Getting the product by product name", productName);

        logger.info("Product List {}", productRepository.findProductsByProductName(productName).toString());

        Products productsByProductName = productRepository.findProductsByProductName(productName);


        if (productsByProductName == null) {
            throw new ProductNotFoundException("Product not found with name: " + productName);
        }
        return productMapper.productToProductResponseDto(productsByProductName);
    }
}
