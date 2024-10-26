package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.SectionRequest;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.Section;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
import doan.ptit.programmingtrainingcenter.repository.SectionRepository;
import doan.ptit.programmingtrainingcenter.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SectionServiceImpl implements SectionService {
    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Override
    public List<Section> getSections() {
        return sectionRepository.findAll();
    }

    @Override
    public Section addSection(SectionRequest sectionRequest) {
        Section section = new Section();
        Course course = courseRepository.findById(sectionRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));
        section.setCourse(course);
        section.setTitle(sectionRequest.getTitle());
        section.setDescription(sectionRequest.getDescription());

        return sectionRepository.save(section);
    }

    @Override
    public List<Section> getSectionsByCourses(String courseId) {
        return sectionRepository.findByCourseId(courseId);
    }
}
