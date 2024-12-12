package doan.ptit.programmingtrainingcenter.dto.request;

import doan.ptit.programmingtrainingcenter.entity.Notification;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotNull(message = "Title cannot be null")
    private String title;

    @NotNull(message = "Message cannot be null")
    private String message;

    @NotNull(message = "Type cannot be null")
    private Notification.Type type;

    @NotNull(message = "Status cannot be null")
    private Notification.Status status;

    @NotNull(message = "Creator ID cannot be null")
    private String creatorId;

    private List<String> recipientIds;





}