package com.tus.order.mapper;

import com.tus.order.controller.OrderController;
import com.tus.order.dto.CustomerDto;
import com.tus.order.dto.OrderDto;
import com.tus.order.entity.Order;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class OrderMapper {

    public static OrderDto toDto(Order order, CustomerDto customerDto) {
        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setAmount(order.getAmount());
        dto.setProduct(order.getProduct());
        dto.setPrice(order.getPrice());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setMobileNumber(order.getMobileNumber());

        if (customerDto != null) {
            dto.setCustomerId(customerDto.getCustomerId());
            dto.setName(customerDto.getName());
            dto.setEmail(customerDto.getEmail());
        }

        addLinks(dto, order.getOrderId());
        return dto;
    }

    public static Order toEntity(OrderDto dto) {
        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setOrderDate(dto.getOrderDate());
        order.setAmount(dto.getAmount());
        order.setProduct(dto.getProduct());
        order.setPrice(dto.getPrice());
        order.setUpdatedAt(dto.getUpdatedAt());
        order.setMobileNumber(dto.getMobileNumber());
        return order;
    }

    private static void addLinks(OrderDto dto, Long orderId) {
        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).getOrder(null, orderId)).withSelfRel());

        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).getAllOrders(null)).withRel("all-orders"));

        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).updateOrder(orderId, dto)).withRel("update"));

        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).deleteOrder(orderId)).withRel("delete"));
    }
}
