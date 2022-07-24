package com.bazinga.eg.gateway.filters;

import com.bazinga.eg.gateway.utils.Constant;
import com.bazinga.eg.gateway.utils.FilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseFilter {
    private static final Logger log = LoggerFactory.getLogger(ResponseFilter.class);

    private final FilterUtils filterUtils;

    @Autowired
    public ResponseFilter(FilterUtils filterUtils) {
        this.filterUtils = filterUtils;
    }

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) ->
                chain
                        .filter(exchange)
                        .then(
                                Mono.fromRunnable(
                                        () -> {
                                            HttpHeaders headers = exchange.getRequest().getHeaders();
                                            String correlationId = filterUtils.getCorrelationId(headers);

                                            log.debug("Adding the correlation id to the outbound headers. {}", correlationId);

                                            exchange.getResponse().getHeaders().add(Constant.CORRELATION_ID, correlationId);

                                            log.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
                                        }));
    }
}
