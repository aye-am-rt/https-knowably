package com.stackroute.nlpmicroservice.services;

import com.stackroute.nlpmicroservice.domain.SitesContent;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class NLPResultsService {
    private static final Logger LOGGER= LoggerFactory.getLogger(NLPResultsService.class);

    private MovieAnalyzerService movieAnalyzerService;
    private MedicalAnalyzerService medicalAnalyzerService;
    private SitesContent sitesContent;
    private StanfordCoreNLP stanfordCoreNLP;
    private List<String> removeTags=new ArrayList<>(
            Arrays.asList("DT",".",",","PRP","WP","CC","POS","WDT","WRB","MD","LS","SYM"));



    public NLPResultsService(MovieAnalyzerService movieAnalyzerService, SitesContent sitesContent,
                             MedicalAnalyzerService medicalAnalyzerService, StanfordCoreNLP stanfordCoreNLP) {
        this.movieAnalyzerService = movieAnalyzerService;
        this.sitesContent=sitesContent;
        this.medicalAnalyzerService=medicalAnalyzerService;
        this.stanfordCoreNLP = stanfordCoreNLP;
    }


    public void StepwiseNlp(String OnePara)
    {
        Annotation annotations = new Annotation(OnePara);
        stanfordCoreNLP.annotate(annotations);

        List<CoreMap> sentences = annotations.get(CoreAnnotations.SentencesAnnotation.class);
        LinkedHashMap<String, List<String>> mapOfEachWordProps = new LinkedHashMap<>();
        LinkedHashMap<String,String> entInSentence=new LinkedHashMap<>();


        for(CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                mapOfEachWordProps.putIfAbsent(word,new ArrayList<>(Arrays.asList(pos,ne)));
            }
            for (CoreMap entityMention : sentence.get(CoreAnnotations.MentionsAnnotation.class)) {
               String ner=entityMention.get(CoreAnnotations.NamedEntityTagAnnotation.class);
               String w=entityMention.get(CoreAnnotations.TextAnnotation.class);
               entInSentence.putIfAbsent(w,ner);
            }
            String domain=sitesContent.getDomain();
            String concept=sitesContent.getConcept();

            List<String> le=new ArrayList<>(entInSentence.keySet());
            for(String lee:le)
            {
                if(mapOfEachWordProps.containsKey(lee)&&removeTags.contains(mapOfEachWordProps.get(lee).get(0)))
                {  mapOfEachWordProps.remove(lee);entInSentence.remove(lee); }
            }
            List<String> lm=new ArrayList<>(mapOfEachWordProps.keySet());
            for(String lmm:lm)
            { if(removeTags.contains(mapOfEachWordProps.get(lmm).get(0)))
                { mapOfEachWordProps.remove(lmm); } }

            if(domain.equalsIgnoreCase("movie"))
                movieAnalyzerService.extractInfo(concept, sentence, mapOfEachWordProps, entInSentence);
            else if(domain.equalsIgnoreCase("medical"))
                medicalAnalyzerService.extractInfo(concept,sentence,mapOfEachWordProps,entInSentence);
            else
                System.out.println("no analysis done in nlp result service");

            mapOfEachWordProps.clear();
            entInSentence.clear();
            le.clear();
            lm.clear();
        }
    }
}
