package doan.ptit.programmingtrainingcenter.dto.request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoursesRequest {
     String categoryId;
     String title;
     String description;
     int duration;
     String level;
     Double price;
     MultipartFile thumbnail;
     List<String> instructorIds;
}
