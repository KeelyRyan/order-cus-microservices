package com.tus.order.service;

import com.tus.order.dto.CustomerDto;
import com.tus.order.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IOrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrder(Long orderId);
    void updateOrder(Long orderId, OrderDto orderDto);
    void deleteOrder(Long orderId);

    Page<OrderDto> getAllOrders(Pageable pageable);
    List<OrderDto> getOrdersByCustomerId(Long customerId);
    Page<OrderDto> getAllCusOrdersByCustomerId(Long customerId, Pageable pageable);

    List<OrderDto> getOrdersWithinDateRange(LocalDate startDate, LocalDate endDate);
    List<OrderDto> getOrdersSortedByDate(String sortDirection);

    boolean isDeleted(String mobileNumber);
	CustomerDto getCustomerByMobile(String mobile);
}
