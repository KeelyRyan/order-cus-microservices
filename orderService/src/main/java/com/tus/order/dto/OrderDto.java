package com.tus.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {
    
    private Long orderId;
    private LocalDate orderDate;
    private Long amount;
    private String product;
    private Double price;
    private LocalDateTime updatedAt;

    @NotBlank(message = "Name is required.")
    private String name;

    @Email(message = "Valid email required.")
    private String email;

    @NotBlank(message = "Mobile number is required.")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits.")
    private String mobileNumber;
}
