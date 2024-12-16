package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.NotificationRequest;
import doan.ptit.programmingtrainingcenter.dto.response.NotificationResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserNotificationResponse;
import doan.ptit.programmingtrainingcenter.entity.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    List<NotificationResponse> getNotifications(String userId , String role);
    NotificationResponse createNotification(String creatorId,NotificationRequest notificationRequest);
    UserNotificationResponse getNotificationsOfRecipient(String recipientId);
    void markAsALLReadByUser(String recipientId);
    void markAsReadByUser(String recipientId, String notificationId );
    void sendNotificationToRecipients(String notificationId, List<String> recipientIds);
    void sendNotificationToRecipient(String notificationId, String recipientId);
    void sendSystemNotification(String title, String message, String recipientId);
}
