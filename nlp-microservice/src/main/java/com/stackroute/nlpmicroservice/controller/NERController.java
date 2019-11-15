package com.stackroute.nlpmicroservice.controller;

import com.stackroute.nlpmicroservice.domain.Medical;
import com.stackroute.nlpmicroservice.domain.Movie;
import com.stackroute.nlpmicroservice.domain.SitesContent;
import com.stackroute.nlpmicroservice.listeners.ConsumerListener;
import com.stackroute.nlpmicroservice.model.TypeEnum;
import com.stackroute.nlpmicroservice.services.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1")
public class NERController {
    private static final Logger LOGGER= LoggerFactory.getLogger(NERController.class);

    private HtmlService htmlService;
    private ParagraphLemmaService paragraphLemmaService;
    private NLPResultsService nlpResultsService;
    private ConfidenceScoreService confidenceScoreService;
    private Movie movie;
    private Medical medical;
    private SitesContent sitesContent;
    private MovieAnalyzerService movieAnalyzerService;
    private MovieDbService movieDbService;
    private MedicalDbService medicalDbService;
   private MedicalAnalyzerService medicalAnalyzerService;

    @Autowired
    public NERController(HtmlService htmlService, ParagraphLemmaService paragraphLemmaService,
                         NLPResultsService nlpResultsService, SitesContent sitesContent,Movie movie,Medical medical,
                         ConfidenceScoreService confidenceScoreService, MovieAnalyzerService movieAnalyzerService,
                         MedicalAnalyzerService medicalAnalyzerService,MovieDbService movieDbService,
                         MedicalDbService medicalDbService)
    { this.htmlService=htmlService;this.paragraphLemmaService = paragraphLemmaService;
    this.nlpResultsService=nlpResultsService;this.sitesContent=sitesContent;this.movie=movie;this.medical=medical;
    this.confidenceScoreService=confidenceScoreService;this.movieAnalyzerService=movieAnalyzerService;
    this.medicalAnalyzerService=medicalAnalyzerService;this.movieDbService= movieDbService;
    this.medicalDbService= medicalDbService; }



    @PostMapping(value = "nlp")
    public ResponseEntity<?> nlp(@RequestBody String html)
    {
        ResponseEntity responseEntity;
        try
        {
            List<String> paraList=htmlService.getAllParagraphs(html);
            if(!paraList.isEmpty())
            {
                for(int i=0;i<10&&i<paraList.size();i++)
                {
                    String cleanHtml = htmlService.html2text(paraList.get(i));
                    String lemPara = paragraphLemmaService.getParagraphWithLemmieWords(cleanHtml);
                    nlpResultsService.StepwiseNlp(lemPara);
                }
            }
            if(sitesContent.getDomain().equalsIgnoreCase("movie"))
            {
                LOGGER.info("added in movie");
                movieAnalyzerService.getMovie().setId(UUID.randomUUID().toString());
                movieAnalyzerService.getMovie().setMovieName(sitesContent.getConcept());
                Movie mv=movieDbService.saveMovie(movieAnalyzerService.getMovie());
                LOGGER.info(mv.toString());
                movie.setMovieName(null);movie.setCasts(new ArrayList<>());movie.setCollection(new ArrayList<>());
                movie.setCountry(new ArrayList<>());movie.setDirectors(new ArrayList<>());movie.setProducers(new ArrayList<>());
                movie.setProductionHouse(new ArrayList<>());movie.setReleaseDate(new ArrayList<>());

            }
            else if (sitesContent.getDomain().equalsIgnoreCase("medical"))
            {
                LOGGER.info("added in medical");
                medicalAnalyzerService.getMedical().setId(UUID.randomUUID().toString());
                medicalAnalyzerService.getMedical().setDisease(sitesContent.getConcept());
                Medical mdd=medicalDbService.saveMedical(medicalAnalyzerService.getMedical());
                LOGGER.info(mdd.toString());
                medical.setDisease(null);medical.setCauses(new ArrayList<>());medical.setDeathCount(new ArrayList<>());
                medical.setMedicine(new ArrayList<>());medical.setSymptoms(new ArrayList<>());medical.setTreatment(new ArrayList<>());
            }
            else
            {
                LOGGER.info("did not added anything in collect domain list");
            }
            responseEntity = new ResponseEntity<>(htmlService.html2text(html), HttpStatus.OK);
        } catch (Exception e)
        {
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return responseEntity;
    }
    @GetMapping(value = "checkConfidence")
    public ResponseEntity<?> confidence() {
        ResponseEntity responseEntity;
        try
        {
            confidenceScoreService.processListOfDomains(sitesContent.getDomain(),sitesContent.getConcept());
            movieDbService.deleteMoviesData();
            medicalDbService.deleteMedicalsData();
            responseEntity = new ResponseEntity<>("hello confidence", HttpStatus.OK);
        }catch (Exception e)
        {
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return responseEntity;
    }
}
