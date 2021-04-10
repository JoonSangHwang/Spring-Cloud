package com.example.orderservice.order;

import com.example.orderservice.order.dto.OrderDto;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<Order> getOrdersByUserId(String userId);
}
