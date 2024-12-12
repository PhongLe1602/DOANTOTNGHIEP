package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.TopicRequest;
import doan.ptit.programmingtrainingcenter.entity.Topic;
import doan.ptit.programmingtrainingcenter.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @GetMapping
    List<Topic> getTopic() {
        return topicService.getTopics();
    }
    @PostMapping
    Topic addTopic(@RequestBody TopicRequest topicRequest) {
        return topicService.addTopic(topicRequest);
    }
}
