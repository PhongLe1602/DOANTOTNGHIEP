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

    @Override
    public Lesson updateLesson(String lessonId, LessonRequest lessonRequest) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson không tồn tại"));

        lesson.setTitle(lessonRequest.getTitle());
        lesson.setContent(lessonRequest.getContent());
        lesson.setDuration(lessonRequest.getDuration());
        lesson.setVideoLink(lessonRequest.getVideoLink());

        // Cập nhật Section nếu cần thay đổi
        if (lessonRequest.getSectionId() != null) {
            Section section = sectionRepository.findById(lessonRequest.getSectionId())
                    .orElseThrow(() -> new RuntimeException("Section không tồn tại"));
            lesson.setSection(section);
        }

        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteLesson(String lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson không tồn tại"));

        lessonRepository.delete(lesson);
    }

    @Override
    public void removeLessonFromSection(String lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson không tồn tại"));

        lesson.setSection(null);
        lessonRepository.save(lesson);
    }

}
