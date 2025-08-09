package com.grocery.productservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductSaveDto {

    @NotBlank(message = "Product Name is Required.")
    private String productName;

    @NotBlank(message = "Product Category is Required.")
    private String productCategory;

    @NotBlank(message = "Product Description is Required.")
    private String productDescription;

    @NotNull(message = "Product Price is Required.")
    private double price;

    @NotBlank(message = "Product Brand Name is Required.")
    private String brandName;

    @NotBlank(message = "Product Poster Name Required.")
    private String productPoster;

    @NotBlank(message = "Product Image Url is Required.")
    private String productImageUrl;
}
