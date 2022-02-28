package com.dailycodebuffer.OrderService.command.api.aggregate;

import com.dailycodebuffer.CommonService.commands.CompleteOrderCommand;
import com.dailycodebuffer.OrderService.command.api.command.CreateOrderCommand;
import com.dailycodebuffer.CommonService.events.OrderCompletedEvent;
import com.dailycodebuffer.OrderService.command.api.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Slf4j
@Aggregate
public class OrderAggregate {
    @TargetAggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;

    public OrderAggregate() {

    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        log.info("###OrderAggregate:OrderAggregate:CreateOrderCommand1");
        // Validate The Command
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
        log.info("###OrderAggregate:OrderAggregate:CreateOrderCommand2");
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        log.info("###OrderAggregate:on:OrderCreatedEvent");
        this.orderId = event.getOrderId();
        this.productId = event.getProductId();
        this.userId = event.getUserId();
        this.addressId = event.getAddressId();
        this.quantity = event.getQuantity();
        this.orderStatus = event.getOrderStatus();
    }

    @CommandHandler
    public void handle(CompleteOrderCommand completeOrderCommand) {
        log.info("###OrderAggregate:handle:CompleteOrderCommand");
        // Validate The Command
        // Publish Order Completed Event
        OrderCompletedEvent orderCompletedEvent = OrderCompletedEvent.builder()
                .orderStatus(completeOrderCommand.getOrderStatus())
                .orderId(completeOrderCommand.getOrderId())
                .build();
        AggregateLifecycle.apply(orderCompletedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCompletedEvent event) {
        log.info("###OrderAggregate:on:OrderCompletedEvent");
        this.orderStatus = event.getOrderStatus();
    }
}
