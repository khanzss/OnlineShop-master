package com.gmail.merikbest2015.ecommerce.service.impl;

import com.gmail.merikbest2015.ecommerce.constants.ErrorMessage;
import com.gmail.merikbest2015.ecommerce.domain.*;
import com.gmail.merikbest2015.ecommerce.domain.product.ProductOption;
import com.gmail.merikbest2015.ecommerce.dto.request.OrderRequest;
import com.gmail.merikbest2015.ecommerce.repository.OrderRepository;
import com.gmail.merikbest2015.ecommerce.repository.ProductOptionRepository;
import com.gmail.merikbest2015.ecommerce.service.OrderService;
import com.gmail.merikbest2015.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;

    @Override
    public Order getOrder(Long orderId) {
        User user = userService.getAuthenticatedUser();
        return orderRepository.getByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND));
    }

    @Override
    public List<CartItem> getOrdering() {
        User user = userService.getAuthenticatedUser();
        return user.getCartItems();
    }
    @Override
    public Page<Order> getUserOrdersList(Pageable pageable) {
        User user = userService.getAuthenticatedUser();
        return orderRepository.findOrderByUserId(user.getId(), pageable);
    }

    @Override
    @Transactional
    public Long postOrder(User user, OrderRequest orderRequest) {
        Order order = modelMapper.map(orderRequest, Order.class);
        order.setUser(user);

        // Tạo danh sách OrderItem từ OrderItemRequest trong OrderRequest
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(itemRequest -> {
                    ProductOption productOption = productOptionRepository.findById(itemRequest.getProductOptionId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product option not found"));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(productOption.getProduct());
                    orderItem.setProductOption(productOption);
                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setPrice(itemRequest.getPrice()); // Giá được xác định từ OrderRequest

                    return orderItem;
                })
                .collect(Collectors.toList());

        order.getOrderItems().addAll(orderItems);

        // Lưu đơn hàng vào database
        orderRepository.save(order);

        // Xóa tất cả các mục trong giỏ hàng của người dùng sau khi đặt hàng
        user.getCartItems().clear();


        return order.getId();
    }
}
