package doan.ptit.programmingtrainingcenter.mapper;


import doan.ptit.programmingtrainingcenter.dto.response.SectionResponse;
import doan.ptit.programmingtrainingcenter.entity.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = LessonMapper.class)
public interface SectionMapper {

    @Mapping(target = "lessonList", source = "lessons")
    SectionResponse toDto(Section section);


    List<SectionResponse> toDtoList(List<Section> sectionList);
}
