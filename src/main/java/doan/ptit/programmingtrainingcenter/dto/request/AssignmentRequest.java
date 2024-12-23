package doan.ptit.programmingtrainingcenter.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import doan.ptit.programmingtrainingcenter.entity.Assignment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentRequest {
    String classId;
    String title;
    String description;
    Assignment.Type type;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")  // Đảm bảo Spring nhận đúng định dạng
    Date dueDate;
    MultipartFile file;


}
