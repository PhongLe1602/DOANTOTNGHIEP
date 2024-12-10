package doan.ptit.programmingtrainingcenter.service.impl;

import com.google.zxing.NotFoundException;
import doan.ptit.programmingtrainingcenter.dto.request.NotificationRequest;
import doan.ptit.programmingtrainingcenter.dto.response.NotificationRecipientResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserNotificationResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserResponse;
import doan.ptit.programmingtrainingcenter.entity.Notification;
import doan.ptit.programmingtrainingcenter.entity.NotificationRecipient;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.repository.NotificationRecipientRepository;
import doan.ptit.programmingtrainingcenter.repository.NotificationRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationRecipientRepository notificationRecipientRepository;
    private final UserRepository userRepository;

    @Override
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    @Transactional
    public Notification createNotification(String creatorId,NotificationRequest notificationRequest) {

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Notification notification = Notification.builder()
                .title(notificationRequest.getTitle())
                .message(notificationRequest.getMessage())
                .type(notificationRequest.getType())
                .status(notificationRequest.getStatus())
                .creator(creator)
                .build();

        Notification savedNotification = notificationRepository.save(notification);


        if (notificationRequest.getRecipientIds() == null || notificationRequest.getRecipientIds().isEmpty()) {
            throw new IllegalArgumentException("Recipient list cannot be null or empty.");
        }


        List<NotificationRecipient> recipients = notificationRequest.getRecipientIds().stream()
                .map(recipientId -> NotificationRecipient.builder()
                        .notification(savedNotification)
                        .recipient(userRepository.findById(recipientId).orElseThrow(() ->
                                new EntityNotFoundException("User with ID " + recipientId + " not found.")))
                        .isRead(false)
                        .build())
                .toList();

        notificationRecipientRepository.saveAll(recipients);

        return savedNotification;
    }


    @Override
    public UserNotificationResponse getNotificationsOfRecipient(String recipientId) {
        List<NotificationRecipient> notificationRecipients = notificationRecipientRepository.findByRecipientId(recipientId);
        User user = userRepository.findById(recipientId).orElseThrow(() ->new EntityNotFoundException("User with ID " + recipientId + " not found."));
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getFullName())
                .build();
        List<NotificationRecipientResponse> notifications = notificationRecipients.stream()
                .map(recipient -> NotificationRecipientResponse.builder()
                        .id(recipient.getId())
                        .title(recipient.getNotification().getTitle())
                        .message(recipient.getNotification().getMessage())
                        .type(recipient.getNotification().getType())
                        .readAt(recipient.getNotification().getCreatedAt())
                        .build())
                .toList();
        return UserNotificationResponse.builder()
                .user(userResponse)
                .notifications(notifications)
                .build();
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
