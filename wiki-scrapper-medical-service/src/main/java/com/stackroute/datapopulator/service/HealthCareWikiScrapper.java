package com.stackroute.datapopulator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.datapopulator.domain.Wikiurl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "api/v1")
@PropertySource("classpath:application.properties")
public class HealthCareWikiScrapper {
    @Value("${topic}")
    private String topic;
    @Value("${uuid}")
    private String uuid;
    @Value("${type}")
    private String type;
    @Value("${name}")
    private String name;
    @Value("${properties}")
    private String properties;
    @Value("${destNode}")
    private String destNode;
    @Value("${sourceNode}")
    private String sourceNode;
    @Value("${relation}")
    private String relation;

    KafkaTemplate<String,String> kafkaTemplate;
    private static final Logger LOGGER= LoggerFactory.getLogger(HealthCareWikiScrapper.class);
    @Autowired
    public HealthCareWikiScrapper(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /*
            function to listen on topic "Wikipedia" to consume wikipedia links
            and crawl the wikipedia page of Medical domain and transform
            data into structured data
        */
    @KafkaListener(topics = "Wikipedia",groupId = "wiki_scrapper",containerFactory = "kafkaListenerContainerFactory")
    public void scrappingWikiData(String receivedInput) throws ParseException {
        /*
            Converting String to JSON object
        */
        String pattern = "\\[.*?\\]";
        String pattern2 = "\\(.*?\\)";
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(receivedInput);
        for (int j = 0; j <jsonArray.size(); j++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(j);
            Wikiurl wikiurl = new Wikiurl();
            JSONArray urlArray = (JSONArray) jsonObject.get("url");
            wikiurl.setUrl((String)urlArray.get(0).toString());
            wikiurl.setConcept((String)jsonObject.get("concept"));
            wikiurl.setUserId((String)jsonObject.get("userId"));
            wikiurl.setDomain((String)jsonObject.get("domain"));
            wikiurl.setId((String)jsonObject.get("id"));
            wikiurl.setQuery((String)jsonObject.get("query"));

         /*
            Getting url from the input object from google-search-service
         */
            String url=wikiurl.getUrl();

            /*
             * Initializing nodes and realtionship array to store all nodes and their relations
             * */
            JSONArray nodesArray = new JSONArray();
            JSONArray relationArray = new JSONArray();
            String id="";
            try {
                /*
                 * fetching web document from the url
                 */
                Document source = Jsoup.connect(url).get();

                /*
                 * Extracting the table that contains the data of use from wikipedia page
                 * */
                Elements table = source.select("table.infobox");
                Elements rows = table.select("tr");
                Elements table1 = source.select("table.infobox tr th");

            /*
                Scrapping the disease name from the wikipedia
                    and other names for that disease if present
            */
                for (Element el : table1.subList(0, Math.min(1, table1.size()))) {
                    String title = el.text();
                    String[] titleArray = title.split("/");
                    for(int i=0;i<titleArray.length;i++){
                        id = UUID.randomUUID().toString();  //generating random IDs

                        /*
                         * Creating node for the name of Disease
                         * */
                        JSONObject nodeTitle = new JSONObject();
                        nodeTitle.put(this.uuid,id);
                        nodeTitle.put(this.type,"Disease");
                        JSONObject jo = new JSONObject();
                        jo.put(this.name, titleArray[i].trim());
                        nodeTitle.put(this.properties,jo);
                        nodesArray.add(nodeTitle);

                    }
                }

                /*
                 * Traversing the rows of the tables
                 * */
                for (Element row : rows) {
                    Elements data1 = row.select("th");
                    Elements data2 = row.select("td");
                    String column1 = data1.text();

                 /*
                    Scrapping the Symptoms of Disease from the wikipedia
                        and other names for that disease if present
                */
                    if (column1.matches("Symptoms")) {
                        String column2 = data2.text();
                        String[] str = column2.replaceAll(pattern, "").replaceAll(pattern2, "").replaceAll("[a-zA-Z]+[:]", ",").split(",");
                        for (int i = 0; i < str.length; i++) {
                            if (!str[i].isEmpty()) {
                                /*
                                 * Creating node for each Symptom extracted from the page
                                 * */
                                String id1 = UUID.randomUUID().toString();
                                JSONObject node = new JSONObject();
                                node.put(this.uuid,id1);
                                node.put(this.type, "Symptom");

                                JSONObject jo = new JSONObject();
                                jo.put(this.name, str[i].trim());
                                node.put(this.properties,jo);

                                /*
                                 * Establishing a relationship between symptom node and the disease title node
                                 * */
                                JSONObject jo1 = new JSONObject();
                                jo1.put(this.uuid, id1);
                                jo1.put(this.sourceNode,id);
                                jo1.put(this.destNode, id1);
                                jo1.put(this.relation, "has");

                                nodesArray.add(node);

                                relationArray.add(jo1);
                            }
                        }
                    }

                 /*
                    Scrapping the medication for Disease from the wikipedia
                        and other names for that disease if present
                */
                    if (column1.matches("Medication")) {
                        String column2 = data2.text();
                        String[] str = column2.replaceAll(pattern, "").replaceAll(pattern2, "").replaceAll("[a-zA-Z]+[:]", ",").split(",");
                        for (int i = 0; i < str.length; i++) {
                            if (!str[i].isEmpty()) {
                                /*
                                 * Creating node for each Medication extracted from the page
                                 * */
                                String id1 = UUID.randomUUID().toString();
                                JSONObject node = new JSONObject();
                                node.put(this.uuid,id1);
                                node.put(this.type, "Medication");

                                JSONObject jo = new JSONObject();
                                jo.put(this.name, str[i].trim());
                                node.put(this.properties,jo);
                                nodesArray.add(node);

                                /*
                                 * Establishing a relationship between Medication node and the disease title node
                                 * */
                                JSONObject jo1 = new JSONObject();
                                jo1.put(this.uuid, id1);
                                jo1.put(this.sourceNode,id);
                                jo1.put(this.destNode, id1);
                                jo1.put(this.relation, "required");

                                relationArray.add(jo1);
                            }
                        }
                    }

                 /*
                    Scrapping the complications due to disease from the wikipedia
                        and other names for that disease if present
                */
                    if (column1.matches("Complications")) {
                        String column2 = data2.text();
                        String[] str = column2.replaceAll(pattern, "").replaceAll(pattern2, "").replaceAll("[a-zA-Z]+[:]", ",").split(",");
                        for (int i = 0; i < str.length; i++) {
                            if (!str[i].isEmpty()) {
                                /*
                                 * Creating node for each Complication extracted from the page
                                 * */
                                String id1 = UUID.randomUUID().toString();
                                JSONObject node = new JSONObject();
                                node.put(this.uuid,id1);
                                node.put(this.type, "Complication");

                                JSONObject jo = new JSONObject();
                                jo.put(this.name, str[i].trim());
                                node.put(this.properties,jo);
                                nodesArray.add(node);

                                /*
                                 * Establishing a relationship between Complication node and the disease title node
                                 * */
                                JSONObject jo1 = new JSONObject();
                                jo1.put(this.uuid, id1);
                                jo1.put(this.sourceNode,id);
                                jo1.put(this.destNode, id1);
                                jo1.put(this.relation, "have");
                                relationArray.add(jo1);
                            }
                        }
                    }

                /*
                    Scrapping the Death count due to disease from the wikipedia
                        and other names for that disease if present
                */
                    if (column1.matches("Deaths")) {
                        String column2 = data2.text().replaceAll(pattern, "").replaceAll(pattern2, "").replaceAll("[a-zA-Z]+[:]", ",");
                        if (!column2.isEmpty()) {
                            /*
                             * Creating node for Death extracted from the page
                             * */
                            String id1 = UUID.randomUUID().toString();
                            JSONObject node = new JSONObject();
                            node.put(this.uuid,id1);
                            node.put(this.type, "Death");

                            JSONObject jo = new JSONObject();
                            jo.put("count", column2.trim());
                            node.put(this.properties,jo);
                            nodesArray.add(node);

                            /*
                             * Establishing a relationship between Death node and the disease title node
                             * */
                            JSONObject jo1 = new JSONObject();
                            jo1.put(this.uuid, id1);
                            jo1.put(this.sourceNode,id);
                            jo1.put(this.destNode, id1);
                            jo1.put(this.relation, "results in");
                            relationArray.add(jo1);
                        }
                    }

                /*
                    Scrapping the causes of disease from the wikipedia
                        and other names for that disease if present
                */
                    if (column1.matches("Causes")) {
                        String column2 = data2.text();
                        String[] str = column2.replaceAll(pattern, "").replaceAll(pattern2, "").replaceAll("[a-zA-Z]+[:]", ",").split(",");
                        for (int i = 0; i < str.length; i++) {
                            if (!str[i].isEmpty()) {
                                /*
                                 * Creating node for each cause extracted from the page
                                 * */
                                String id1 = UUID.randomUUID().toString();
                                JSONObject node = new JSONObject();
                                node.put(this.uuid,id1);
                                node.put(this.type, "Cause");

                                JSONObject jo = new JSONObject();
                                jo.put(this.name, str[i].trim());
                                node.put(this.properties,jo);
                                nodesArray.add(node);

                                /*
                                 * Establishing a relationship between Cause node and the disease title node
                                 * */
                                JSONObject jo1 = new JSONObject();
                                jo1.put(this.uuid, id1);
                                jo1.put(this.sourceNode,id);
                                jo1.put(this.destNode, id1);
                                jo1.put(this.relation, "caused by");
                                relationArray.add(jo1);
                            }
                        }
                    }
                /*
                    Scrapping the frequency of disease from the wikipedia
                        and other names for that disease if present
                */
                    if (column1.matches("Frequency")) {
                        String column2 = data2.text();
                        String[] str = column2.replaceAll(pattern, "").replaceAll(pattern2, "").replaceAll("[a-zA-Z]+[:]", ",").split(",");
                        for (int i = 0; i < str.length; i++) {
                            if (!str[i].isEmpty()) {
                                /*
                                 * Creating node for Frequency extracted from the page
                                 * */
                                String id1 = UUID.randomUUID().toString();
                                JSONObject node = new JSONObject();
                                node.put(this.uuid,id1);
                                node.put(this.type,"Frequency");
                                JSONObject jo = new JSONObject();
                                jo.put(this.name, str[i].trim());
                                node.put(this.properties,jo);
                                nodesArray.add(node);

                                /*
                                 * Establishing a relationship between Frequency node and the disease title node
                                 * */
                                JSONObject jo1 = new JSONObject();
                                jo1.put(this.uuid, id1);
                                jo1.put(this.sourceNode,id);
                                jo1.put(this.destNode, id1);
                                jo1.put(this.relation, "occurs");
                                relationArray.add(jo1);
                            }
                        }
                    }

                /*
                    Scrapping the treatment for disease from the wikipedia
                        and other names for that disease if present
                */
                    if (column1.matches("Treatment")) {
                        String column2 = data2.text();
                        String[] str = column2.replaceAll(pattern, "").replaceAll(pattern2, "").replaceAll("[a-zA-Z]+[:]", ",").split(",");
                        for (int i = 0; i < str.length; i++) {
                            if (!str[i].isEmpty()) {

                                /*
                                 * Creating node for Treatment extracted from the page
                                 * */
                                String id1 = UUID.randomUUID().toString();
                                JSONObject node = new JSONObject();
                                node.put(this.uuid,id1);
                                node.put(this.type, "Treatment");
                                JSONObject jo = new JSONObject();
                                jo.put(this.name, str[i].trim());
                                node.put(this.properties,jo);
                                nodesArray.add(node);

                                /*
                                 * Establishing a relationship between treatment node and the disease title node
                                 * */
                                JSONObject jo1 = new JSONObject();
                                jo1.put(this.uuid, id1);
                                jo1.put(this.sourceNode,id);
                                jo1.put(this.destNode, id1);
                                jo1.put(this.relation, "required treatment");
                                relationArray.add(jo1);
                            }
                        }
                    }
                /*
                    Scrapping the Risk factors of disease from the wikipedia
                        and other names for that disease if present
                */
                    if (column1.matches("Risk factors")) {
                        String column2 = data2.text();
                        String[] str = column2.replaceAll(pattern, "").replaceAll(pattern2, "").replaceAll("[a-zA-Z]+[:]", ",").split(",");
                        for (int i = 0; i < str.length; i++) {
                            if (!str[i].isEmpty()) {

                                /*
                                 * Creating node for Risk Factors extracted from the page
                                 * */
                                String id1 = UUID.randomUUID().toString();
                                JSONObject node = new JSONObject();
                                node.put(this.uuid,id1);
                                node.put(this.type, "Risk factor");
                                JSONObject jo = new JSONObject();
                                jo.put(this.name, str[i].trim());
                                node.put(this.properties,jo);
                                nodesArray.add(node);

                                /*
                                 * Establishing a relationship between Risk factors node and the disease title node
                                 * */
                                JSONObject jo1 = new JSONObject();
                                jo1.put(this.uuid, id1);
                                jo1.put(this.sourceNode,id);
                                jo1.put(this.destNode, id1);
                                jo1.put(this.relation, "has risk");
                                relationArray.add(jo1);
                            }
                        }
                    }
                }

            /*
                Creating a jsonObject contains nodesArray
                    and relationshipArray of relations among the nodes
            */
                JSONObject jsonDTO = new JSONObject();
                jsonDTO.put("nodes",nodesArray);
                jsonDTO.put("relationship",relationArray);
                jsonDTO.put("query", wikiurl.getQuery());
                jsonDTO.put("userId",wikiurl.getUserId());

                publishData(jsonDTO);


            } catch (IOException e) {
               LOGGER.error(e.getMessage());
            }
        }
    }

    public void publishData(JSONObject jsonDTO){
        /* producing the jsonDTO in kafka on topic WikiScrapper*/
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            String resultPayload= objectMapper.writeValueAsString(jsonDTO);
            kafkaTemplate.send(topic,resultPayload);


        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
