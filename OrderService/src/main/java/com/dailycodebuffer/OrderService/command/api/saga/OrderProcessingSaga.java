package com.dailycodebuffer.OrderService.command.api.saga;

import com.dailycodebuffer.CommonService.commands.CompleteOrderCommand;
import com.dailycodebuffer.CommonService.commands.ShipOrderCommand;
import com.dailycodebuffer.CommonService.commands.ValidatePaymentCommand;
import com.dailycodebuffer.CommonService.events.OrderCompletedEvent;
import com.dailycodebuffer.CommonService.events.OrderShippedEvent;
import com.dailycodebuffer.CommonService.events.PaymentProcessedEvent;
import com.dailycodebuffer.CommonService.model.User;
import com.dailycodebuffer.CommonService.queries.GetUserPaymentDetailQuery;
import com.dailycodebuffer.OrderService.command.api.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {

    private CommandGateway commandGateway;

    private QueryGateway queryGateway;

    public OrderProcessingSaga(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent in Saga for Order Id: {}", event.getOrderId());

        GetUserPaymentDetailQuery getUserPaymentDetailQuery = new GetUserPaymentDetailQuery(event.getUserId());

        User user = null;
        try {
            user = queryGateway.query(
                    getUserPaymentDetailQuery,
                    ResponseTypes.instanceOf(User.class)
            ).join();
        } catch(Exception e) {
            log.error(e.getMessage());
        }

        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                .cardDetails(user.getCardDetails())
                .orderId(event.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .build();
        commandGateway.sendAndWait((validatePaymentCommand));
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent event) {
        log.info("PaymentProcessedEvent in Saga for Order Id: {}", event.getOrderId());
        try {
            ShipOrderCommand shipOrderCommand = ShipOrderCommand
                    .builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(event.getOrderId())
                    .build();
            commandGateway.send(shipOrderCommand);
        } catch(Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent event) {
        log.info("OrderShippedEvent in Saga for Order Id: {}", event.getOrderId());
        try {
            CompleteOrderCommand completeOrderCommand = CompleteOrderCommand
                    .builder()
                    .orderId(UUID.randomUUID().toString())
                    .orderStatus("APPROVED")
                    .build();
            commandGateway.send(completeOrderCommand);
        } catch(Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCompletedEvent event) {
        log.info("OrderCompletedEvent in Saga for Order Id: {}", event.getOrderId());
    }
}
