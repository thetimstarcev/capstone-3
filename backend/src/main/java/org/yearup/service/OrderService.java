package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.CartItem;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;
import org.yearup.models.Profile;
import org.yearup.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@CrossOrigin
public class OrderService {
    private final ProfileRepository profileRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(ProfileRepository profileRepository, ShoppingCartRepository shoppingCartRepository, OrderLineItemRepository orderLineItemRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.profileRepository = profileRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order checkout(int userId) {
        Profile profile = profileRepository.findById(userId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Order order = new Order();
        order.setUserId(profile.getUserId());
        order.setDate(LocalDateTime.now());
        order.setAddress(profile.getAddress());
        order.setCity(profile.getCity());
        order.setState(profile.getState());
        order.setZip(profile.getZip());
        order.setShippingAmount(0);
        orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setOrderId(order.getOrderId());
            orderLineItem.setPrice((productRepository.findById(cartItem.getProductId()).orElseThrow()).getPrice());
            orderLineItem.setProductId(cartItem.getProductId());
            orderLineItem.setQuantity(cartItem.getQuantity());
            orderLineItem.setDiscount(0);
            orderLineItemRepository.save(orderLineItem);
        }
        shoppingCartRepository.deleteByUserId(userId);
        return order;
    }
}
