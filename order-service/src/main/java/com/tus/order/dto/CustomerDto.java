package com.tus.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    @NotBlank(message = "Name is required.")
    private String name;
    private Long customerId; 
    @Email(message = "Valid email required.")
    private String email;

    @NotBlank(message = "Mobile number is required.")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits.")
    private String mobileNumber;

	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}

}

