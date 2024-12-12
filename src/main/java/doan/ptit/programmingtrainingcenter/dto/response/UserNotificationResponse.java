package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationResponse {
    private UserResponse user;
    private List<NotificationRecipientResponse> notifications;
}
