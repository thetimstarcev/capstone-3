package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Order;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

@Service
public class OrderService {
    private OrderLineItemRepository orderLineItemRepository;
    private OrderRepository orderRepository;

    public OrderService(OrderLineItemRepository orderLineItemRepository, OrderRepository orderRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderRepository = orderRepository;
    }

    public Order checkout(){
        return null;
    }
}
