package com.stackroute.nlpmicroservice.services;

import com.stackroute.nlpmicroservice.domain.Movie;
import com.stackroute.nlpmicroservice.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieDbService {
    private static final Logger LOGGER= LoggerFactory.getLogger(MovieDbService.class);


    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    public MovieDbService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie saveMovie(Movie movieObj) throws Exception {
        if(movieRepository.existsById(movieObj.getId()))
        {
            throw new Exception ("Movie id already exists");
        }
        return movieRepository.save(movieObj);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
    public void deleteMoviesData() {
        movieRepository.deleteAll();
    }


    public Movie getMovieById(String id) throws Exception {
        List<Movie> allMovieList=movieRepository.findAll();
        Movie findOneMovie=new Movie();
        if(movieRepository.existsById(id))
        {
            for (Movie movie : allMovieList) {
                if (movie.getId().equals(id)) {
                    findOneMovie = movie;
                }
            }
        }
        else
        {
            throw new Exception("movie does not exists with given id= "+id);
        }
        return findOneMovie;
    }


}
