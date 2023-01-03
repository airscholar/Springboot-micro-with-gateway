package com.airscholar.microsecurity.filter;

import com.airscholar.microsecurity.config.RedisHashComponent;
import com.airscholar.microsecurity.dto.ApiKey;
import com.airscholar.microsecurity.util.AppConstants;
import com.airscholar.microsecurity.util.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    private RedisHashComponent redisHashComponent;

    public AuthFilter(RedisHashComponent redisHashComponent) {
        this.redisHashComponent = redisHashComponent;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> apiKeysHeader = exchange.getRequest().getHeaders().get("gatewaykey");
        log.info("API Key: {}", apiKeysHeader);

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String routeId = route != null ? route.getId() : "Unknown";

        if (routeId == null || CollectionUtils.isEmpty(apiKeysHeader) || !isAuthorised(routeId, apiKeysHeader.get(0))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API Key");
        }

        return chain.filter(exchange);
    }

    private boolean isAuthorised(String routeId, String apiKey){
        //get the api key object from the redis
        Object apiKeyObject = redisHashComponent.hGet(AppConstants.RECORD_KEY, apiKey);
        log.info("Api Key Object: {}, route id: {}", apiKeyObject, routeId);
        if(apiKeyObject != null){
            ApiKey key = MapperUtils.objectMapper(apiKeyObject, ApiKey.class);
            return key.getServices().contains(routeId);
        }

        return false;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
