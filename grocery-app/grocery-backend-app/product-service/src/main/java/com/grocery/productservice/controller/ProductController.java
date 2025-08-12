package com.grocery.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.productservice.dto.ProductPageResponse;
import com.grocery.productservice.dto.ProductResponseDto;
import com.grocery.productservice.dto.ProductSaveDto;
import com.grocery.productservice.dto.ProductUpdateDto;
import com.grocery.productservice.exception.EmptyFileException;
import com.grocery.productservice.service.ProductService;
import com.grocery.productservice.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllProducts() {
        List<ProductResponseDto> responseDtoList = productService.getAllProducts();
        if (responseDtoList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createProduct(
            @RequestPart MultipartFile multipartFile,
            @RequestPart @Validated String productSaveDto) throws IOException {
        if(multipartFile.isEmpty()){
            throw new EmptyFileException("File is empty..Please send another file");
        }
        ProductSaveDto saveDto = convertStringToProductSaveDto(productSaveDto);
        ProductResponseDto savedProduct = productService.addProduct(saveDto,multipartFile);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/get/{product_id}")
    public ResponseEntity<?> getProductById(@PathVariable("product_id") Long product_id){
        ProductResponseDto getProductById = productService.getProductById(product_id);
        return new ResponseEntity<>(getProductById, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{product_id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("product_id") Long product_id) throws IOException {
        productService.deleteProductById(product_id);
        return new ResponseEntity<>("Deleted product successfully", HttpStatus.OK);
    }

    @PutMapping("/update/{product_id}")
    public ResponseEntity<?> updateProductDetails(
            @RequestPart MultipartFile multipartFile,
            @PathVariable("product_id") Long product_id,
            @Validated @RequestPart String updateDto
                                                  ) throws IOException {
        if(multipartFile.isEmpty()){
            throw new EmptyFileException("File is empty..Please send another file");
        }
        ProductUpdateDto productUpdateDto = convertStringToProductUpdateDto(updateDto);
        ProductResponseDto updatedProduct = productService.updateProduct(product_id, productUpdateDto,multipartFile);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/sortByPrice/{order_by}")
    public ResponseEntity<?> sortProduct(@PathVariable("order_by") String order_by){
        List<ProductResponseDto> sortProductsByPriceList = productService.sortProductsByPrice(order_by);
        if (sortProductsByPriceList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sortProductsByPriceList, HttpStatus.OK);
    }

    @GetMapping("/getCategory")
    public ResponseEntity<?> getCategory(){
        List<String> categoryList = productService.getAllCategories();
        if (categoryList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @GetMapping("/category/{product_category}")
    public ResponseEntity<?> findProductByCategory(@PathVariable("product_category") String product_category){
        List<ProductResponseDto> productsByCategory = productService.getProductsByCategory(product_category);
        if (productsByCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productsByCategory, HttpStatus.OK);
    }

    @GetMapping("/sortByCategory/{product_category}/{order_by}")
    public ResponseEntity<?> sortProductByCategory(@PathVariable("product_category")String productCategory,
                                                   @PathVariable("order_by") String order_by){
        List<ProductResponseDto> sortProductsByCategory = productService.sortProductsByCategory(productCategory, order_by);
        if (sortProductsByCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sortProductsByCategory, HttpStatus.OK);
    }

    @GetMapping("/sortByCategoryByPrice/{product_category}/{order_by}")
    public ResponseEntity<?> sortProductByCategoryByPrice(@PathVariable("product_category")String productCategory,
                                                   @PathVariable("order_by") String order_by){
        List<ProductResponseDto> sortProductsByCategoryByPrice = productService.sortProductsByCategoryByPrice(productCategory, order_by);
        if (sortProductsByCategoryByPrice.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sortProductsByCategoryByPrice, HttpStatus.OK);
    }

    @GetMapping("/getBrand")
    public ResponseEntity<?> getBrandName(){
        List<String> brandList = productService.getAllBrands();
        if (brandList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(brandList, HttpStatus.OK);
    }

    @GetMapping("/brand/{brand_name}")
    public ResponseEntity<?> getProductsByBrand(@PathVariable("brand_name") String brandName){
        List<ProductResponseDto> productListByBrans = productService.getProductsByBrand(brandName);
        if (productListByBrans.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productListByBrans, HttpStatus.OK);
    }

    @GetMapping("/sortByBrand/{brand_name}/{order_by}")
    public ResponseEntity<?> sortProductByBrandName(@PathVariable("brand_name") String brandName,
                                                    @PathVariable("order_by") String order_by){
        List<ProductResponseDto> sortProductsByBrand = productService.sortProductsByBrand(brandName, order_by);
        if (sortProductsByBrand.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sortProductsByBrand, HttpStatus.OK);
    }

    @GetMapping("/sortByBrandByPrice/{brand_name}/{order_by}")
    public ResponseEntity<?> sortProductByBrandNameByPrice(@PathVariable("brand_name") String brandName,
                                                    @PathVariable("order_by") String order_by){
        List<ProductResponseDto> sortProductsByBrandByPrice = productService.sortProductsByBrandByPrice(brandName, order_by);
        if (sortProductsByBrandByPrice.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sortProductsByBrandByPrice, HttpStatus.OK);
    }


    @GetMapping("/getProduct/{product_name}")
    public ResponseEntity<?> getProductByName(@PathVariable("product_name") String productName){
        ProductResponseDto productResponseDto = productService.getProductByProductName(productName);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @GetMapping("/allProductsWithPage")
    public ResponseEntity<?> getAllProductsWithPage(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        ProductPageResponse ProductPageResponseList = productService.getProductsWithPage(pageNumber, pageSize);
        return new ResponseEntity<>(ProductPageResponseList, HttpStatus.OK);
    }

    //Pagination and Sorting
    @GetMapping("/allProductsWithPageAndSort")
    public ResponseEntity<?> getAllProductsWithPageAndSort(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false) String direction

    ) {
        ProductPageResponse ProductPageResponseListWithSort = productService.getProductsWithPageAndSort(pageNumber, pageSize, sortBy, direction);
        return new ResponseEntity<>(ProductPageResponseListWithSort, HttpStatus.OK);
    }


    //String productSaveDto to class ProductSaveDto
    public ProductSaveDto convertStringToProductSaveDto(String productSaveDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(productSaveDto, ProductSaveDto.class);
    }

    //For update
    public ProductUpdateDto convertStringToProductUpdateDto(String productUpdateDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(productUpdateDto, ProductUpdateDto.class);
    }

}
