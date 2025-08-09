package com.grocery.wishlist.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface ProductClient {

    Logger log = LoggerFactory.getLogger(ProductClient.class);

    @GetExchange("/api/v1/products/get/{product_id}")
    ProductResponseDto getProductById(@PathVariable("product_id") long productId);
}
