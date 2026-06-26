package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.models.Order;
import org.yearup.models.User;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
@PreAuthorize("hasRole('USER')")
@CrossOrigin
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    /**
     * Creates a new order from the current user's shopping cart.
     * Converts all items in the cart to order line items and clears the cart.
     *
     * @param principal the authenticated user making the request
     * @return ResponseEntity with status 201 Created and the newly created order
     */
    @PostMapping
    public ResponseEntity<Order> addOrder(Principal principal) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        Order order = orderService.checkout(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
