package com.tus.order.controller;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.tus.order.dto.OrderDto;
import com.tus.order.service.IOrderService;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    @Value("${server.port}")
    private String port;

    // Get order by ID
    @GetMapping(path = "/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@RequestHeader("orders-correlation-id") String correlationId, @PathVariable Long orderId) {
        OrderDto orderDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

 // Get all orders (paged)
    @GetMapping(path = "/orders")
    public Page<OrderDto> getAllOrders(@PageableDefault(size = 10, sort = "orderDate") Pageable pageable) {
        System.out.println("Handled by instance on port: " + port); // or use a logger
        return orderService.getAllOrders(pageable);
    }


    // Get orders within date range
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

    //  Get orders sorted by date
    @GetMapping("/sorted")
    public ResponseEntity<List<OrderDto>> getOrdersSortedByDate(
            @RequestParam(defaultValue = "asc") String sort) {
        List<OrderDto> orders = orderService.getOrdersSortedByDate(sort);
        return ResponseEntity.ok(orders);
    }

    //  Create a new order
    @PostMapping(path = "/orders")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        OrderDto createdOrder = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    //  Update existing order
    @PutMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(
            @Valid @PathVariable Long orderId,
            @RequestBody OrderDto orderDto) {
        orderService.updateOrder(orderId, orderDto);
        OrderDto updatedOrder = orderService.getOrder(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    // Delete an order
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
