package com.gmail.merikbest2015.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomRouteFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpRequest request = exchange.getRequest();
            String targetServiceHost = request.getURI().getHost();
            String targetServiceIp = request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : "> Unknown";

            exchange.getAttributes().put("targetServiceHost", targetServiceHost);
            exchange.getAttributes().put("targetServiceIp", targetServiceIp);
        }));
    }

    @Override
    public int getOrder() {
        return -2; // Order of the filter, it should run before the logging filter
    }
}
