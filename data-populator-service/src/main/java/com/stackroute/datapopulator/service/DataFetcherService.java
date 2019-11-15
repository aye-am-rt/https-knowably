package com.stackroute.datapopulator.service;

import com.google.gson.Gson;
import com.stackroute.datapopulator.domain.InfoModel;
import com.stackroute.datapopulator.repository.DataPopulatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataFetcherService {
    @Autowired
    private DataPopulatorRepository dataRepository;
    private String topic="SchedulerResult";
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    //----------method to fetch domain info from the database and writes to SchedulerResult topic------------
    public void dataFetcher() {
        Map tempmap1=new HashMap();
       	List<Map> result=new ArrayList<>();
       List<String> temp= new ArrayList<>();
       InfoModel infoModel=new InfoModel();
       //returns all the node labels
       List<String> nodes = dataRepository.getNodeLabels();
       //returns all the relationships labels
      List<String> relations = dataRepository.getRelations();
        tempmap1.put("key","labels");
        tempmap1.put("value",nodes);
        result.add(tempmap1);
        for (String a : nodes
        ) {
            Map tempmap2=new HashMap();
            temp = dataRepository.getNodeNames(a);
            tempmap2.put("key",a);
            tempmap2.put("value",temp);
            result.add(tempmap2);
        }
        Map tempmap3=new HashMap();
        tempmap3.put("key","relations");
        tempmap3.put("value",dataRepository.getRelations());
        result.add(tempmap3);
        infoModel.setInfo(result);
        Gson gson = new Gson();
        String json = gson.toJson(infoModel);
        kafkaTemplate.send(topic, json);
    }
}
