package com.grocery.cartservice.repository;

import com.grocery.cartservice.entity.Cart;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserIdAndProductId(@NotNull(message = "Customer Id Required") long userId, @NotNull(message = "Product Id Required") long productId);

    boolean existsByUserIdAndProductId(@NotNull(message = "Customer Id Required") long userId, @NotNull(message = "Product Id Required") long productId);

    List<Cart> findAllByUserId(Long userId);

}
