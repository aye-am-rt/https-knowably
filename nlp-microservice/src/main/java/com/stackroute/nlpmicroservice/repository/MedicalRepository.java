package com.stackroute.nlpmicroservice.repository;

import com.stackroute.nlpmicroservice.domain.Medical;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRepository  extends MongoRepository<Medical,String> {
    @Query(value = "{'Medical.Disease': ?0}")
    List<Medical> findByDisease(String disease);
}
