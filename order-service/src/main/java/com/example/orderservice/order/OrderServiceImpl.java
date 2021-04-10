package com.example.orderservice.order;

import com.example.orderservice.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Order orderEntity = modelMapper.map(orderDto, Order.class);

        orderRepository.save(orderEntity);

        OrderDto returnValue = modelMapper.map(orderEntity, OrderDto.class);

        return returnValue;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        Order orderEntity = orderRepository.findByOrderId(orderId);
        OrderDto orderDto = modelMapper.map(orderEntity, OrderDto.class);

        return orderDto;
    }

    @Override
    public Iterable<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
