package com.dailycodebuffer.ShipmentService.command.api.events;

import com.dailycodebuffer.CommonService.events.OrderShippedEvent;
import com.dailycodebuffer.ShipmentService.command.api.data.Shipment;
import com.dailycodebuffer.ShipmentService.command.api.data.ShipmentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEventHandler {

    private ShipmentRepository shipmentRepository;

    public ShipmentEventHandler(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(event, shipment);
        shipmentRepository.save(shipment);
    }
}
