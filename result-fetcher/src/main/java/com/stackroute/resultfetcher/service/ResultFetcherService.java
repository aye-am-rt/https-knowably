package com.stackroute.resultfetcher.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.stackroute.resultfetcher.domain.ProcessedQueryModel;
import com.stackroute.resultfetcher.domain.ResultModel;
import com.stackroute.resultfetcher.repository.DataRepository;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ResultFetcherService {
    private DataRepository dataRepository;
    @Autowired
    public ResultFetcherService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }
    private String topic="FinalResult";
    private String value = "value";
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    public void resultFetcher(String param) {                    //method to fetch data from database
        ResultModel resultModel=new ResultModel();
        Gson gson = new Gson();
        List<String> matchedConstraints;
        String[] result;
        List<String> tempResult=new ArrayList<>();
        ProcessedQueryModel processedQueryModel = gson.fromJson(param.toLowerCase(), ProcessedQueryModel.class);
        ObjectMapper om=new ObjectMapper();
        List forValuesOfConstraints=new ArrayList<>();                 //collects constraints to join them with "and" later
        List forValuesOfQueryResults=new ArrayList();                   //collects query results to join them with "and" later
        //------------------for every required query result---------------------------------------
        for (String a: processedQueryModel.getQueryresult()
        ) {
            if(dataRepository.checkIfNode(a)){                                     //if required is a node
                List<Map> nodes;
                List<String>node;
                if(processedQueryModel.getConstraints().size()>1){                  //for more than one constraint
                    matchedConstraints=dataRepository.matchConstraints(param.toLowerCase());
                    node=dataRepository.getResultNode(a,matchedConstraints);              //gives common nodes
                    for (Map m:processedQueryModel.getConstraints()
                    ) {
                        if(!forValuesOfConstraints.contains(WordUtils.capitalizeFully(String.valueOf(m.get(value)))))
                            forValuesOfConstraints.add(WordUtils.capitalizeFully(String.valueOf(m.get(value))));
                    }
                    for (String s:node
                    ) {
                        tempResult.add(s);
                    }
                }
                else{
                    matchedConstraints=dataRepository.matchConstraint(param.toLowerCase());           //for single constraint
                    nodes=dataRepository.getResultNodes(a,matchedConstraints);         //gives matching nodes
                    for (Map m:nodes
                    ) {
                        if(!forValuesOfConstraints.contains(WordUtils.capitalizeFully(String.valueOf(m.get("key")))))         //to remove duplicates
                            forValuesOfConstraints.add(WordUtils.capitalizeFully(String.valueOf(m.get("key"))));
                        tempResult.add(String.valueOf(m.get(value)));
                    }
                }
            }
            else{                                                                //if required is a property
                if(processedQueryModel.getConstraints().size()>1){
                    matchedConstraints=dataRepository.matchConstraints(param.toLowerCase());
                }
                else {
                    matchedConstraints=dataRepository.matchConstraint(param.toLowerCase());
                }
                for (Map m:processedQueryModel.getConstraints()
                ) {
                    if(!forValuesOfConstraints.contains(WordUtils.capitalizeFully(String.valueOf(m.get(value)))))
                        forValuesOfConstraints.add(WordUtils.capitalizeFully(String.valueOf(m.get(value))));
                }
                List<List> props=dataRepository.getResultProps(a,matchedConstraints);
                for(int i=0;i<props.get(0).size();i++){
                    Map tempmap=om.convertValue(props.get(0).get(i),Map.class);
                    tempResult.add(String.valueOf(tempmap.get(a)));
                }
            }
            forValuesOfQueryResults.add(WordUtils.capitalizeFully(a));
        }
        result=new String[tempResult.size()+1];
        result[0]=String.join(",",forValuesOfQueryResults)+" of "+String.join(",",forValuesOfConstraints);
        int i=1;
        for (String s:tempResult
        ) {
            result[i]=WordUtils.capitalizeFully(s);
            i++;
        }
        resultModel.setResult(result);
        resultModel.setQuery(processedQueryModel.getQuery());
        resultModel.setDomain(processedQueryModel.getDomain());
        String json = gson.toJson(resultModel);
        kafkaTemplate.send(topic, json);                  //write result to FinalResult topic
    }
}
