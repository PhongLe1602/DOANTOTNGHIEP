package doan.ptit.programmingtrainingcenter.mapper;


import doan.ptit.programmingtrainingcenter.dto.response.LessonResponse;
import doan.ptit.programmingtrainingcenter.entity.Lesson;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    LessonResponse toLessonResponse(Lesson lesson);

    List<LessonResponse> toLessonResponseList(List<Lesson> lessons);
}
