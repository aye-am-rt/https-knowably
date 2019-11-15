package com.stackroute.nlpmicroservice.services;

import com.stackroute.nlpmicroservice.domain.Movie;
import com.stackroute.nlpmicroservice.model.MovieDictionary;
import edu.stanford.nlp.util.CoreMap;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@Data
public class MovieAnalyzerService {
    private static final Logger LOGGER= LoggerFactory.getLogger(MovieAnalyzerService.class);

    private MovieDictionary movieDictionary;
    private Movie movie;

    @Autowired
    public MovieAnalyzerService(MovieDictionary movieDictionary, Movie movie) {
        this.movieDictionary = movieDictionary;
        this.movie=movie;
    }

    void extractInfo(String concept,CoreMap sentence, LinkedHashMap<String, java.util.List<String>> mapOfEachWordProps,
                     LinkedHashMap<String, String> entInSentence) {

        movie.setMovieName(concept);

        HashMap<Integer,String> matchedPoints=new HashMap<>();
        ArrayList<Integer> indices=new ArrayList<>();
        movieDictionary.movieInitializer();
        int index;
        for(int i=0;i<movieDictionary.getStarring().size();i++)
        {
            index=sentence.toString().toLowerCase().indexOf(movieDictionary.getStarring().get(i).toLowerCase());
            if(index!=-1)
            {
                matchedPoints.put(index,"actor");
                indices.add(index);
            }
        }
        for(int i=0;i<movieDictionary.getProducer().size();i++)
        {
            index=sentence.toString().toLowerCase().indexOf(movieDictionary.getProducer().get(i).toLowerCase());
            if(index!=-1)
            {
                matchedPoints.put(index,"producer");
                indices.add(index);
            }
        }
        for(int i=0;i<movieDictionary.getDirector().size();i++)
        {
            index=sentence.toString().toLowerCase().indexOf(movieDictionary.getDirector().get(i).toLowerCase());
            if(index!=-1)
            {
                matchedPoints.put(index,"director");
                indices.add(index);
            }
        }
        for(int i=0;i<movieDictionary.getReleasedYear().size();i++)
        {
            index=sentence.toString().toLowerCase().indexOf(movieDictionary.getReleasedYear().get(i).toLowerCase());
            if(index!=-1)
            {
                matchedPoints.put(index,"year");
                indices.add(index);
            }
        }
        for(int i=0;i<movieDictionary.getProductionHouse().size();i++)
        {
            index=sentence.toString().toLowerCase().indexOf(movieDictionary.getProductionHouse().get(i).toLowerCase());
            if(index!=-1)
            {
                matchedPoints.put(index,"productionHouse");
                indices.add(index);
            }
        }
        for(int i=0;i<movieDictionary.getCollections().size();i++)
        {
            index=sentence.toString().toLowerCase().indexOf(movieDictionary.getCollections().get(i).toLowerCase());
            if(index!=-1)
            {
                matchedPoints.put(index,"collection");
                indices.add(index);
            }
        }
        for(int i=0;i<movieDictionary.getCountry().size();i++)
        {
            index=sentence.toString().toLowerCase().indexOf(movieDictionary.getCountry().get(i).toLowerCase());
            if(index!=-1)
            {
                matchedPoints.put(index,"country");
                indices.add(index);
            }
        }
        matchedPoints.put(sentence.toString().length(),"EOS");
        indices.add(sentence.toString().length());
        LinkedHashSet<Integer> hashSet1 = new LinkedHashSet<>(indices);
        indices = new ArrayList<>(hashSet1);
        Collections.sort(indices);

        for(int i=0;i<indices.size()-1;i++)
        {
            if(matchedPoints.get(indices.get(i)).equalsIgnoreCase("actor")) {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                List<String> l=new ArrayList<>(entInSentence.keySet());
                for (String s : l) {
                    if (entInSentence.get(s).equalsIgnoreCase("person") && subStr.contains(s)) {
                        movie.getCasts().add(s);
                    }
                }
            }

            if(matchedPoints.get(indices.get(i)).equalsIgnoreCase("producer")) {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                List<String> l=new ArrayList<>(entInSentence.keySet());
                for (String s : l) {
                    if (entInSentence.get(s).equalsIgnoreCase("person") && subStr.contains(s)) {
                        movie.getProducers().add(s);
                    }
                }
            }

            if(matchedPoints.get(indices.get(i)).equalsIgnoreCase("director")) {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                List<String> l=new ArrayList<>(entInSentence.keySet());
                for (String s : l) {
                    if (entInSentence.get(s).equalsIgnoreCase("person") && subStr.contains(s)) {
                        movie.getDirectors().add(s);
                    }
                }
            }
            if(matchedPoints.get(indices.get(i)).equalsIgnoreCase("year")) {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                List<String> l=new ArrayList<>(entInSentence.keySet());
                for (String s : l) {
                    if (entInSentence.get(s).equalsIgnoreCase("DATE") && subStr.contains(s)) {
                        movie.getReleaseDate().add(s);
                    }
                }

            }
            if(matchedPoints.get(indices.get(i)).equalsIgnoreCase("productionHouse")) {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                List<String> l=new ArrayList<>(entInSentence.keySet());
                for (String s : l) {
                    if (entInSentence.get(s).equalsIgnoreCase("ORGANIZATION") && subStr.contains(s)) {
                        movie.getProductionHouse().add(s);
                    }
                }
            }
            if(matchedPoints.get(indices.get(i)).equalsIgnoreCase("collection")) {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                List<String> l=new ArrayList<>(entInSentence.keySet());
                for (String s : l) {
                    if (entInSentence.get(s).equalsIgnoreCase("money") && subStr.contains(s)) {
                        movie.getCollection().add(s); }
                }
            }
            if(matchedPoints.get(indices.get(i)).equalsIgnoreCase("country")) {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                List<String> l=new ArrayList<>(entInSentence.keySet());
                for (String s : l) {
                    if (entInSentence.get(s).equalsIgnoreCase("country") && subStr.contains(s)) {
                        movie.getCountry().add(s);
                    }
                }
            }
        }
        matchedPoints.clear();
        indices.clear();
    }

}
