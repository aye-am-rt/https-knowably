package com.stackroute.service;

import com.google.gson.Gson;
import com.stackroute.domain.Data;
import com.stackroute.domain.DataModel;
import com.stackroute.repository.DataRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class RedisService {
    @Autowired
    DataRepositoryImpl dataRepository;

    public void populate(String param) {
        Gson gson = new Gson();
        Data dataModel = gson.fromJson(param, Data.class);
        for (DataModel dm:dataModel.getInfo()
             ) {
            dataRepository.save(dm);
        }
    }
    public Map<String,DataModel> getAll(){
       return dataRepository.findAll();
    }
}
