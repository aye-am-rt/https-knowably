package com.knowably.notificationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.knowably.notificationservice.domain.DomainData;
import com.knowably.notificationservice.domain.QueryResponse;
import com.knowably.notificationservice.domain.ResponseDomain;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * RestController annotation is used to create Restful web services using Spring MVC
 */
@PropertySource("classpath:application.properties")
@RestController
public class NotificationController {
    private static final Logger LOGGER= LoggerFactory.getLogger(NotificationController.class);
    private QueryResponse queryResponse;
    private List<DomainData> lists;
    private List<String> frontendData;
    private List<ResponseDomain> responseDomains;
    @Value("${topic}")
    /* the topic name from which the service publishes to Kafka*/
    private String topic;
    /**
     * Constructor based Dependency injection to inject SimpMessagingTemplate into controller
     */
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * Constructor based Dependency injection to inject KafkaTemplate into controller
     */

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;


    /**Checking the service with the postman to make sure it is working fine*/

    /**
     * Postmapping will call the uploadContent method and takes queryresponse object as a parameter in the requestBody
     */
    @PostMapping("/content")
    public void uploadContent(@RequestBody QueryResponse queryResponse) {
        this.queryResponse = new QueryResponse();
        this.queryResponse = queryResponse;
    }


    /**
     * GetMapping will call the getNotification method
     */
    @GetMapping("/content")
    public String getNotification() {
        template.convertAndSend("/topic/notification", queryResponse.getResult()[0]);

        return "Notifications successfully sent to Angular !";
    }


    /**
     * The @MessageMapping annotation ensures that if a notification is sent to destination "/notification",
     * then the getNotification() method is called.
     */

    @GetMapping("/formdata")
    @MessageMapping("/formdata")

    public String getNotification1(String string) throws JsonProcessingException {
        responseDomains=new ArrayList<>();
        JSONObject jsonObject = new JSONObject(string);
        DomainData data = new DomainData();
        for (int i = 0; i < lists.size(); i++) {
            ResponseDomain responseDomain = new ResponseDomain();

            data = lists.get(i);
            responseDomain.setKey(data.getKey());
            responseDomain.setType(data.getType());
            responseDomain.setValue(Collections.singletonList((String) jsonObject.get(data.getKey().toUpperCase())));
            String str="status"+data.getKey().toLowerCase();
            try {
                if (jsonObject.get(str) != null) {
                    responseDomain.setStatus((String) jsonObject.get(str));
                }
            }
            catch (Exception e) {
                responseDomain.setStatus("none");
                LOGGER.error(e.getMessage());
            }
            responseDomains.add(responseDomain);

        }
        ObjectMapper mapper=new ObjectMapper();
        String result=mapper.writeValueAsString(responseDomains);
        kafkaTemplate.send(topic,result);

        return "success";
    }


    /**
     * Kafka listener which can be used to call consume method when something is published in the finalresult Topic
     */


    @KafkaListener(topics ="FinalResult",groupId = "service",containerFactory = "kafkaListenerContainerFactory")
    public void consume(String receivedResponse) throws JsonProcessingException {

        frontendData=new ArrayList<>();

        Gson gson = new Gson();
        QueryResponse queryResponse1 = gson.fromJson(receivedResponse, QueryResponse.class);


        frontendData.add(queryResponse1.getQuery());
        if (queryResponse1.getResult()!=null) {
            for (int i = 0; i < queryResponse1.getResult().length; i++) {
                frontendData.add(queryResponse1.getResult()[i]);
            }
        }
//        *The return value is broadcast to users who subscribes to "/topic/notification"

        ObjectMapper mapper=new ObjectMapper();
        String result=mapper.writeValueAsString(queryResponse1);
        template.convertAndSend("/topic/notification", result);
    }

    /** Gets data from the resultfetcher which need to displayed in the frontend for the expert-user*/
    /** postmapping to Test it using PostMan*/
    @PostMapping("/domain")
    /**
     * Kafka listener which can be used to call getDomainData method when something is published in the DomainData Topic
     */
    @KafkaListener(topics = "DomainData",groupId = "service")
    public void getDomainData( String receivedResponse) {
        lists=new ArrayList<>();
        JSONArray jsonArray = new JSONArray(receivedResponse);

        for (int i = 0; i < jsonArray.length(); i++) {
            String temp = jsonArray.getJSONObject(i).toString();
            Gson gson = new Gson();
            DomainData data = gson.fromJson(temp, DomainData.class);
            lists.add(data);
        }

        template.convertAndSend("/queue/domain", lists);



    }
}
