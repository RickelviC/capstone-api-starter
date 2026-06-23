package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.yearup.models.*;
import org.yearup.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows
        // look up each product
        // and build the ShoppingCart

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

    // add additional methods here

    public ShoppingCart addByUserId(int userId, int productId){

        CartItem itemInCart = shoppingCartRepository.findByUserIdAndProductId(userId,productId);

        if (itemInCart == null){
            itemInCart = new CartItem();

            itemInCart.setUserId(userId);
            itemInCart.setQuantity(1);
            itemInCart.setProductId(productId);

        }else {
            itemInCart.setQuantity(itemInCart.getQuantity() + 1);
        }

        shoppingCartRepository.save(itemInCart);

        return getByUserId(userId);
    }

    public ShoppingCart updateByUserId(int userId,int productId){

        CartItem item = shoppingCartRepository.findByUserIdAndProductId(userId,productId);

        int product = item.getProductId();



        return null;
    }


    public void deleteCart(int userId){
        shoppingCartRepository.deleteByUserId(userId);
    }
}
