package doan.ptit.programmingtrainingcenter.mapper;

import doan.ptit.programmingtrainingcenter.dto.response.NotificationResponse;
import doan.ptit.programmingtrainingcenter.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificationMapper {


    @Mapping(source = "creator", target = "creator")
    @Mapping(source = "creator.fullName", target = "creator.name")
    NotificationResponse toNotificationResponse(Notification notification);
}
