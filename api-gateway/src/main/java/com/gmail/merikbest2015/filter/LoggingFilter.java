package com.gmail.merikbest2015.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // Obtener información del request
        String requestPath = request.getPath().toString();
        String requestMethod = request.getMethodValue();
        String requestHeaders = request.getHeaders().toString();
        String requestHost = request.getURI().getHost();
        String requestIp = request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : "> Unknown";

        logger.info("> Request Path: {}", requestPath);
        logger.info("> Request Method: {}", requestMethod);
        logger.info("> Request Headers: {}", requestHeaders);
        logger.info("> Request Host: {}", requestHost);
        logger.info("> Request IP: {}", requestIp);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // Obtener información de la respuesta
            int responseStatusCode = response.getStatusCode() != null ? response.getStatusCode().value() : -1;
            String responseHeaders = response.getHeaders().toString();

            logger.info("> Response Status Code: {}", responseStatusCode);
            logger.info("> Response Headers: {}", responseHeaders);

            // Obtener información del microservicio de destino
            String targetServiceHost = exchange.getAttribute("targetServiceHost") != null ? exchange.getAttribute("targetServiceHost").toString() : "> Unknown";
            String targetServiceIp = exchange.getAttribute("targetServiceIp") != null ? exchange.getAttribute("targetServiceIp").toString() : "> Unknown";

            logger.info("> Target Service Host: {}", targetServiceHost);
            logger.info("> Target Service IP: {}", targetServiceIp);
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Order of the filter
    }
}
