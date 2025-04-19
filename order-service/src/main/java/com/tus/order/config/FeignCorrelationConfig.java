package com.tus.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignCorrelationConfig {

    private static final String CORRELATION_ID_HEADER = "orders-correlation-id";

    @Bean
    public RequestInterceptor correlationIdInterceptor() {
        return (RequestTemplate template) -> {
            String correlationId = MDC.get(CORRELATION_ID_HEADER);
            if (correlationId != null) {
                template.header(CORRELATION_ID_HEADER, correlationId);
            }
        };
    }
}
