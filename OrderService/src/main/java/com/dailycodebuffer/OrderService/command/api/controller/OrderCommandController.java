package com.dailycodebuffer.OrderService.command.api.controller;

import com.dailycodebuffer.OrderService.command.api.command.CreateOrderCommand;
import com.dailycodebuffer.OrderService.command.api.model.OrderRestModel;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderCommandController {

    private CommandGateway commandGateway;

    public OrderCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createOrder(@RequestBody OrderRestModel orderRestModel) {
        log.info("###OrderCommandController:createOrder1");
        String orderId = UUID.randomUUID().toString();
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(orderId)
                .userId(orderRestModel.getUserId())
                .addressId(orderRestModel.getAddressId())
                .productId(orderRestModel.getProductId())
                .quantity(orderRestModel.getQuantity())
                .orderStatus("CREATED")
                .build();
        log.info("###OrderCommandController:createOrder2");
        commandGateway.sendAndWait(createOrderCommand);
        log.info("###OrderCommandController:createOrder3");

        return "Order Created";
    }
}
