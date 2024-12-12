package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse <T> {
    private String status;
    private String message;
    private T data;
    private LocalDateTime timestamp;


//    public ApiResponse(String status, String message, T data) {
//        this.status = status;
//        this.message = message;
//        this.data = data;
//        this.timestamp = LocalDateTime.now();
//    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
