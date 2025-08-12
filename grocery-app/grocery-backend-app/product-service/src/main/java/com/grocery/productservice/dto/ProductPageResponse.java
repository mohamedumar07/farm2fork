package com.grocery.productservice.dto;

import java.util.List;

public record ProductPageResponse(
        List<ProductResponseDto> responseDtoList,
        Integer pageNumber,
        Integer pageSize,
        long totalElements,
        int totalPage,
        boolean isLast) {
}
