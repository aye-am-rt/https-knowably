package com.stackroute.datapopulator.service;
import com.google.gson.Gson;
import com.stackroute.datapopulator.domain.ConvertedDataModel;
import com.stackroute.datapopulator.domain.ConvertedNode;
import com.stackroute.datapopulator.domain.DataModel;
import com.stackroute.datapopulator.domain.Node;
import com.stackroute.datapopulator.repository.DataPopulatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataPopulatorService {
    @Autowired
    private DataPopulatorRepository dataRepository;
    private static final Logger logger = LoggerFactory.getLogger(DataPopulatorService.class);
    public String dataPopulator(String line){                               //method to store data in database
        Gson gson = new Gson();
        DataModel datamodel = gson.fromJson(line, DataModel.class);
        ConvertedDataModel convertedDataModel=new ConvertedDataModel();     // convert to required format
        List<ConvertedNode> result=new ArrayList<>();

        for (Node n: datamodel.getNodes()
        ) {
            ConvertedNode convertedNode=new ConvertedNode();
            convertedNode.setUuid(n.getUuid());
            convertedNode.setProperties(n.getProperties());
            List<String> temp=new ArrayList<>();
            temp.add(n.getType());
            convertedNode.setType(temp);
            result.add(convertedNode);
        }

        convertedDataModel.setNodes(result);
        convertedDataModel.setRelationship(datamodel.getRelationship());
        convertedDataModel.setQuery(datamodel.getQuery());
        convertedDataModel.setUserId(datamodel.getUserId());
        String json=gson.toJson(convertedDataModel);
        dataRepository.createNodes(json);
        dataRepository.createRelations(json);
        logger.info(datamodel.getUserId());
        logger.info(datamodel.getQuery());
        if(datamodel.getUserId()=="internal"&& datamodel.getQuery()!=null){        //for internal requests when result not found in the database
            return datamodel.getQuery();
        }
        else {
            return "null";
        }

    }

    public boolean dataMerger(){                //method to merge duplicate nodes
        dataRepository.mergeNodes();
        dataRepository.mergeRelations();
        return true;
    }

}
