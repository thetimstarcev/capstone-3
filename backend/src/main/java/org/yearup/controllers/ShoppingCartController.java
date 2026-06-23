package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

// only logged in users should have access to these actions
@RestController
@RequestMapping("/cart")
@PreAuthorize("hasRole('USER')")
public class ShoppingCartController {
    // a shopping cart controller depends on the service layer
    private ShoppingCartService shoppingCartService;
    private UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        // get the currently logged in username
        String userName = principal.getName();
        // find database user by username
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        // use the shoppingCartService to get all items in the cart and return the cart
        return shoppingCartService.getByUserId(userId);
    }

    @PostMapping("/products/{id}")
    public ResponseEntity<ShoppingCart> addProduct(Principal principal, @PathVariable int id) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        ShoppingCart cart = shoppingCartService.addProduct(userId, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)
    @PutMapping("/products/{id}")
    public ResponseEntity<ShoppingCart> updateProduct(Principal principal, @PathVariable int id, @RequestBody ShoppingCartItem item) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        int quantity = item.getQuantity();
        ShoppingCart cart = shoppingCartService.updateProduct(userId,id,quantity);
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)

}
