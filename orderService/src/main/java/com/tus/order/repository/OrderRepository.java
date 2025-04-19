package com.tus.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tus.order.entity.Customer;
import com.tus.order.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerCustomerId(Long customerId);
    Page<Order> findAll(Pageable pageable);
    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    List<Order> findAllByOrderByOrderDateAsc();
    List<Order> findAllByOrderByOrderDateDesc();
    Page<Order> findByCustomerCustomerId(Long customerId, Pageable pageable);
    Customer saveAndFlush(Customer customer);
}
