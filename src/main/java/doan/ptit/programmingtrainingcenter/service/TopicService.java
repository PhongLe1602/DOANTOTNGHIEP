package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.TopicRequest;
import doan.ptit.programmingtrainingcenter.entity.Topic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TopicService {
    List<Topic> getTopics();
    Topic getTopic(int id);
    Topic addTopic(TopicRequest topicRequest);
}
