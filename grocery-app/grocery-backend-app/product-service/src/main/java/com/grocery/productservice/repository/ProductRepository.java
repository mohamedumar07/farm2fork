package com.grocery.productservice.repository;

import com.grocery.productservice.model.Products;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

    Optional<Products> findProductsByProductIdAndProductName(Long productId, @NotBlank(message = "Product Name is Required.") String productName);

    Products findProductsByProductName(String productName);

    List<Products> findAllByOrderByPriceAsc();

    List<Products> findAllByOrderByPriceDesc();

    List<Products> findAllByProductCategory(String category);

    @Query("select DISTINCT p.productCategory from Products p")
    List<String> getAllProductCategories();

    List<Products> findAllByOrderByProductCategory();

    List<Products> findAllByOrderByProductCategoryDesc();

    @Query("SELECT p FROM Products p WHERE p.productCategory = :category ORDER BY p.price ASC")
    List<Products> findAllByOrderByProductCategoryAscByPriceAsc(@Param("category") String category);

    @Query("SELECT p FROM Products p WHERE p.productCategory = :category ORDER BY p.price desc")
    List<Products> findAllByOrderByProductCategoryDescByPriceDesc(@Param("category") String category);

    @Query("SELECT DISTINCT p.brandName from Products p")
    List<String> getAllBrandName();

    @Query("SELECT p FROM Products p WHERE p.brandName = :brand ORDER BY p.price ASC")
    List<Products> findAllByOrderByBrandNameAscByPriceAsc(@Param("brand") String brand);

    @Query("SELECT p FROM Products p WHERE p.brandName = :brand ORDER BY p.price ASC")
    List<Products> findAllByOrderByBrandNameDescByPriceDesc(@Param("brand") String brand);

    List<Products> findAllByOrderByBrandNameAsc();

    List<Products> findAllByOrderByBrandNameDesc();
}
