package com.msvc.cart_service.cart_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced  // 👈 Habilita la resolución por nombre del servicio (vía Eureka)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
