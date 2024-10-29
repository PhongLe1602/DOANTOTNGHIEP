package doan.ptit.programmingtrainingcenter.mapper;

import doan.ptit.programmingtrainingcenter.dto.request.OrderRequest;
import doan.ptit.programmingtrainingcenter.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "paymentStatus", constant = "PENDING")
    @Mapping(target = "totalAmount", ignore = true)  // Sẽ tính tổng sau
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    Order toEntity(OrderRequest orderRequest, User user, PaymentMethod paymentMethod);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "course", source = "course")
    @Mapping(target = "price", source = "itemRequest.price")  // Chỉ định rõ source của price
    @Mapping(target = "createdAt", ignore = true)
    OrderItem toOrderItemEntity(OrderRequest.OrderItemRequest itemRequest, Order order, Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    void updateOrder(@MappingTarget Order order, OrderRequest orderRequest);

    default List<OrderItem> toOrderItemEntities(List<OrderRequest.OrderItemRequest> items, Order order, List<Course> courses) {
        return items.stream()
                .map(item -> {
                    Course course = courses.stream()
                            .filter(c -> c.getId().equals(item.getCourseId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Course not found"));
                    return toOrderItemEntity(item, order, course);
                })
                .collect(java.util.stream.Collectors.toList());
    }
}