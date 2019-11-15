package com.stackroute.analyticsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.analyticsservice.repository.QueryResultResponse;
import com.stackroute.analyticsservice.domain.AnalyticsOutput;
import com.stackroute.analyticsservice.domain.Response;
import com.stackroute.analyticsservice.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1")
public class AnalyticsServiceController {
    private ResponseEntity responseEntity;
    private final AnalyticsService analyticsService;
    @Autowired
    public AnalyticsServiceController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }
    //Saving response from user to cassandra
    @PostMapping("response")
    public ResponseEntity<String> processResponse(@RequestBody Response response){
        List<String> list = new ArrayList<>();
        try{
            //Checks if response already exists in cassandra and updates, else it adds to cassandra
            if(analyticsService.existsByQuery(response.getQuery()))
            {
                analyticsService.updateQuery(response);
                //Sends response as json as frontend is expecting json string
                list.add("Updated Successfully");
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(list);
                responseEntity = new ResponseEntity<String>(json, HttpStatus.OK);
            }
            else
            {
                analyticsService.saveQuery(response);
                //Sends response as json as frontend is expecting json string
                list.add("Added Successfully");
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(list);
                responseEntity = new ResponseEntity<String>(json,HttpStatus.OK);
            }
        } catch (Exception e)
        {
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
        }
        return responseEntity;
    }
    //Returns all responses from cassandra
    @GetMapping("display")
    public ResponseEntity<?> showResults() {
        try {
            List<QueryResultResponse> list = analyticsService.getResults();
            //Convert List to array as angular requires array
            QueryResultResponse[] list1 = new QueryResultResponse[list.size()];
            int i=0;
            for(QueryResultResponse a:list)
            {
                list1[i] = a;
                i++;
            }
            responseEntity = new ResponseEntity<QueryResultResponse[]>(list1,HttpStatus.FOUND);
        } catch (Exception e)
        {
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
        }
        return responseEntity;
    }
    //Returns all responses in movie domain from cassandra
    @GetMapping("display/movie")
    public ResponseEntity<?> showMovieResults() {
        try {
            List<QueryResultResponse> list = analyticsService.getResultsByDomain("movie");
            //Convert List to array as angular requires array
            QueryResultResponse[] list1 = new QueryResultResponse[list.size()];
            int i=0;
            for(QueryResultResponse a:list)
            {
                list1[i] = a;
                i++;
            }
            responseEntity = new ResponseEntity<QueryResultResponse[]>(list1,HttpStatus.FOUND);
        } catch (Exception e)
        {
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
        }
        return responseEntity;
    }
    //Returns all responses in medical domain from cassandra
    @GetMapping("display/medical")
    public ResponseEntity<?> showMedicalResults() {
        try {
            List<QueryResultResponse> list = analyticsService.getResultsByDomain("medical");
            //Convert List to array as angular requires array
            QueryResultResponse[] list1 = new QueryResultResponse[list.size()];
            int i=0;
            for(QueryResultResponse a:list)
            {
                list1[i] = a;
                i++;
            }
            responseEntity = new ResponseEntity<QueryResultResponse[]>(list1,HttpStatus.FOUND);
        } catch (Exception e)
        {
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
        }
        return responseEntity;
    }
    //Returns analytics output for graphing
    @GetMapping("analytics")
    public ResponseEntity<?> getGraphData() {
        try {
            responseEntity = new ResponseEntity<AnalyticsOutput>(analyticsService.getAnalyticsData(),HttpStatus.FOUND);
        } catch (Exception e)
        {
            responseEntity = new ResponseEntity<String>(e.getMessage(),HttpStatus.CONFLICT);
        }
        return responseEntity;
    }
}
