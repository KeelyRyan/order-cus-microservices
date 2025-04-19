package com.tus.order.client;

import com.tus.order.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    @GetMapping("/api/customer")
    CustomerDto getCustomerByMobile(@RequestParam("mobile") String mobile);

    @PostMapping("/api/customer")
    CustomerDto createCustomer(@RequestBody CustomerDto customerDto); 
    }
