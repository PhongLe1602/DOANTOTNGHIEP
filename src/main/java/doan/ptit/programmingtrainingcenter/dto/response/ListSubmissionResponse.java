package doan.ptit.programmingtrainingcenter.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)


public class ListSubmissionResponse {
    AssignmentResponse assignment;
    List<StudentSubmissionResponse> studentSubmissions;

}
