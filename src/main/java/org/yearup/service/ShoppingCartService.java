package org.yearup.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.models.*;
import org.yearup.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        for (CartItem cartItem : shoppingCartRepository.findByUserId(userId)) {
            Product product = productService.getById(cartItem.getProductId());
            ShoppingCartItem item = new ShoppingCartItem();

            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());

            cart.add(item);
        }

        return cart;
    }

    public ShoppingCart addByUserId(int userId, int productId) {
        CartItem itemInCart = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if (itemInCart == null) {
            itemInCart = new CartItem();

            itemInCart.setUserId(userId);
            itemInCart.setQuantity(1);
            itemInCart.setProductId(productId);

        } else {
            itemInCart.setQuantity(itemInCart.getQuantity() + 1);
        }
        shoppingCartRepository.save(itemInCart);

        return getByUserId(userId);
    }

    public ShoppingCart updateByUserId(int userId, int productId, int quantity) {
        CartItem item = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        item.setQuantity(quantity);

        shoppingCartRepository.save(item);

        return getByUserId(userId);
    }

    @Transactional
    public void deleteCart(int userId) {
        shoppingCartRepository.deleteByUserId(userId);
    }
}
