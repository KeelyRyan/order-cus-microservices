//package com.tus.order.entity;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
////@NoArgsConstructor
////@AllArgsConstructor
//public class Customer extends BaseEntity {
//
////	@Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long customerId;
////
////    @Column(nullable = false)
////    private String name;
////
////    private String email;
////
////    @Column(unique = true)
////    private String mobileNumber;
////
////    private LocalDateTime updatedAt;
////
////    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
////    private List<Order> orders;
//}