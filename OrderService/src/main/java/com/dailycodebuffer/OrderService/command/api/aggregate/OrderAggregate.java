package com.dailycodebuffer.OrderService.command.api.aggregate;

import com.dailycodebuffer.OrderService.command.api.command.CreateOrderCommand;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class OrderAggregate {
    @TargetAggregateIdentifier
    private String orderId;

    private String productId;
    private String userId;
    private String addressId;
    private String quantity;
    private String orderStatus;

    public OrderAggregate() {

    }
    public OrderAggregate(CreateOrderCommand createOrderCommand) {

    }
}
