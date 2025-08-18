package com.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()

				// Users service
				.route("users-service", r -> r
						.path("/api/users/**")
						.uri("lb://users-service"))

				// Product service
				.route("product-service", r -> r
						.path("/api/products/**")
						.uri("lb://product-service"))
				// Cart service
				.route("cart-service", r -> r
						.path("/api/cart/**")
						.uri("lb://cart-service"))

				// Optional: Discovery server routes (commented in original)
				/*
                .route("discovery-server", r -> r
                    .path("/eureka/web")
                    .filters(f -> f.setPath("/"))
                    .uri("http://localhost:8761"))

                .route("discovery-server-static", r -> r
                    .path("/eureka/**")
                    .uri("http://localhost:8761"))
                */

				.build();
	}

	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOriginPattern("*"); // o poner tu frontend: http://localhost:3000
		config.addAllowedMethod("*");
		config.addAllowedHeader("*");
		config.setAllowCredentials(true); // si usás cookies o headers como Authorization

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsWebFilter(source);
	}
}
