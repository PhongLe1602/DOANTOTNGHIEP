package doan.ptit.programmingtrainingcenter.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ApiListResponse<T> {
    private int statusCode;
    private String message;
    private List<T> data;
    private LocalDateTime timestamp;

    public static <T> ApiListResponse<T> success(int statusCode, String message, List<T> data) {
        return ApiListResponse.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiListResponse<T> error(int statusCode, String message, List<T> data) {
        return ApiListResponse.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
