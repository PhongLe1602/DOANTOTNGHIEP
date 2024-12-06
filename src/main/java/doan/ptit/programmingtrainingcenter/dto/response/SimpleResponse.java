package doan.ptit.programmingtrainingcenter.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SimpleResponse {
    private String status;
    private String message;
    private LocalDateTime timestamp;

    public static SimpleResponse success(String message) {
        return SimpleResponse.builder()
                .status("success")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
