package com.order_service.service.imp;

import com.order_service.dto.mapper.OrderMapper;
import com.order_service.dto.request.OrderRequestDTO;
import com.order_service.dto.response.OrderResponseDTO;
import com.order_service.entity.Order;
import com.order_service.repository.OrderRepository;
import com.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderMapper orderMapper;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderDTO) {
            Order order = new Order();
            order.setBuyerId(orderDTO.getBuyerId());
            order.setProductIds(orderDTO.getProductsIds());
            order.setTotalPrice(orderDTO.getTotalPrice());

            Order savedOrder = orderRepository.save(order);
            OrderResponseDTO orderResponseDTO = orderMapper.toDto(savedOrder);

            return orderResponseDTO;
    }
}
