package com.tus.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data   // ✅ This generates all the getters/setters/toString/hashCode/etc.
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private LocalDate orderDate;
    private Long amount;
    private String product;
    private Double price;
    private LocalDateTime updatedAt;


    // You can add @ManyToOne(customer) here again if needed
}
