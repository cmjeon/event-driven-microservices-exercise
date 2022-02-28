package com.dailycodebuffer.OrderService.command.api.event;

import com.dailycodebuffer.CommonService.events.OrderCompletedEvent;
import com.dailycodebuffer.OrderService.command.api.data.Order;
import com.dailycodebuffer.OrderService.command.api.data.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventHandler {
    private OrderRepository orderRepository;
    public OrderEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        log.info("####OrderEventHandler:on:OrderCreatedEvent");
        Order order = new Order();
        BeanUtils.copyProperties(event, order);
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCompletedEvent event) {
         Order order = orderRepository.findById(event.getOrderId()).get();
         order.setOrderStatus(event.getOrderStatus());
         orderRepository.save(order);
    }
}
