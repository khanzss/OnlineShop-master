package com.gmail.merikbest2015.ecommerce.service.impl;

import com.gmail.merikbest2015.ecommerce.domain.CartItem;
import com.gmail.merikbest2015.ecommerce.domain.Perfume;
import com.gmail.merikbest2015.ecommerce.domain.User;
import com.gmail.merikbest2015.ecommerce.repository.CartItemRepository;
import com.gmail.merikbest2015.ecommerce.repository.PerfumeRepository;
import com.gmail.merikbest2015.ecommerce.service.CartService;
import com.gmail.merikbest2015.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserService userService;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItem> getCartItemsInCart() {
        User user = userService.getAuthenticatedUser();
        return user.getCartItems();
    }

    @Override
    @Transactional
    public void addCartItemToCart(Long cartItemId) {
        User user = userService.getAuthenticatedUser();
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        user.getCartItems().add(cartItem);
    }

    @Override
    @Transactional
    public void removeCartItemFromCart(Long cartItemId) {
        User user = userService.getAuthenticatedUser();
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        user.getCartItems().remove(cartItem);
    }
}
