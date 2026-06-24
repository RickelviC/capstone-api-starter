package org.yearup.controllers;

import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.*;
import org.yearup.service.*;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")//must be logged in to see the cart
@CrossOrigin
public class ShoppingCartController {
    private ShoppingCartService shoppingCartService;
    private UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        int userId = getUser(principal);

        return shoppingCartService.getByUserId(userId);
    }

    @PostMapping("/products/{id}")
    public ResponseEntity<ShoppingCart> addCart(Principal principal, @PathVariable int id) {
        int userId = getUser(principal);
        ShoppingCart saved = shoppingCartService.addByUserId(userId, id);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/products/{id}")
    public ShoppingCart updateCart(Principal principal, @PathVariable int id, @RequestBody ShoppingCartItem item) {
        int userId = getUser(principal);

        return shoppingCartService.updateByUserId(userId, id, item.getQuantity());
    }

    @DeleteMapping
    public ShoppingCart deleteCart(Principal principal) {
        int userId = getUser(principal);
        shoppingCartService.deleteCart(userId);

        return shoppingCartService.getByUserId(userId);
    }

    // helper to find database user by username
    private int getUser(Principal principal) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        return user.getId();
    }
}
