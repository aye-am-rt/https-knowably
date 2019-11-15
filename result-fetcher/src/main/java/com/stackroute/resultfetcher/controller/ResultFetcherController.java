package com.stackroute.resultfetcher.controller;
import com.stackroute.resultfetcher.service.ResultFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class ResultFetcherController {
    private ResultFetcherService resultFetcherService;
    @Autowired
    public ResultFetcherController(ResultFetcherService resultFetcherService) {
        this.resultFetcherService = resultFetcherService;
    }
    //---------------------controller method to fetch result--------------------------------
    @GetMapping
    @KafkaListener(topics = "QueryResult", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public boolean resultController(String param)  {
        resultFetcherService.resultFetcher(param);
        return true;
    }
}
