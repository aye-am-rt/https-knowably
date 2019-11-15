package com.stackroute.nlpmicroservice.core;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Properties;

@NoArgsConstructor
@Service
public class Pipeline {
    private  static StanfordCoreNLP stanfordCoreNLP;

    @Bean(name="stanfordCoreNLP")
    public static StanfordCoreNLP getInstance()
    {
        Properties properties = new Properties();
        String propertiesName = "tokenize, ssplit, pos, lemma, ner";
        properties.setProperty("annotators", propertiesName);

        if(stanfordCoreNLP==null)
        { stanfordCoreNLP=new StanfordCoreNLP(properties); }
        return stanfordCoreNLP;
    }

}
