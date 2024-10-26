package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.LessonRequest;
import doan.ptit.programmingtrainingcenter.entity.Lesson;
import doan.ptit.programmingtrainingcenter.entity.Section;
import doan.ptit.programmingtrainingcenter.repository.LessonRepository;
import doan.ptit.programmingtrainingcenter.repository.SectionRepository;
import doan.ptit.programmingtrainingcenter.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Override
    public Lesson addLesson(LessonRequest lessonRequest) {
        Lesson lesson = new Lesson();
        Section section = sectionRepository.findById(lessonRequest.getSectionId()).orElseThrow(() -> new RuntimeException("Section Not Found"));
        lesson.setTitle(lessonRequest.getTitle());
        lesson.setContent(lessonRequest.getContent());
        lesson.setDuration(lessonRequest.getDuration());
        lesson.setSection(section);

        return lessonRepository.save(lesson);
    }
}
