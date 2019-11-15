package com.stackroute.nlpmicroservice.services;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParagraphLemmaService {
    private static final Logger LOGGER= LoggerFactory.getLogger(ParagraphLemmaService.class);


    @Autowired
    private
    StanfordCoreNLP stanfordCoreNLP;

    public ParagraphLemmaService(StanfordCoreNLP stanfordCoreNLP) {
        this.stanfordCoreNLP = stanfordCoreNLP;
    }

    public ArrayList<String> getLemmieWords(String p) {
        Annotation annotations = new Annotation(p);
        stanfordCoreNLP.annotate(annotations);

        ArrayList<String> lemmaWords = new ArrayList<>();
        List<CoreMap> sentenceList = annotations.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentenceList) {
            for (CoreLabel word : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                lemmaWords.add(word.lemma());
            }
        }
        return lemmaWords;
    }
    public String getParagraphWithLemmieWords(String p)
    {
        p = p.trim();
        p=p.replaceAll("\\[.*?\\]", "");
        p=p.replaceAll("\\(.*?\\)", "");
        ArrayList<String> lemmieWords = getLemmieWords(p);
        StringBuilder paragraphWithLemmatizedWords = new StringBuilder();
        for (String lemmieWord : lemmieWords) {
            paragraphWithLemmatizedWords.append(lemmieWord).append(" ");
        }
        return paragraphWithLemmatizedWords.toString().trim();
    }
}
