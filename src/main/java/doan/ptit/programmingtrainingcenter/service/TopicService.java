package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.TopicRequest;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.Topic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TopicService {
    List<Topic> getTopics();
    Topic getTopic(String id);
    Topic addTopic(TopicRequest topicRequest);
    Topic updateTopic(String id, TopicRequest topicRequest);
    void deleteTopic(String id);
    void addCourseToTopic(String topicId, String courseId);
    void removeCourseFromTopic(String topicId, String courseId);
    List<Course> getCoursesByTopic(String topicId);
}
