package com.grocery.productservice.service;

import com.grocery.productservice.dto.ProductPageResponse;
import com.grocery.productservice.dto.ProductResponseDto;
import com.grocery.productservice.dto.ProductSaveDto;
import com.grocery.productservice.dto.ProductUpdateDto;
import com.grocery.productservice.exception.InvalidSortException;
import com.grocery.productservice.exception.ProductAlreadyExistsException;
import com.grocery.productservice.exception.ProductNotFoundException;
import com.grocery.productservice.mapper.ProductMapper;
import com.grocery.productservice.model.Products;
import com.grocery.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    private static Logger logger = LoggerFactory.getLogger(ProductServiceImp.class);

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    private final FileService fileService;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductServiceImp(FileService fileService, ProductRepository productRepository, ProductMapper productMapper) {
        this.fileService = fileService;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Products> allProducts = productRepository.findAll();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        for (Products products : allProducts) {
            String productImageUrl = baseUrl+"/api/v1/file/"+products.getProductPoster();
            ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
            responseDto.setProductImageUrl(productImageUrl);

            productResponseDtos.add(responseDto);
        }

        return productResponseDtos;
    }

    @Override
    @Transactional
    public ProductResponseDto addProduct(ProductSaveDto productSaveDto,
                                         MultipartFile file) throws IOException {

        if(Files.exists(Paths.get(path+ File.separator + file.getOriginalFilename()))){
            throw new FileAlreadyExistsException("File already exists..Please try another filename..");
        }

        Optional<Products> getproducts = productRepository
                .findProductsByProductName(productSaveDto.getProductName());

        if (getproducts.isPresent()) {
            throw new ProductAlreadyExistsException("Product already exists with name " + productSaveDto.getProductName());
        }

        //Upload the file
        String uploadedFileName = fileService.uploadFile(path,file);

        //Set the poster name
        productSaveDto.setProductPoster(uploadedFileName);

        //Map the productSaveDto to produt
        Products products = productMapper.productSaveDtoToProducts(productSaveDto);

        //Then save the product
        Products savedProduct = productRepository.save(products);

        //generat the posterurl
        String productImageUrl = baseUrl+"/api/v1/file/"+uploadedFileName;

        //Map the product to productResponseDto
        ProductResponseDto savedResponseDto = productMapper.productToProductResponseDto(savedProduct);

        //Set the posterUrl
        savedResponseDto.setProductImageUrl(productImageUrl);

        return savedResponseDto;

    }

    @Override
    public ProductResponseDto getProductById(Long productId) {
        Products getProductById = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found with ID: " + productId));

        String productImageUrl = baseUrl+"/api/v1/file/"+getProductById.getProductPoster();

        ProductResponseDto productResponseDto = productMapper.productToProductResponseDto(getProductById);
        productResponseDto.setProductImageUrl(productImageUrl);

        return productResponseDto;
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(Long productId,
                                            ProductUpdateDto productUpdateDto,
                                            MultipartFile file) throws IOException {
        // 1. check if movie object exists with given movieId
        Products product = productRepository.findProductsByIdAndProductName(productId,productUpdateDto.getProductName())
                        .orElseThrow(()-> new ProductNotFoundException("Product not found with id: " + productId +" and name: " + productUpdateDto.getProductName()));

        //Get fileName
        String fileName = product.getProductPoster();
        if(file!=null){
            Files.deleteIfExists(Paths.get(path+ File.separator + fileName));
            fileName = fileService.uploadFile(path,file);
        }

        //Set the poster value
        logger.info("filename {}", fileName);
        product.setProductPoster(fileName);

        //Save the uploaded file
        product.setProductCategory(productUpdateDto.getProductCategory());
        product.setProductDescription(productUpdateDto.getProductDescription());
        product.setPrice(productUpdateDto.getPrice());
        product.setBrandName(productUpdateDto.getBrandName());

        //Save the updated movie
        Products updatedProduct = productRepository.save(product);
        String productImageUrl = baseUrl+"/api/v1/file/"+fileName;

        //Response dto
        ProductResponseDto updatedResponseDto = productMapper.productToProductResponseDto(updatedProduct);
        updatedResponseDto.setProductImageUrl(productImageUrl);


        return updatedResponseDto;
    }


    @Override
    public void deleteProductById(Long productId) throws IOException {

        Products products = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with id: " + productId));

        //delete the poster
        Files.deleteIfExists(Paths.get(path+ File.separator+products.getProductPoster()));

        //delete from DB
        productRepository.delete(products);
    }

    @Override
    public List<ProductResponseDto> sortProductsByPrice(String orderBy) {

        if(orderBy.equalsIgnoreCase("ASC")) {
            List<Products> sortProducts = productRepository.findAllByOrderByPriceAsc();

            List<ProductResponseDto> productResponseDtosByAsc = new ArrayList<>();

            for(Products products : sortProducts) {
                String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                responseDto.setProductImageUrl(productImageUrl);
                productResponseDtosByAsc.add(responseDto);
            }

            return productResponseDtosByAsc;
        }
        else if (orderBy.equalsIgnoreCase("DESC")) {
            List<Products> sortProducts = productRepository.findAllByOrderByPriceDesc();

            List<ProductResponseDto> productResponseDtosByDesc = new ArrayList<>();

            for(Products products : sortProducts) {
                String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                responseDto.setProductImageUrl(productImageUrl);
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
            String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
            ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
            responseDto.setProductImageUrl(productImageUrl);
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
                String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                responseDto.setProductImageUrl(productImageUrl);
                productResponseDtosByAsc.add(responseDto);
            }
            return productResponseDtosByAsc;
        }
        else if (orderBy.equalsIgnoreCase("DESC")) {
            List<Products> sortProductsByCategoryDesc = productRepository.findAllByOrderByProductCategoryDesc();
            List<ProductResponseDto> productResponseDtosByDesc = new ArrayList<>();
            for(Products products : sortProductsByCategoryDesc) {
                String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                responseDto.setProductImageUrl(productImageUrl);
                productResponseDtosByDesc.add(responseDto);
            }
            return productResponseDtosByDesc;
        }
        else {
            throw new InvalidSortException("Invalid sort order");
        }
    }

    @Override
    public List<ProductResponseDto> sortProductsByCategoryByPrice(String category, String orderBy) {
        if(orderBy.equalsIgnoreCase("ASC")) {
            List<Products> sortProductsByCategory = productRepository.findAllByOrderByProductCategoryAscByPriceAsc(category);
            List<ProductResponseDto> productResponseDtosByAsc = new ArrayList<>();
            for(Products products : sortProductsByCategory) {
                String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                responseDto.setProductImageUrl(productImageUrl);
                productResponseDtosByAsc.add(responseDto);
            }
            return productResponseDtosByAsc;
        }
        else if (orderBy.equalsIgnoreCase("DESC")) {
            List<Products> sortProductsByCategoryDesc = productRepository.findAllByOrderByProductCategoryDescByPriceDesc(category);
            List<ProductResponseDto> productResponseDtosByDesc = new ArrayList<>();
            for(Products products : sortProductsByCategoryDesc) {
                String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                responseDto.setProductImageUrl(productImageUrl);
                productResponseDtosByDesc.add(responseDto);
            }
            return productResponseDtosByDesc;
        }
        else {
            throw new InvalidSortException("Invalid sort order");
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
            String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
            ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
            responseDto.setProductImageUrl(productImageUrl);
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
                String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                responseDto.setProductImageUrl(productImageUrl);
                productResponseDtosByAsc.add(responseDto);
            }
            return productResponseDtosByAsc;
        }
        else if (orderBy.equalsIgnoreCase("DESC")) {
            List<Products> sortProductsListByBrandDesc = productRepository.findAllByOrderByBrandNameDesc();
            List<ProductResponseDto> productResponseDtosByDesc = new ArrayList<>();
            for(Products products : sortProductsListByBrandDesc) {
                String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                responseDto.setProductImageUrl(productImageUrl);
                productResponseDtosByDesc.add(responseDto);
            }
            return productResponseDtosByDesc;
        }
        else{
            throw new InvalidSortException("Invalid sort order");
        }
    }


    @Override
    public List<ProductResponseDto> sortProductsByBrandByPrice(String brand, String orderBy) {
        if(orderBy.equalsIgnoreCase("ASC")) {
            List<Products> sortProductsListByBrand = productRepository.findAllByOrderByBrandNameAscByPriceAsc(brand);
            List<ProductResponseDto> productResponseDtosByAsc = new ArrayList<>();
            for(Products products : sortProductsListByBrand) {
                String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                responseDto.setProductImageUrl(productImageUrl);
                productResponseDtosByAsc.add(responseDto);
            }
            return productResponseDtosByAsc;
        }
        else if (orderBy.equalsIgnoreCase("DESC")) {
            List<Products> sortProductsListByBrandDesc = productRepository.findAllByOrderByBrandNameDescByPriceDesc(brand);
            List<ProductResponseDto> productResponseDtosByDesc = new ArrayList<>();
            for(Products products : sortProductsListByBrandDesc) {
                String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
                ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
                responseDto.setProductImageUrl(productImageUrl);
                productResponseDtosByDesc.add(responseDto);
            }
            return productResponseDtosByDesc;
        }
        else{
            throw new InvalidSortException("Invalid sort order");
        }
    }


    @Override
    public ProductResponseDto getProductByProductName(String productName) {

        logger.info("Getting the product by product name", productName);

        Products productsByProductName = productRepository.findProductsByProductName(productName)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with name: " + productName));

        String productImageUrl = baseUrl + "/api/v1/file/" + productsByProductName.getProductPoster();

        ProductResponseDto responseDto = productMapper.productToProductResponseDto(productsByProductName);
        responseDto.setProductImageUrl(productImageUrl);

        return responseDto;
    }

    @Override
    public ProductPageResponse getProductsWithPage(Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Products> productsPage = productRepository.findAll(pageable);
        List<Products> productsList = productsPage.getContent();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Products products : productsList) {
            ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
            String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
            responseDto.setProductImageUrl(productImageUrl);
            productResponseDtos.add(responseDto);
        }

        return new ProductPageResponse(productResponseDtos,pageNumber,pageSize,
                productsPage.getTotalElements(),productsPage.getTotalPages(),productsPage.isLast());
    }

    @Override
    public ProductPageResponse getProductsWithPageAndSort(Integer pageNumber, Integer pageSize, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("ASC")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Products> productsPage = productRepository.findAll(pageable);
        List<Products> productsList = productsPage.getContent();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Products products : productsList) {
            ProductResponseDto responseDto = productMapper.productToProductResponseDto(products);
            String productImageUrl = baseUrl + "/api/v1/file/"+products.getProductPoster();
            responseDto.setProductImageUrl(productImageUrl);
            productResponseDtos.add(responseDto);
        }

        return new ProductPageResponse(productResponseDtos,pageNumber,pageSize,
                productsPage.getTotalElements(),productsPage.getTotalPages(),productsPage.isLast());
    }
}
