package com.bazinga.eg.gateway.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Objects;

@Component
public class FilterUtils {

    public String getCorrelationId(HttpHeaders headers) {
        List<String> coIds = headers.get(Constant.CORRELATION_ID);

        if (Objects.nonNull(coIds)) {
            return coIds.stream().findFirst().orElse(null);
        }

        return null;
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return this.setRequestHeader(exchange, Constant.CORRELATION_ID, correlationId);
    }

    private ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(
                exchange.getRequest().mutate().header(name, value).build()
        ).build();
    }
}
