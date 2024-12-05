package doan.ptit.programmingtrainingcenter.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiPageResponse<T> {
    private String status;
    private String message;
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private LocalDateTime timestamp;

    public ApiPageResponse(Page<T> page, String message) {
        this.status = "success";
        this.message = message;
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiPageResponse<T> success(Page<T> page, String message) {
        return new ApiPageResponse<>(page, message);
    }
}