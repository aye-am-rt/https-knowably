package com.stackroute.datapopulator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.stackroute.datapopulator.domain.Input;
import com.stackroute.datapopulator.domain.WebCrawl;
import com.stackroute.datapopulator.service.WebCrawlerService;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/*Rest controller  to use all the rest functionalities*/
@RestController
/*Crossorigin to allow all requests*/
@CrossOrigin("*")
/*To fetch property values from application.properties*/
@PropertySource("classpath:application.properties")
/*To map request with the given Route*/
@RequestMapping(value = "api/v1")
public class WebCrawlerController {

    private WebCrawlerService webCrawlerService;
    private static final Logger LOGGER= LoggerFactory.getLogger(WebCrawlerController.class);

    /*Property value which can be fetched from application.properties*/
    @Value("${topic}")
    /*Topicpayload the topic name from which the service listens from Kafka*/
    private String topic;
    /*CompletableFuture is used for asynchronous programming in Java. Asynchronous programming is a means

    of writing non-blocking code by running a task on a separate thread than the main application thread

    and notifying the main thread about its progress, completion or failure.*/
    List<CompletableFuture<WebCrawl>> results;
    List<WebCrawl> list1;


    /*Kafka template which can be used to publish messages to Kafka*/
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;


    @Autowired
    public WebCrawlerController(WebCrawlerService webCrawlerService) {
        this.webCrawlerService = webCrawlerService;
    }




    //Kafkalistener which helps the controller to listen to the topic in Kafka and consume data from Kafka topic
    @KafkaListener(topics ="TopicTest",groupId = "services",containerFactory = "kafkaListenerContainerFactory")
    public void consume(String receivedInput) throws ExecutionException, InterruptedException {
       /*Received input can be stringified JsonArray*/

        results=new ArrayList<>();
        String receivedInput2;
        /*Parsing Received Stringified JsonArray to JsonArray*/
        JSONArray array = new JSONArray(receivedInput);

        /*Accessing Each Json Object from Json Array stringifying it again and parsing it using GSON(google Gson)*/
        for (int i=0;i<array.length();i++) {
            receivedInput2 = array.getJSONObject(i).toString();

            /*Gson to parse each Json object required custom java object(INPUT class)*/
            Gson gson = new Gson();
            Input input1 = gson.fromJson(receivedInput2, Input.class);

            /*Adding the result to results list<CompletableFuture<WebCrawl>>*/
            results.add(webCrawlerService.getContent(input1));

        }
        /*Joining the results list*/
        CompletableFuture.allOf(results.toArray(new CompletableFuture[0])).join();
        List<WebCrawl> list=new ArrayList<>();

        /*Adding the result list which is of type list of completableFuture<webcrawls> to the list which is of type list of webcrawls*/
        for (int i=0;i<array.length();i++){
            list.add(results.get(i).get());
        }

/*Object mapper which is used to map object as whatever type we want*/
        ObjectMapper objectMapper = new ObjectMapper();




        try {

            /*Converting list of json objects to String */
                String resultPayload = objectMapper.writeValueAsString(list);
                /*Publishing the Stringified Json to the kafka topic*/
                kafkaTemplate.send(topic, resultPayload);

            }
        /*Handling the Json Parse exception* */
        catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage());
            }


    }

}
