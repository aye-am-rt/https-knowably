package com.stackroute.repository;

import com.stackroute.domain.DataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
public class DataRepositoryImpl {

    private RedisTemplate<String, DataModel> redisTemplate;

    private HashOperations hashOperations;


    @Autowired
    public DataRepositoryImpl(RedisTemplate<String, DataModel> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }


    public void save(DataModel dataModel) {
        hashOperations.put("DataModel", dataModel.getKey(), dataModel);
    }


    public Map<String, DataModel> findAll() {
        return hashOperations.entries("DataModel");
    }

}
