package com.tus.order.repository;

import com.tus.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
//    List<Order> findByCustomerCustomerId(Long customerId); // for non-paged
//    Page<Order> findByCustomerCustomerId(Long customerId, Pageable pageable); 
    List<Order> findByOrderDateBetween(LocalDate start, LocalDate end);
    List<Order> findAllByOrderByOrderDateAsc();
    List<Order> findAllByOrderByOrderDateDesc();
    List<Order> findByMobileNumber(String mobileNumber);

}
