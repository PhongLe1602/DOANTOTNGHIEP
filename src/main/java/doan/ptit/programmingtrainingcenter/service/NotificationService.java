package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.NotificationRequest;
import doan.ptit.programmingtrainingcenter.dto.response.NotificationRecipientResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserNotificationResponse;
import doan.ptit.programmingtrainingcenter.entity.Notification;
import doan.ptit.programmingtrainingcenter.entity.NotificationRecipient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    List<Notification> getNotifications();
    Notification createNotification(String creatorId,NotificationRequest notificationRequest);
    UserNotificationResponse getNotificationsOfRecipient(String recipientId);
    void markAsALLReadByUser(String recipientId);
    void markAsReadByUser(String recipientId, String notificationId );
}
