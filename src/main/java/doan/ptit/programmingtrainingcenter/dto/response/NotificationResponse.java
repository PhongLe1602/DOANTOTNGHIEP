package doan.ptit.programmingtrainingcenter.dto.response;

import doan.ptit.programmingtrainingcenter.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private String title;
    private String message;
    private Notification.Type type;
    private Notification.Status status;
    private UserResponse creator;
    private Date createdAt;
}
