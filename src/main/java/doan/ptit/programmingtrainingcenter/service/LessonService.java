package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.LessonRequest;
import doan.ptit.programmingtrainingcenter.entity.Lesson;
import org.springframework.stereotype.Service;

@Service
public interface LessonService {
    Lesson addLesson(LessonRequest lessonRequest);
}
