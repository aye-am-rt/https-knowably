package com.stackroute.nlpmicroservice.listeners;

import com.google.gson.Gson;
import com.stackroute.nlpmicroservice.domain.Medical;
import com.stackroute.nlpmicroservice.domain.Movie;
import com.stackroute.nlpmicroservice.domain.SitesContent;
import com.stackroute.nlpmicroservice.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConsumerListener {
    private static final Logger LOGGER= LoggerFactory.getLogger(ConsumerListener.class);

    private HtmlService htmlService;
    private ParagraphLemmaService paragraphLemmaService;
    private MovieAnalyzerService movieAnalyzerService;
    private MedicalAnalyzerService medicalAnalyzerService;
    private Movie movie;
    private Medical medical;
    private MovieDbService movieDbService;
    private MedicalDbService medicalDbService;
    private NLPResultsService nlpResultsService;
    private SitesContent sitesContent;
    private ConfidenceScoreService confidenceScoreService;
    private String[] arrayOfWc;

    @Autowired
    public ConsumerListener(HtmlService htmlService, ParagraphLemmaService paragraphLemmaService,
                            NLPResultsService nlpResultsService, SitesContent sitesContent,
                            Movie movie, Medical medical, MovieAnalyzerService movieAnalyzerService,
                            MedicalAnalyzerService medicalAnalyzerService, MovieDbService movieDbService,
                            MedicalDbService medicalDbService,ConfidenceScoreService confidenceScoreService)
    { this.htmlService=htmlService;this.paragraphLemmaService = paragraphLemmaService;
        this.nlpResultsService=nlpResultsService;this.sitesContent=sitesContent;
        this.confidenceScoreService=confidenceScoreService;this.movie=movie;this.medical=medical;
        this.movieAnalyzerService=movieAnalyzerService;this.medicalAnalyzerService=medicalAnalyzerService;
        this.movieDbService= movieDbService;this.medicalDbService= medicalDbService; }


    //This method is used to consume json object from producer
    @KafkaListener(topics = "TopicPayload", groupId = "group_id")
    public void consume(String listOfWebCrawlers) throws Exception {
        try {
            listOfWebCrawlers = listOfWebCrawlers.substring(1, listOfWebCrawlers.length() - 1);
            StringBuilder sb = new StringBuilder(listOfWebCrawlers.trim());
            arrayOfWc = sb.toString().split(",");
        }catch (Exception ex)
        {
            LOGGER.info(Arrays.toString(ex.getStackTrace()),sitesContent.getDomain());
        }

        for(int i=0;i<arrayOfWc.length;i++)
        {
            Gson gson = new Gson();
            SitesContent sitesContent = gson.fromJson(arrayOfWc[i], SitesContent.class);
            List<String> ithPayload = sitesContent.getPayload();
            if (ithPayload.isEmpty())
                LOGGER.info("no payload found");
            ListIterator<String> iterator = ithPayload.listIterator();
            while (iterator.hasNext()) {
                List<String> paraList = htmlService.getAllParagraphs(iterator.next());
                if (!paraList.isEmpty()) {
                    for (String p : paraList) {
                        String cleanHtml = htmlService.html2text(p);
                        String lemPara = paragraphLemmaService.getParagraphWithLemmieWords(cleanHtml);
                        nlpResultsService.StepwiseNlp(lemPara);
                    }
                }
                try {
                    if (sitesContent.getDomain().equalsIgnoreCase("movie")) {
                        LOGGER.info("added in movie");
                        movieAnalyzerService.getMovie().setId(UUID.randomUUID().toString());
                        movieAnalyzerService.getMovie().setMovieName(sitesContent.getConcept());
                        Movie mv = movieDbService.saveMovie(movieAnalyzerService.getMovie());
                        LOGGER.info(mv.toString());
                        movie.setMovieName(null);
                        movie.setCasts(new ArrayList<>());
                        movie.setCollection(new ArrayList<>());
                        movie.setCountry(new ArrayList<>());
                        movie.setDirectors(new ArrayList<>());
                        movie.setProducers(new ArrayList<>());
                        movie.setProductionHouse(new ArrayList<>());
                        movie.setReleaseDate(new ArrayList<>());

                    } else if (sitesContent.getDomain().equalsIgnoreCase("medical")) {
                        LOGGER.info("added in medical");
                        medicalAnalyzerService.getMedical().setId(UUID.randomUUID().toString());
                        medicalAnalyzerService.getMedical().setDisease(sitesContent.getConcept());
                        Medical mdd = medicalDbService.saveMedical(medicalAnalyzerService.getMedical());
                        LOGGER.info(mdd.toString());
                        medical.setDisease(null);
                        medical.setCauses(new ArrayList<>());
                        medical.setDeathCount(new ArrayList<>());
                        medical.setMedicine(new ArrayList<>());
                        medical.setSymptoms(new ArrayList<>());
                        medical.setTreatment(new ArrayList<>());
                    } else {
                        LOGGER.info("did not added anything MongoDb ");
                    }
                }catch (Exception ex)
                {
                    LOGGER.info(Arrays.toString(ex.getStackTrace()),sitesContent.getDomain());
                }
            }
        }
        try
        {
            confidenceScoreService.processListOfDomains(sitesContent.getDomain(),sitesContent.getConcept());
            movieDbService.deleteMoviesData();
            medicalDbService.deleteMedicalsData();
        }
        catch (Exception e)
        { LOGGER.info(Arrays.toString(e.getStackTrace()),sitesContent.getDomain()); }
    }
}