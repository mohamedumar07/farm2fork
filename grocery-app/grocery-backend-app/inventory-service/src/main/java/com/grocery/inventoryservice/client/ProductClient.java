package com.grocery.inventoryservice.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface ProductClient {

    @GetExchange("/api/v1/products/get/{product_id}")
     ProductResponseDto getByProductId(@PathVariable("product_id") long productId);
}
