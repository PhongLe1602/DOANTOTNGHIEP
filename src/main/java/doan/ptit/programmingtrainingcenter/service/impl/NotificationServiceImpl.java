package doan.ptit.programmingtrainingcenter.service.impl;

import com.google.zxing.NotFoundException;
import doan.ptit.programmingtrainingcenter.dto.request.NotificationRequest;
import doan.ptit.programmingtrainingcenter.dto.response.NotificationRecipientResponse;
import doan.ptit.programmingtrainingcenter.dto.response.NotificationResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserNotificationResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserResponse;
import doan.ptit.programmingtrainingcenter.entity.Notification;
import doan.ptit.programmingtrainingcenter.entity.NotificationRecipient;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.mapper.NotificationMapper;
import doan.ptit.programmingtrainingcenter.repository.NotificationRecipientRepository;
import doan.ptit.programmingtrainingcenter.repository.NotificationRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationRecipientRepository notificationRecipientRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationResponse> getNotifications(String userId , String role) {

        // Thêm thông báo của SYSTEM vào danh sách
        List<Notification> notifications = new ArrayList<>(notificationRepository.findByType(Notification.Type.valueOf("SYSTEM")));

        // Thêm thông báo của role người dùng vào danh sách nếu có
        if (role != null) {
            notifications.addAll(notificationRepository.findByType(Notification.Type.valueOf(role)));
        }

        notifications.addAll(notificationRepository.findNotificationsForUser(userId));

        // Chuyển đổi từ Notification entity sang NotificationResponse DTO
        return notifications.stream()
                .map(notification -> NotificationResponse.builder()
                        .id(notification.getId())
                        .title(notification.getTitle())
                        .message(notification.getMessage())
                        .type(notification.getType())
                        .status(notification.getStatus())
                        .creator(UserResponse.builder()
                                .id(notification.getCreator().getId())
                                .name(notification.getCreator().getFullName())
                                .email(notification.getCreator().getEmail())
                                .build())
                        .createdAt(notification.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }



    @Override
    @Transactional
    public NotificationResponse createNotification(String creatorId, NotificationRequest notificationRequest) {

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

        // Kiểm tra nếu có danh sách recipientIds
        if (notificationRequest.getRecipientIds() != null && !notificationRequest.getRecipientIds().isEmpty()) {
            List<NotificationRecipient> recipients = notificationRequest.getRecipientIds().stream()
                    .map(recipientId -> NotificationRecipient.builder()
                            .notification(savedNotification)
                            .recipient(userRepository.findById(recipientId).orElseThrow(() ->
                                    new EntityNotFoundException("User with ID " + recipientId + " not found.")))
                            .isRead(false)
                            .build())
                    .toList();
            notificationRecipientRepository.saveAll(recipients);
        }

        return notificationMapper.toNotificationResponse(savedNotification);
    }



    @Override
    public UserNotificationResponse getNotificationsOfRecipient(String recipientId) {
        List<NotificationRecipient> notificationRecipients = notificationRecipientRepository.findByRecipientId(recipientId);
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
    @Transactional
    public void markAsReadByUser(String recipientId, String notificationId) {
        NotificationRecipient notificationRecipient = notificationRecipientRepository.findByRecipientIdAndNotificationId(recipientId,notificationId);
        notificationRecipient.setRead(true);
        notificationRecipientRepository.save(notificationRecipient);


    }

    @Override
    @Transactional
    public void sendNotificationToRecipients(String notificationId, List<String> recipientIds) {
        // Tìm thông báo theo ID
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification with ID " + notificationId + " not found."));

        // Kiểm tra danh sách recipientIds có hợp lệ không
        if (recipientIds == null || recipientIds.isEmpty()) {
            throw new IllegalArgumentException("Recipient list cannot be null or empty.");
        }

        // Tạo danh sách NotificationRecipient và lưu vào database
        List<NotificationRecipient> notificationRecipients = recipientIds.stream()
                .map(recipientId -> {
                    User recipient = userRepository.findById(recipientId)
                            .orElseThrow(() -> new EntityNotFoundException("User with ID " + recipientId + " not found."));

                    return NotificationRecipient.builder()
                            .notification(notification)
                            .recipient(recipient)
                            .isRead(false)
                            .build();
                })
                .collect(Collectors.toList());

        notificationRecipientRepository.saveAll(notificationRecipients);
    }

    @Override
    @Transactional
    public void sendNotificationToRecipient(String notificationId, String recipientId) {
        // Tìm thông báo theo ID
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification with ID " + notificationId + " not found."));

        // Tìm người nhận theo ID
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + recipientId + " not found."));

        // Tạo NotificationRecipient và lưu vào database
        NotificationRecipient notificationRecipient = NotificationRecipient.builder()
                .notification(notification)
                .recipient(recipient)
                .isRead(false)
                .build();

        notificationRecipientRepository.save(notificationRecipient);
    }

    @Override
    @Transactional
    public void sendSystemNotification(String title, String message, String recipientId) {
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .type(Notification.Type.SYSTEM)
                .status(Notification.Status.ACTIVE)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + recipientId + " not found."));
        NotificationRecipient notificationRecipient = NotificationRecipient.builder()
                .notification(savedNotification)
                .recipient(recipient)
                .isRead(false)
                .build();
        notificationRecipientRepository.save(notificationRecipient);
    }

}
