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
public class MovieWikiScrapper {
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
    @Value("${pattern}")
    private String pattern;
    @Value("${pattern2}")
    private String pattern2;
    @Value("${language}")
    private String language;
    @Value("${tag}")
    private String tag;
    @Value("${country}")
    private String country;

    KafkaTemplate<String,String> kafkaTemplate;
    private static final Logger LOGGER= LoggerFactory.getLogger(MovieWikiScrapper.class);
    @Autowired
    public MovieWikiScrapper(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /*
            function to listen on topic "Wikipedia" to consume wikipedia links
            and crawl the wikipedia page of Medical domain and transform
            data into structured data
        */
    @KafkaListener(topics = "MovieWikipedia",groupId = "wiki_scrapper",containerFactory = "kafkaListenerContainerFactory")
    public void collectWebTableData1(String receivedInput) throws ParseException {

        /*
            Converting String to JSON object
        */
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(receivedInput);
        for (int j = 0; j < jsonArray.size(); j++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(j);
            Wikiurl wikiurl = new Wikiurl();
            JSONArray urlArray = (JSONArray) jsonObject.get("url");
            wikiurl.setUrl((String) urlArray.get(0).toString());
            wikiurl.setConcept((String) jsonObject.get("concept"));
            wikiurl.setUserId((String) jsonObject.get("userId"));
            wikiurl.setDomain((String) jsonObject.get("domain"));
            wikiurl.setId((String) jsonObject.get("id"));
            wikiurl.setQuery((String) jsonObject.get("query"));

         /*
            Getting url from the input object from google-search-service
         */
            String url = wikiurl.getUrl();

            JSONArray nodesArray = new JSONArray();
            JSONArray relationArray = new JSONArray();
            String id = "";
            try {
                /*
                 * fetching web document from the url
                 */
                String header = "";
                Document source = Jsoup.connect(url).get();

                /*
                 * Extracting the table that contains the data of use from wikipedia page
                 * */
                Elements table = source.select("table.infobox");
                Elements title = table.select("tr th.summary");
                for(Element el : title){
                    id = UUID.randomUUID().toString();
                    JSONObject node = new JSONObject();

                    /*
                     * Creating node for the name of movie
                     * */
                    node.put(this.uuid, id);
                    node.put(this.type, "Movie");
                    JSONObject jo = new JSONObject();
                    jo.put(this.name, el.text().trim());
                    node.put(this.properties, jo);
                    nodesArray.add(node);
                    break;
                }

                /*
                 * Traversing the rows of the tables
                 * */
                Elements rows = table.select("tr");
                for (Element row : rows) {
                    Elements data1 = row.select("th");
                    Elements data2 = row.select("td");
                    if (!data1.select("div").isEmpty()) {
                        Elements headerElement = data1.select("div");
                        header = headerElement.html();
                        header = header.replace("<br>", "\n").replaceAll("\n", "");
                    } else {
                        header = data1.html();
                        header = header.replace("<br>", " ");
                    }

                    //Extracting Cast of the specific movie from Html Document
                    if (header.matches("Starring")) {
                        if (!data2.select("div").isEmpty()) {
                            Elements data3 = data2.select("div ul li a");
                            String str1 = data3.html();
                            String[] str = str1.replaceAll(pattern, "").replaceAll(pattern2, "").replaceAll("[a-zA-Z]+[:]", ",").split("\n");
                            for (int i = 0; i < str.length; i++) {
                                if (!str[i].isEmpty()) {
                                    String id1 = UUID.randomUUID().toString();
                                    JSONObject node = new JSONObject();
                                    /*
                                     * Creating node for each actor extracted from the page
                                     * */
                                    node.put(this.uuid, id1);
                                    node.put(this.type, "Artist");
                                    JSONObject jo = new JSONObject();
                                    jo.put(this.name, str[i].trim());
                                    node.put(this.properties, jo);
                                    nodesArray.add(node);

                                    /*
                                     * Establishing a relationship between actor node and the movie title node
                                     * */
                                    JSONObject jo1 = new JSONObject();
                                    String relationId = UUID.randomUUID().toString();
                                    jo1.put(this.uuid, relationId);
                                    jo1.put(this.destNode,id);
                                    jo1.put(this.sourceNode, id1);
                                    jo1.put(this.relation, "acted by");
                                    relationArray.add(jo1);
                                }
                            }
                        } else {
                            Elements data3 = data2.select("a");
                            String str1 = data3.html();
                            String[] str = str1.replaceAll(pattern, "").replaceAll(pattern2, "").replaceAll("[a-zA-Z]+[:]", ",").split("\n");
                            for (int i = 0; i < str.length; i++) {
                                if (!str[i].isEmpty()) {
                                    /*
                                     * Creating node for each actor extracted from the page
                                     * */
                                    String id1 = UUID.randomUUID().toString();
                                    JSONObject node = new JSONObject();
                                    node.put(this.uuid, id1);
                                    node.put(this.type, "Artist");

                                    JSONObject jo = new JSONObject();
                                    jo.put(this.name, str[i].trim());
                                    node.put(this.properties, jo);
                                    nodesArray.add(node);
                                    /*
                                     * Establishing a relationship between actor node and the movie title node
                                     * */
                                    JSONObject jo1 = new JSONObject();
                                    String relationId = UUID.randomUUID().toString();
                                    jo1.put(this.uuid, relationId);
                                    jo1.put(this.sourceNode,id);
                                    jo1.put(this.destNode, id1);
                                    jo1.put(this.relation, "acted by");
                                    relationArray.add(jo1);
                                }
                            }
                        }
                    }

                    //Extracting Production Company from Html Document
                    if (header.matches("Productioncompany") && !data2.select("div").isEmpty()) {
                            Elements data3 = data2.select("div");
                            if (!data2.select("a").isEmpty()) {
                                Elements data4 = data2.select("a");
                                String str1 = data4.html();
                                str1 = str1.replace("<br>", "\n");
                                String[] str = str1.replaceAll(pattern, "").replaceAll(pattern2, "").replaceAll("[a-zA-Z]+[:]", ",").split("\n");
                                for (int i = 0; i < str.length; i++) {
                                    if (!str[i].isEmpty()) {
                                        /*
                                         * Creating node for each production company extracted from the page
                                         * */
                                        String id1 = UUID.randomUUID().toString();
                                        JSONObject node = new JSONObject();
                                        node.put(this.uuid, id1);
                                        node.put(this.type, "Producer");

                                        JSONObject jo = new JSONObject();
                                        jo.put(this.name, str[i].trim());
                                        node.put(this.properties, jo);
                                        nodesArray.add(node);

                                        /*
                                         * Establishing a relationship between production company node and the movie title node
                                         * */
                                        JSONObject jo1 = new JSONObject();
                                        String relationId = UUID.randomUUID().toString();
                                        jo1.put(this.uuid, relationId);
                                        jo1.put(this.sourceNode,id);
                                        jo1.put(this.destNode, id1);
                                        jo1.put(this.relation, "produced by");
                                        relationArray.add(jo1);
                                    }
                                }
                            }
                            String column2 = data3.html();
                            column2 = column2.replace("<br>", "\n");
                            String[] str = column2.replace(pattern, "").replace(pattern2, "").replace("[a-zA-Z]+[:]", ",").split("\n");
                            for (int i = 0; i < str.length; i++) {
                                if (!str[i].isEmpty() && !str[i].contains(("<a "))) {
                                        /*
                                         * Creating node for each production company extracted from the page
                                         * */
                                        String id1 = UUID.randomUUID().toString();
                                        JSONObject node = new JSONObject();
                                        node.put(this.uuid, id1);
                                        node.put(this.type, "Producer");

                                        JSONObject jo = new JSONObject();
                                        jo.put(this.name, str[i].trim());
                                        node.put(this.properties, jo);
                                        nodesArray.add(node);

                                        /*
                                         * Establishing a relationship between production company node and the movie title node
                                         * */
                                        JSONObject jo1 = new JSONObject();
                                        String relationId = UUID.randomUUID().toString();
                                        jo1.put(this.uuid, relationId);
                                        jo1.put(this.sourceNode,id);
                                        jo1.put(this.destNode, id1);
                                        jo1.put(this.relation, "produced by");
                                        relationArray.add(jo1);

                                }
                            }

                        }


                    //Extracting Directors from Html Document
                    if (header.matches("Directed by")) {
                        Elements data4 = data2.select("a");
                        String str1 = data4.html();
                        String[] str = str1.replace(pattern, "").replace(pattern2, "").replace("[a-zA-Z]+[:]", ",").split("\n");
                        for (int i = 0; i < str.length; i++) {
                            if (!str[i].isEmpty()) {
                                /*
                                 * Creating node for each Director extracted from the page
                                 * */
                                String id1 = UUID.randomUUID().toString();
                                JSONObject node = new JSONObject();
                                node.put(this.uuid, id1);
                                node.put(this.type, "Director");

                                JSONObject jo = new JSONObject();
                                jo.put(this.name, str[i].trim());
                                node.put(this.properties, jo);
                                nodesArray.add(node);

                                /*
                                 * Establishing a relationship between Director node and the movie title node
                                 * */
                                JSONObject jo1 = new JSONObject();
                                String relationId = UUID.randomUUID().toString();
                                jo1.put(this.uuid, relationId);
                                jo1.put(this.sourceNode,id);
                                jo1.put(this.destNode, id1);
                                jo1.put(this.relation, "directed by");
                                relationArray.add(jo1);
                            }
                        }
                    }

                    //Extracting Country from Html Document
                    if (header.matches(country)) {
                        if (!data2.select("div").isEmpty()) {
                            Elements data3 = data2.select(tag);
                            String countryWiki = data3.html();
                            String[] str = countryWiki.replace(pattern, "").replace(pattern2, "").replace("[a-zA-Z]+[:]", ",").split("\n");
                            for (int i = 0; i < str.length; i++) {
                                if (!str[i].isEmpty()) {
                                    String str1 = str[i].substring(0, str[i].indexOf("<sup"));
                                    /*
                                     * Creating node for each Country extracted from the page
                                     * */
                                    String id1 = UUID.randomUUID().toString();
                                    JSONObject node = new JSONObject();
                                    node.put(this.uuid, id1);
                                    node.put(this.type, country);

                                    JSONObject jo = new JSONObject();
                                    jo.put(this.name, str1.trim());
                                    node.put(this.properties, jo);
                                    nodesArray.add(node);

                                    /*
                                     * Establishing a relationship between Country node and the movie title node
                                     * */
                                    JSONObject jo1 = new JSONObject();
                                    String relationId = UUID.randomUUID().toString();
                                    jo1.put(this.uuid, relationId);
                                    jo1.put(this.sourceNode,id);
                                    jo1.put(this.destNode, id1);
                                    jo1.put(this.relation, "released in");
                                    relationArray.add(jo1);
                                }
                            }
                        } else {
                            String str1 = data2.html();
                            String[] str = str1.replace(pattern, "").replace(pattern2, "").replace("[a-zA-Z]+[:]", ",").split("\n");
                            for (int i = 0; i < str.length; i++) {
                                if (!str[i].isEmpty()) {

                                    /*
                                     * Creating node for each Country extracted from the page
                                     * */
                                    String id1 = UUID.randomUUID().toString();
                                    JSONObject node = new JSONObject();
                                    node.put(this.uuid, id1);
                                    node.put(this.type, country);

                                    JSONObject jo = new JSONObject();
                                    jo.put(this.name, str[i].trim());
                                    node.put(this.properties, jo);
                                    nodesArray.add(node);

                                    /*
                                     * Establishing a relationship between Country node and the movie title node
                                     * */
                                    JSONObject jo1 = new JSONObject();
                                    String relationId = UUID.randomUUID().toString();
                                    jo1.put(this.uuid, relationId);
                                    jo1.put(this.sourceNode,id);
                                    jo1.put(this.destNode, id1);
                                    jo1.put(this.relation, "released in");
                                    relationArray.add(jo1);
                                }
                            }
                        }
                    }

                    //Extracting Language from Html Document
                    if (header.matches(language)) {
                        if (!data2.select("div").isEmpty()) {
                            Elements data3 = data2.select(tag);
                            String languageWiki = data3.html();
                            String[] str = languageWiki.replace(pattern, "").replace(pattern2, "").replace("[a-zA-Z]+[:]", ",").split("\n");
                            for (int i = 0; i < str.length; i++) {
                                if (!str[i].isEmpty()) {
                                    /*
                                     * Creating node for each Language extracted from the page
                                     * */
                                    String id1 = UUID.randomUUID().toString();
                                    JSONObject node = new JSONObject();
                                    node.put(this.uuid, id1);
                                    node.put(this.type, language);

                                    JSONObject jo = new JSONObject();
                                    jo.put(this.name, str[i].trim());
                                    node.put(this.properties, jo);
                                    nodesArray.add(node);

                                    /*
                                     * Establishing a relationship between Language node and the movie title node
                                     * */
                                    JSONObject jo1 = new JSONObject();
                                    String relationId = UUID.randomUUID().toString();
                                    jo1.put(this.uuid, relationId);
                                    jo1.put(this.sourceNode,id);
                                    jo1.put(this.destNode, id1);
                                    jo1.put(this.relation, language);
                                    relationArray.add(jo1);
                                }
                            }
                        } else {
                            String str1 = data2.html();
                            String[] str = str1.replace(pattern, "").replace(pattern2, "").replace("[a-zA-Z]+[:]", ",").split("\n");
                            for (int i = 0; i < str.length; i++) {
                                if (!str[i].isEmpty()) {

                                    /*
                                     * Creating node for each Language extracted from the page
                                     * */
                                    String id1 = UUID.randomUUID().toString();
                                    JSONObject node = new JSONObject();
                                    node.put(this.uuid, id1);
                                    node.put(this.type, language);

                                    JSONObject jo = new JSONObject();
                                    jo.put(this.name, str[i].trim());
                                    node.put(this.properties, jo);
                                    nodesArray.add(node);

                                    /*
                                     * Establishing a relationship between Language node and the movie title node
                                     * */
                                    JSONObject jo1 = new JSONObject();
                                    String relationId = UUID.randomUUID().toString();
                                    jo1.put(this.uuid, relationId);
                                    jo1.put(this.sourceNode,id);
                                    jo1.put(this.destNode, id1);
                                    jo1.put(this.relation, language);
                                    relationArray.add(jo1);
                                }
                            }
                        }
                    }

                    //Extracting Release Date from Html Document
                    if (header.matches("Release date")) {
                        Elements data3 = data2.select(tag);
                        String str1 = data3.html().substring(0, data3.html().indexOf("<span"));
                        String[] str = str1.replace("&nbsp;", " ").split("\n");
                        for (int i = 0; i < str.length; i++) {
                            if (!str[i].isEmpty()) {

                                /*
                                 * Creating node for release date extracted from the page
                                 * */
                                String id1 = UUID.randomUUID().toString();
                                JSONObject node = new JSONObject();
                                node.put(this.uuid, id1);
                                node.put(this.type, "Release Date");

                                JSONObject jo = new JSONObject();
                                jo.put(this.name, str[i].trim());
                                node.put(this.properties, jo);
                                nodesArray.add(node);

                                /*
                                 * Establishing a relationship between release date node and the movie title node
                                 * */
                                JSONObject jo1 = new JSONObject();
                                String relationId = UUID.randomUUID().toString();
                                jo1.put(this.uuid, relationId);
                                jo1.put(this.sourceNode,id);
                                jo1.put(this.destNode, id1);
                                jo1.put(this.relation, "released on");
                                relationArray.add(jo1);
                            }
                        }
                    }

                    //Extracting Box Office Collection from Html Document
                    if (header.matches("Box office")) {
                        if (!data2.select("span").isEmpty()) {
                            String boxOffice = data2.html();
                            String amountTemp = "";
                            StringBuilder amountTemp1 = new StringBuilder();
                            String str1 = boxOffice.substring(boxOffice.indexOf("</span>") + 7, boxOffice.indexOf("<sup")).replace("&nbsp;"," ");
                            if (data2.select("span").html().length() > 1) {
                                int len = data2.select("span").html().length();
                                String amount = data2.select("span").html().substring(1, len);
                                amountTemp = "Rs. " + amount;
                            } else {
                                amountTemp = "Rs. ";
                            }
                            amountTemp1.append(amountTemp).append(str1);
                            /*
                             * Creating node for box office extracted from the page
                             * */
                            String id1 = UUID.randomUUID().toString();
                            JSONObject node = new JSONObject();
                            node.put(this.uuid, id1);
                            node.put(this.type, "BoxOffice");

                            JSONObject jo = new JSONObject();
                            jo.put(this.name, amountTemp1);
                            node.put(this.properties, jo);
                            nodesArray.add(node);

                            /*
                             * Establishing a relationship between box office node and the movie title node
                             * */
                            JSONObject jo1 = new JSONObject();
                            String relationId = UUID.randomUUID().toString();
                            jo1.put(this.uuid, relationId);
                            jo1.put(this.sourceNode,id);
                            jo1.put(this.destNode, id1);
                            jo1.put(this.relation, "collection");
                            relationArray.add(jo1);
                        } else {
                            String boxOffice = data2.html().substring(0, data2.html().indexOf("<sup")).replace("&nbsp;"," ");
                            String id1 = UUID.randomUUID().toString();
                            /*
                             * Creating node for box office extracted from the page
                             * */
                            JSONObject node = new JSONObject();
                            node.put(this.uuid, id1);
                            node.put(this.type, "BoxOffice");

                            JSONObject jo = new JSONObject();
                            jo.put(this.name, boxOffice.trim());
                            node.put(this.properties, jo);
                            nodesArray.add(node);
                            /*
                             * Establishing a relationship between box office node and the movie title node
                             * */
                            JSONObject jo1 = new JSONObject();
                            String relationId = UUID.randomUUID().toString();
                            jo1.put(this.uuid, relationId);
                            jo1.put(this.sourceNode,id);
                            jo1.put(this.destNode, id1);
                            jo1.put(this.relation, "collection");
                            relationArray.add(jo1);
                        }
                        break;
                    }
                }

                /* creating a jsonObject contains nodesArray and relationship array*/
                JSONObject jsonDTO = new JSONObject();
                jsonDTO.put("nodes",nodesArray);
                jsonDTO.put("relationship",relationArray);
                jsonDTO.put("query", wikiurl.getQuery());
                jsonDTO.put("userId",wikiurl.getUserId());

                //producing the jsonDTO in Kafaka
                publishData(jsonDTO);
            }

            catch (IOException e) {
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
