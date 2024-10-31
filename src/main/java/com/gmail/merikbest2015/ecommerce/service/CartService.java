package com.gmail.merikbest2015.ecommerce.service;

import com.gmail.merikbest2015.ecommerce.domain.CartItem;
import com.gmail.merikbest2015.ecommerce.domain.Perfume;

import java.util.List;

public interface CartService {

    List<CartItem> getCartItemsInCart();

    void addCartItemToCart(Long perfumeId);

    void removeCartItemFromCart(Long perfumeId);
}
