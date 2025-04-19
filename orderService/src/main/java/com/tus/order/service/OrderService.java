package com.tus.order.service;

import com.tus.order.client.CustomerClient;
import com.tus.order.dto.CustomerDto;
import com.tus.order.dto.OrderDto;
import com.tus.order.entity.Order;
import com.tus.order.exception.ResourceNotFoundException;
import com.tus.order.mapper.OrderMapper;
import com.tus.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;

    @Override
    public CustomerDto getCustomerByMobile(String mobile) {
        return customerClient.getCustomerByMobile(mobile);
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = OrderMapper.toEntity(orderDto);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId.toString()));
        return OrderMapper.toDto(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId.toString()));
        orderRepository.delete(order);
    }

    @Override
    public void updateOrder(Long orderId, OrderDto orderDto) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId.toString()));

        existingOrder.setAmount(orderDto.getAmount());
        existingOrder.setProduct(orderDto.getProduct());
        existingOrder.setPrice(orderDto.getPrice());
        existingOrder.setOrderDate(orderDto.getOrderDate());
        existingOrder.setUpdatedAt(orderDto.getUpdatedAt());

        orderRepository.save(existingOrder);
    }

    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(OrderMapper::toDto);
    }

    @Override
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerCustomerId(customerId);
        return orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderDto> getAllCusOrdersByCustomerId(Long customerId, Pageable pageable) {
        return orderRepository.findByCustomerCustomerId(customerId, pageable)
                .map(OrderMapper::toDto);
    }

    @Override
    public List<OrderDto> getOrdersWithinDateRange(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
        return orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersSortedByDate(String sort) {
        List<Order> orders;

        if ("desc".equalsIgnoreCase(sort)) {
            orders = orderRepository.findAllByOrderByOrderDateDesc();
        } else {
            orders = orderRepository.findAllByOrderByOrderDateAsc();
        }

        return orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

	@Override
	public boolean isDeleted(String mobileNumber) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteCustomer(String mobileNumber) {
		// TODO Auto-generated method stub
		return false;
	}

}
