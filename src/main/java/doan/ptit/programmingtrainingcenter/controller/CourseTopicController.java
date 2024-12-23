package doan.ptit.programmingtrainingcenter.controller;

import doan.ptit.programmingtrainingcenter.dto.request.TopicRequest;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.Topic;
import doan.ptit.programmingtrainingcenter.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-topic")
public class CourseTopicController {

    @Autowired
    private TopicService topicService;

    // Lấy tất cả các topic
    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> getTopics() {
        List<Topic> topics = topicService.getTopics();
        return ResponseEntity.ok(topics);
    }

    // Lấy thông tin một topic theo ID
    @GetMapping("/topic/{id}")
    public ResponseEntity<Topic> getTopic(@PathVariable String id) {
        Topic topic = topicService.getTopic(id);
        return ResponseEntity.ok(topic);
    }

    // Thêm một topic mới
    @PostMapping("/topic")
    public ResponseEntity<Topic> addTopic(@RequestBody TopicRequest topicRequest) {
        Topic topic = topicService.addTopic(topicRequest);
        return ResponseEntity.ok(topic);
    }

    // Cập nhật thông tin topic
    @PutMapping("/topic/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable String id, @RequestBody TopicRequest topicRequest) {
        Topic topic = topicService.updateTopic(id, topicRequest);
        return ResponseEntity.ok(topic);
    }

    // Xóa topic
    @DeleteMapping("/topic/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable String id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok("Topic deleted successfully.");
    }

    // Thêm khóa học vào topic
    @PostMapping("/add-course")
    public ResponseEntity<String> addCourseToTopic(@RequestParam String topicId, @RequestParam String courseId) {
        try {
            topicService.addCourseToTopic(topicId, courseId);
            return ResponseEntity.ok("Course added to topic successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding course to topic: " + e.getMessage());
        }
    }

    // Xóa khóa học khỏi topic
    @DeleteMapping("/remove-course")
    public ResponseEntity<String> removeCourseFromTopic(@RequestParam String topicId, @RequestParam String courseId) {
        try {
            topicService.removeCourseFromTopic(topicId, courseId);
            return ResponseEntity.ok("Course removed from topic successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error removing course from topic: " + e.getMessage());
        }
    }
    @GetMapping("/courses/{topicId}")
    public ResponseEntity<List<Course>> getCoursesByTopic(@PathVariable String topicId) {
        List<Course> courses = topicService.getCoursesByTopic(topicId);
        return ResponseEntity.ok(courses);
    }
}
