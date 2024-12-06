package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.NotificationRequest;
import doan.ptit.programmingtrainingcenter.entity.Notification;
import doan.ptit.programmingtrainingcenter.entity.NotificationRecipient;
import doan.ptit.programmingtrainingcenter.repository.NotificationRecipientRepository;
import doan.ptit.programmingtrainingcenter.repository.NotificationRepository;
import doan.ptit.programmingtrainingcenter.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationRecipientRepository notificationRecipientRepository;


    @Override
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification createNotification(NotificationRequest notificationRequest) {
        Notification notification = Notification.builder()
                .title(notificationRequest.getTitle())
                .message(notificationRequest.getMessage())
                .type(notificationRequest.getType())
                .status(notificationRequest.getStatus())
                .build();
        Notification savedNotification = notificationRepository.save(notification);
        notificationRequest.getRecipientIds().forEach(recipientId -> {
            NotificationRecipient recipient = NotificationRecipient.builder()
                    .notification(savedNotification)
                    .recipientId(recipientId)
                    .isRead(false)
                    .build();
            notificationRecipientRepository.save(recipient);
        });
        return savedNotification;
    }

    @Override
    public List<NotificationRecipient> getNotificationsOfRecipient(String recipientId) {
        return notificationRecipientRepository.findByRecipientId(recipientId);
    }

    @Override
    public void markAsALLReadByUser(String recipientId) {
       List<NotificationRecipient> notificationRecipients = notificationRecipientRepository.findByRecipientIdAndIsRead(recipientId,false);
        for (NotificationRecipient recipient : notificationRecipients) {
            if (!recipient.isRead()) {
                recipient.setRead(true);
                notificationRecipientRepository.save(recipient);
            }
        }

    }

    @Override
    public void markAsReadByUser(String recipientId, String notificationId) {
        NotificationRecipient notificationRecipient = notificationRecipientRepository.findByRecipientIdAndNotificationId(recipientId,notificationId);
        notificationRecipient.setRead(true);
        notificationRecipientRepository.save(notificationRecipient);


    }

}
