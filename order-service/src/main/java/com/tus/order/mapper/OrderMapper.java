package com.tus.order.mapper;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import com.tus.order.controller.OrderController;
import com.tus.order.dto.OrderDto;
import com.tus.order.entity.Customer;
import com.tus.order.entity.Order;

public class OrderMapper {

    public static OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();

        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setAmount(order.getAmount());
        dto.setProduct(order.getProduct());
        dto.setPrice(order.getPrice());
        dto.setUpdatedAt(order.getUpdatedAt());

        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getCustomerId());
            dto.setName(order.getCustomer().getName());
            dto.setEmail(order.getCustomer().getEmail());
            dto.setMobileNumber(order.getCustomer().getMobileNumber());
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

        if (dto.getCustomerId() != null) {
            Customer customer = new Customer();
            customer.setCustomerId(dto.getCustomerId());
            order.setCustomer(customer);
        }

        return order;
    }

    private static void addLinks(OrderDto dto, Long orderId) {
        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).getOrder(orderId)).withSelfRel());

        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).getAllOrders(null)).withRel("all-orders"));

        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).updateOrder(orderId, dto)).withRel("update"));

        dto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).deleteOrder(orderId)).withRel("delete"));
    }
}
