package doan.ptit.programmingtrainingcenter.repository;

import doan.ptit.programmingtrainingcenter.dto.response.NotificationResponse;
import doan.ptit.programmingtrainingcenter.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByType(Notification.Type type);

    @Query("SELECT nr.notification FROM NotificationRecipient nr WHERE nr.recipient.id = :userId")
    List<Notification> findNotificationsForUser(@Param("userId") String userId);
}
