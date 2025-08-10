package com.grocery.cartservice.config;

import com.grocery.cartservice.client.ProductClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${product.client.url}")
    private String productClientUrl;

    @Bean
    public ProductClient productClient() {
        RestClient restClient = RestClient.builder().baseUrl(productClientUrl).build();

        var restClientAdapter  = RestClientAdapter.create(restClient);

        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return httpServiceProxyFactory.createClient(ProductClient.class);

    }
}
