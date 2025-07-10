package com.order_service.dto.mapper;

import com.order_service.dto.response.OrderResponseDTO;
import com.order_service.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {OrderMapper.class})
public abstract class OrderMapper {

    public abstract OrderResponseDTO toDto(Order order);
}
