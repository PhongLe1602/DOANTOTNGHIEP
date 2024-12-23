package doan.ptit.programmingtrainingcenter.dto.request;


import doan.ptit.programmingtrainingcenter.entity.StudentSubmission;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class StudentSubmissionRequest {
    String assignmentId;
    MultipartFile file;


}
