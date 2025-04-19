package com.tus.order.controller;
import com.tus.order.dto.OrderDto;
import com.tus.order.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    // 🔹 Get order by ID
    @GetMapping(path = "/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long orderId) {
        OrderDto orderDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    // 🔹 Get all orders (paged)
    @GetMapping(path = "/orders")
    public Page<OrderDto> getAllOrders(@PageableDefault(size = 10, sort = "orderDate") Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    // 🔹 Get paged orders by customerId
    @GetMapping("/customer/{customerId}/orders")
    public ResponseEntity<Page<OrderDto>> getOrdersByCustomerIdPaged(
            @PathVariable Long customerId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<OrderDto> orders = orderService.getAllCusOrdersByCustomerId(customerId, pageable);
        return ResponseEntity.ok(orders);
    }

    // 🔹 Get orders by customerId (non-paged)
    @GetMapping("/customer/{customerId}/all-orders")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderDto> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    // 🔹 Get orders within date range
    @GetMapping("/search/by-date")
    public ResponseEntity<List<OrderDto>> getOrdersWithinDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        List<OrderDto> orders = orderService.getOrdersWithinDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    // 🔹 Get orders sorted by date
    @GetMapping("/sorted")
    public ResponseEntity<List<OrderDto>> getOrdersSortedByDate(
            @RequestParam(defaultValue = "asc") String sort) {
        List<OrderDto> orders = orderService.getOrdersSortedByDate(sort);
        return ResponseEntity.ok(orders);
    }

    // 🔹 (Optional passthrough) Fetch customer by mobile
//    @GetMapping(path = "/customer")
//    public ResponseEntity<CustomerDto> getCustomer(@RequestParam String mobileNumber) {
//        CustomerDto customerDto = orderService.getCustomerByMobile(mobileNumber); // Likely via REST/Feign
//        return ResponseEntity.ok(customerDto);
//    }

    // 🔹 Create a new order
    @PostMapping(path = "/orders")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        OrderDto createdOrder = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // 🔹 Update existing order
    @PutMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(
            @Valid @PathVariable Long orderId,
            @RequestBody OrderDto orderDto) {
        orderService.updateOrder(orderId, orderDto);
        OrderDto updatedOrder = orderService.getOrder(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    // 🔹 Delete an order
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
