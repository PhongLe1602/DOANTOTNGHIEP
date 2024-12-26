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
    @PutMapping("/{lessonId}")
    public Lesson updateLesson(@PathVariable String lessonId, @RequestBody LessonRequest lessonRequest) {
        return lessonService.updateLesson(lessonId, lessonRequest);
    }

    @DeleteMapping("/{lessonId}")
    public void deleteLesson(@PathVariable String lessonId) {
        lessonService.deleteLesson(lessonId);
    }

    @PutMapping("/{lessonId}/remove-from-section")
    public void removeLessonFromSection(@PathVariable String lessonId) {
        lessonService.removeLessonFromSection(lessonId);
    }

}
