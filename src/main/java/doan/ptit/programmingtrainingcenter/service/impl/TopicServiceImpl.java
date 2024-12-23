package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.TopicRequest;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.CourseTopic;
import doan.ptit.programmingtrainingcenter.entity.Topic;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseTopicRepository;
import doan.ptit.programmingtrainingcenter.repository.TopicRepository;
import doan.ptit.programmingtrainingcenter.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    TopicRepository topicRepository;


    @Autowired
    private CourseTopicRepository courseTopicRepository;

    @Override
    public List<Topic> getTopics() {
        return topicRepository.findAll();
    }

    @Override
    public Topic getTopic(String id) {
        return topicRepository.findById(id).orElseThrow(() -> new RuntimeException("Topic Not Found"));
    }

    @Override
    public Topic addTopic(TopicRequest topicRequest) {
        Topic topic = new Topic();
        topic.setName(topicRequest.getName());
        topic.setDescription(topicRequest.getDescription());
        return topicRepository.save(topic);
    }

    @Override
    public Topic updateTopic(String id, TopicRequest topicRequest) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new RuntimeException("Topic Not Found"));
        topic.setName(topicRequest.getName());
        topic.setDescription(topicRequest.getDescription());
        return topicRepository.save(topic);
    }

    @Override
    @Transactional
    public void deleteTopic(String id) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new RuntimeException("Topic Not Found"));
        courseTopicRepository.deleteByTopicId(id);
        topicRepository.delete(topic);
    }

    @Override
    public void addCourseToTopic(String topicId, String courseId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Topic Not Found"));
        Course course = new Course();  // You can retrieve the course from the database as needed
        // Assuming a method to get the Course by courseId
        course.setId(courseId);

        CourseTopic courseTopic = new CourseTopic();
        courseTopic.setCourse(course);
        courseTopic.setTopic(topic);
        courseTopicRepository.save(courseTopic);
    }

    @Override
    public void removeCourseFromTopic(String topicId, String courseId) {



        Optional<CourseTopic> courseTopic = courseTopicRepository.findByCourseIdAndTopicId(courseId, topicId);
        if (courseTopic.isPresent()) {
            courseTopicRepository.delete(courseTopic.get());
        } else {
            throw new RuntimeException("Course not found in this topic");
        }
    }

    @Override
    public List<Course> getCoursesByTopic(String topicId) {
        List<CourseTopic> courseTopics = courseTopicRepository.findByTopicId(topicId);


        List<Course> courses = new ArrayList<>();
        for (CourseTopic courseTopic : courseTopics) {
            courses.add(courseTopic.getCourse());
        }

        return courses;
    }
}
