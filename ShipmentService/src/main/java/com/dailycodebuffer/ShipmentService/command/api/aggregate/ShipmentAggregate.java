package com.dailycodebuffer.ShipmentService.command.api.aggregate;

import com.dailycodebuffer.CommonService.commands.ShipOrderCommand;
import com.dailycodebuffer.CommonService.events.OrderShippedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.hibernate.criterion.Order;

@Aggregate
public class ShipmentAggregate {
    private String shipmentId;
    private String orderId;
    private String shipmentStatus;

    public ShipmentAggregate() {

    }
    @CommandHandler
    public ShipmentAggregate(ShipOrderCommand shipOrderCommand) {
        // validate the shipOrderCommand
        // publish the Order Shipped event
        OrderShippedEvent orderShippedEvent = OrderShippedEvent
                .builder()
                .shipmentId(shipOrderCommand.getShipmentId())
                .orderId(shipOrderCommand.getOrderId())
                .shipmentStatus("COMPLETED")
                .build();

        AggregateLifecycle.apply(orderShippedEvent);
    }

    @EventSourcingHandler
    public void on(OrderShippedEvent event) {
        this.orderId = event.getOrderId();
        this.shipmentId = event.getShipmentId();
        this.shipmentStatus = event.getShipmentStatus();
    }
}
