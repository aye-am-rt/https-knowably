package com.stackroute.controller;
import com.stackroute.domain.DataModel;
import com.stackroute.service.RedisService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RedisServiceController {
    private RedisService redisService;
    public RedisServiceController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping
    @KafkaListener(topics = "SchedulerResult", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public boolean add(String param) {
        redisService.populate(param);
        return true;
    }

    @GetMapping
    public Map<String, DataModel> all() {
        return redisService.getAll();
    }
}
