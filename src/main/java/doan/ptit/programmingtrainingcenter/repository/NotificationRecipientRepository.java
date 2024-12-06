package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.entity.NotificationRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, String> {
    List<NotificationRecipient> findByRecipientId(String recipientId);
    List<NotificationRecipient> findByRecipientIdAndIsRead(String recipientId, boolean isRead);
    NotificationRecipient findByRecipientIdAndNotificationId(String recipientId, String notificationId);
}

