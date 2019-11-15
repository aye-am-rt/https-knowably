package com.stackroute.nlpmicroservice.repository;

import com.stackroute.nlpmicroservice.domain.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie,String>
{
    @Query(value = "{'Movie.movieName': ?0}")
    List<Movie> findBymovieName(String movieName);

}
