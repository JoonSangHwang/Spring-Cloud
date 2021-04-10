package com.example.orderservice.order;

import com.example.orderservice.order.dto.OrderDto;
import com.example.orderservice.order.dto.RequestOrder;
import com.example.orderservice.order.dto.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("order-service")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final Environment env;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                     @RequestBody RequestOrder orderDetails) {
        log.info("Before add orders data");
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);
        /* jpa */
        OrderDto createdOrder = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = modelMapper.map(createdOrder, ResponseOrder.class);

        log.info("After added orders data");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) throws Exception {
        log.info("Before retrieve orders data");

        // 해당 유저의 주문 내역 조회
        Iterable<Order> orderList = orderService.getOrdersByUserId(userId);

        // 반환 포맷
        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach(v -> {
            result.add(modelMapper.map(v, ResponseOrder.class));
        });

//        try {
//            Thread.sleep(1000);
//            throw new Exception("장애 발생");
//        } catch(InterruptedException ex) {
//            log.warn(ex.getMessage());
//        }

        log.info("Add retrieved orders data");

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
