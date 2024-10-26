package doan.ptit.programmingtrainingcenter.dto.request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoursesRequest {
     String title;
     String description;
     int duration;
     String level;
     Double price;
     String thumbnail;
}
