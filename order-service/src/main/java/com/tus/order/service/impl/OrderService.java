package com.tus.order.service.impl;

import com.tus.order.client.CustomerClient;
import com.tus.order.dto.CustomerDto;
import com.tus.order.dto.OrderDto;
import com.tus.order.entity.Order;
import com.tus.order.exception.ResourceNotFoundException;
import com.tus.order.mapper.OrderMapper;
import com.tus.order.repository.OrderRepository;
import com.tus.order.service.IOrderService;

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
        CustomerDto customerDto;

        try {
            customerDto = customerClient.getCustomerByMobile(orderDto.getMobileNumber());
        } catch (Exception e) {
            // Customer not found — create it
            CustomerDto newCustomer = new CustomerDto();
            newCustomer.setName(orderDto.getName());
            newCustomer.setEmail(orderDto.getEmail());
            newCustomer.setMobileNumber(orderDto.getMobileNumber());

            customerDto = customerClient.createCustomer(newCustomer);
        }

        Order order = OrderMapper.toEntity(orderDto);
        order.setMobileNumber(orderDto.getMobileNumber()); // make sure mobile is saved

        Order saved = orderRepository.save(order);
        return OrderMapper.toDto(saved, customerDto); // pass customerDto into mapper
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId.toString()));

        CustomerDto customerDto = customerClient.getCustomerByMobile(order.getMobileNumber());
        return OrderMapper.toDto(order, customerDto);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId.toString()));
        orderRepository.delete(order);
    }

    @Override
    public void updateOrder(Long orderId, OrderDto orderDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId.toString()));

        order.setAmount(orderDto.getAmount());
        order.setProduct(orderDto.getProduct());
        order.setPrice(orderDto.getPrice());
        order.setOrderDate(orderDto.getOrderDate());
        order.setUpdatedAt(orderDto.getUpdatedAt());

        orderRepository.save(order);
    }

    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(order -> {
                    CustomerDto customerDto = customerClient.getCustomerByMobile(order.getMobileNumber());
                    return OrderMapper.toDto(order, customerDto);
                });
    }
    @Override
    public List<OrderDto> getOrdersByMobileNumber(String mobileNumber) {
        return orderRepository.findByMobileNumber(mobileNumber)
                .stream()
                .map(order -> {
                    CustomerDto customerDto = customerClient.getCustomerByMobile(order.getMobileNumber());
                    return OrderMapper.toDto(order, customerDto);
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<OrderDto> getOrdersWithinDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate).stream()
                .map(order -> {
                    CustomerDto customerDto = customerClient.getCustomerByMobile(order.getMobileNumber());
                    return OrderMapper.toDto(order, customerDto);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersSortedByDate(String sort) {
        List<Order> orders = "desc".equalsIgnoreCase(sort)
                ? orderRepository.findAllByOrderByOrderDateDesc()
                : orderRepository.findAllByOrderByOrderDateAsc();

        return orders.stream()
                .map(order -> {
                    CustomerDto customerDto = customerClient.getCustomerByMobile(order.getMobileNumber());
                    return OrderMapper.toDto(order, customerDto);
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean isDeleted(String mobileNumber) {
        // Optional logic for soft deletes or audit trails
        return false;
    }
}
