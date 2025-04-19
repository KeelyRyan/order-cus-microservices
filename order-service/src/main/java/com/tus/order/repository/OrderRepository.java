package com.tus.order.repository;

import com.tus.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateBetween(LocalDate start, LocalDate end);
    List<Order> findAllByOrderByOrderDateAsc();
    List<Order> findAllByOrderByOrderDateDesc();
    List<Order> findByMobileNumber(String mobileNumber);

}
