package com.stackroute.datapopulator.googlesearchapiservicedemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.datapopulator.googlesearchapiservicedemo.domain.Input;
import com.stackroute.datapopulator.googlesearchapiservicedemo.domain.SearchResult;
import com.stackroute.datapopulator.googlesearchapiservicedemo.domain.UiInputInt;
import com.stackroute.datapopulator.googlesearchapiservicedemo.service.GoogleSearchService;
import com.stackroute.datapopulator.googlesearchapiservicedemo.domain.UiInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Api(value = "Google Search Service Rest API")
@RestController
@CrossOrigin("*")
@RequestMapping(value = "api/v1")
public class GoogleSearchController {
    GoogleSearchService googleSearchService;
    KafkaTemplate<String,String> kafkaTemplate;
    private static final Logger LOGGER= LoggerFactory.getLogger(GoogleSearchController.class);

    private Input input;
    private ResponseEntity responseEntity;
    @Autowired
    public GoogleSearchController(GoogleSearchService googleSearchService, KafkaTemplate<String, String> kafkaTemplate)
    {
        this.googleSearchService = googleSearchService;
        this.kafkaTemplate = kafkaTemplate;
    }
    @ApiOperation(value = "Fetch weblinks Related to Domain and Concepts")
    @PostMapping("domain")
    public ResponseEntity<String> domainUpload(@RequestBody UiInput input1)
    {
        try {
            input = new Input();
            input.setDomain(input1.getDomain());
            String[] conceptArray = input1.getConcept().split(",");
            input.setConcepts(conceptArray);
            input.setUserId(input1.getUserId());
            //Saving the expert input to a Cache database
                googleSearchService.saveCache(input);
                List<CompletableFuture<SearchResult>> allFutures = new ArrayList<>();
                for(int i=0;i<conceptArray.length;i++)
                {
                    allFutures.add(googleSearchService.getLinks(null,input1.getUserId(),input1.getDomain(),conceptArray[i].trim()));
                }
                CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();
                List<SearchResult> list = new ArrayList<>();
                List<SearchResult> list1 = new ArrayList<>();
                List<SearchResult> list2= new ArrayList<>();
                //Separating the list based on direct wikipedia output or web crawler output
                for(int i=0;i<conceptArray.length;i++)
                {
                    SearchResult test = allFutures.get(i).get();
                    list.add(test);
                    if(test.getUrl()[1]==null)
                    {
                        list1.add(test);
                    }
                    else
                    {
                        list2.add(test);
                    }
                }
                //Converting object list to JSON String to write to Kafka
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(list1);
                String json2 = mapper.writeValueAsString(list2);
                if(input.getDomain().equalsIgnoreCase("medical")) kafkaTemplate.send("Wikipedia",json);
                else kafkaTemplate.send("MovieWikipedia",json);
                kafkaTemplate.send("TopicTest",json2);
                responseEntity = new ResponseEntity<List<SearchResult>>(list, HttpStatus.OK);

        } catch (Exception e)
        {

            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
        }
        return responseEntity;
    }
    @ApiOperation(value = "Triggering the same operations as above function but for internal triggering")
    @PostMapping("domain/internal")
    public ResponseEntity<?> domainUploadInternal(@RequestBody UiInputInt input1)
    {
        try {
            input = new Input();
            input.setDomain(input1.getDomain());
            String[] conceptArray = input1.getConcept().split(",");
            input.setConcepts(conceptArray);
            input.setUserId(input1.getUserId());
            List<CompletableFuture<SearchResult>> allFutures = new ArrayList<>();
            for(int i=0;i<conceptArray.length;i++)
            {
                allFutures.add(googleSearchService.getLinks(input1.getQuery(),input1.getUserId(),input1.getDomain(),conceptArray[i].trim()));
            }
            CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();
            List<SearchResult> list = new ArrayList<>();
            List<SearchResult> list1 = new ArrayList<>();
            List<SearchResult> list2= new ArrayList<>();
            //Separating the list based on direct wikipedia output or web crawler output
            for(int i=0;i<conceptArray.length;i++)
            {
                SearchResult test = allFutures.get(i).get();
                list.add(test);
                if(test.getUrl()[1]==null)
                {
                    list1.add(test);
                }
                else
                {
                    list2.add(test);
                }
            }
            //Converting object list to JSON String to write to Kafka
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(list1);
            String json2 = mapper.writeValueAsString(list2);
            if(input.getDomain().equalsIgnoreCase("medical")) kafkaTemplate.send("Wikipedia",json);
            else kafkaTemplate.send("MovieWikipedia",json);
            kafkaTemplate.send("TopicTest",json2);
            responseEntity = new ResponseEntity<List<SearchResult>>(list, HttpStatus.OK);
        } catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
        }
        return responseEntity;
    }
}
