package com.tus.order.client;

import com.tus.order.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Name matches `spring.application.name` in customer-service.yml
@FeignClient(name = "customer-service")
public interface CustomerClient {

    @GetMapping("/api/customers")
    CustomerDto getCustomerByMobile(@RequestParam String mobile);
}
