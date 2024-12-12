package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.LessonRequest;
import doan.ptit.programmingtrainingcenter.entity.Lesson;
import doan.ptit.programmingtrainingcenter.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @PostMapping
    Lesson createLesson(@RequestBody LessonRequest lessonRequest) {
        return lessonService.addLesson(lessonRequest);
    }

}
