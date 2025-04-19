package com.tus.order.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tus.order.dto.CustomerDto;
import com.tus.order.dto.OrderDto;

public interface IOrderService {
	OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrder(Long orderId);
	boolean isDeleted(String mobileNumber);
	void deleteOrder(Long orderId);
	void updateOrder(Long orderId, OrderDto orderDto);
	boolean deleteCustomer(String mobileNumber);
    Page<OrderDto> getAllOrders(Pageable pageable);
	List<OrderDto> getOrdersByCustomerId(Long customerId);
    Page<OrderDto> getAllCusOrdersByCustomerId(Long customerId, Pageable pageable);
	List<OrderDto> getOrdersWithinDateRange(LocalDate startDate, LocalDate endDate);
	List<OrderDto> getOrdersSortedByDate(String sort);
	CustomerDto getCustomerByMobile(String mobile);
}