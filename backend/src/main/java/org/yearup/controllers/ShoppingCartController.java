package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin
public class ShoppingCartController {
    // a shopping cart controller depends on the service layer
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    @Autowired
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

    /**
     * Adds a product to the authenticated user's shopping cart.
     * If the product already exists in the cart, increments its quantity.
     *
     * @param principal the authenticated user making the request
     * @param id the product ID to add to the cart
     * @return ResponseEntity with status 201 Created and the updated cart
     */
    @PostMapping("/products/{id}")
    public ResponseEntity<ShoppingCart> addProduct(Principal principal, @PathVariable int id) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        ShoppingCart cart = shoppingCartService.addProduct(userId, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    /**
     * Updates the quantity of a product in the authenticated user's shopping cart.
     *
     * @param principal the authenticated user making the request
     * @param id the product ID to update
     * @param item the shopping cart item containing the new quantity
     * @return ResponseEntity with status 200 OK and the updated cart
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<ShoppingCart> updateProduct(Principal principal, @PathVariable int id, @RequestBody ShoppingCartItem item) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        int quantity = item.getQuantity();
        ShoppingCart cart = shoppingCartService.updateProduct(userId, id, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    /**
     * Clears all items from the authenticated user's shopping cart.
     *
     * @param principal the authenticated user making the request
     * @return ResponseEntity with status 200 OK and an empty cart
     */
    @DeleteMapping
    public ResponseEntity<ShoppingCart> clearCart(Principal principal) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        shoppingCartService.clearCart(userId);
        ShoppingCart cart = new ShoppingCart();
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }
}
