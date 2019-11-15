package com.stackroute.datapopulator.googlesearchapiservicedemo.repository;

import com.stackroute.datapopulator.googlesearchapiservicedemo.domain.Cache;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CacheRepository extends MongoRepository<Cache, Integer> {
    @Query("{'concepts' : ?0 }")
    public List<Cache> findByConcepts(@Param("chars") String[] chars);
}
