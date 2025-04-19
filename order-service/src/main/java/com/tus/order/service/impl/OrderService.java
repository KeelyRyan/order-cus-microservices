package com.tus.order.service.impl;

import com.tus.order.client.CustomerClient;
import com.tus.order.dto.CustomerDto;
import com.tus.order.dto.OrderDto;
import com.tus.order.entity.Customer;
import com.tus.order.entity.Order;
import com.tus.order.exception.ResourceNotFoundException;
import com.tus.order.mapper.OrderMapper;
import com.tus.order.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;


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
    	    // Feign throws for 404s – create customer remotely
    	    CustomerDto newDto = new CustomerDto();
    	    newDto.setName(orderDto.getName());
    	    newDto.setEmail(orderDto.getEmail());
    	    newDto.setMobileNumber(orderDto.getMobileNumber());

    	    customerDto = customerClient.createCustomer(newDto);
    	}

    	// Create or retrieve local customer
    	CustomerDto finalCustomerDto = customerDto; // now effectively final
    	Customer customer = customerRepository.findByMobileNumber(finalCustomerDto.getMobileNumber())
    	    .orElseGet(() -> {
    	        Customer newCustomer = new Customer();
    	        newCustomer.setName(finalCustomerDto.getName());
    	        newCustomer.setEmail(finalCustomerDto.getEmail());
    	        newCustomer.setMobileNumber(finalCustomerDto.getMobileNumber());
    	        return customerRepository.save(newCustomer);
    	    });


        // Save the order
        Order order = OrderMapper.toEntity(orderDto);
        order.setCustomer(customer);

        Order saved = orderRepository.save(order);
        return OrderMapper.toDto(saved);
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
                .map(OrderMapper::toDto);
    }

    @Override
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerCustomerId(customerId)
                .stream()
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
        return orderRepository.findByOrderDateBetween(startDate, endDate)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersSortedByDate(String sort) {
        if ("desc".equalsIgnoreCase(sort)) {
            return orderRepository.findAllByOrderByOrderDateDesc()
                    .stream().map(OrderMapper::toDto).collect(Collectors.toList());
        } else {
            return orderRepository.findAllByOrderByOrderDateAsc()
                    .stream().map(OrderMapper::toDto).collect(Collectors.toList());
        }
    }

	@Override
	public boolean isDeleted(String mobileNumber) {
		// TODO Auto-generated method stub
		return false;
	}

    // Optional fallback methods can go here later
}
