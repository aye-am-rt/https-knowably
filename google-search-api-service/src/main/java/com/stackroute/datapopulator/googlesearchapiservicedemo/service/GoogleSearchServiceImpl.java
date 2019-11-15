package com.stackroute.datapopulator.googlesearchapiservicedemo.service;

import com.stackroute.datapopulator.googlesearchapiservicedemo.domain.Cache;
import com.stackroute.datapopulator.googlesearchapiservicedemo.domain.Input;
import com.stackroute.datapopulator.googlesearchapiservicedemo.domain.SearchResult;
import com.stackroute.datapopulator.googlesearchapiservicedemo.repository.CacheRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpTimeoutException;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@PropertySource("classpath:bootstrap.properties")
public class GoogleSearchServiceImpl implements GoogleSearchService {
    //Fetching value of url from external file
    @Value("${googleapi.url}")
    private String url1;

    private CacheRepository cacheRepository;
    @Autowired
    public GoogleSearchServiceImpl(CacheRepository cacheRepository)
    {
        this.cacheRepository = cacheRepository;
    }
    //Function to save to cache memory
    @Override
    public void saveCache(Input input) {
        Cache cache = new Cache();
        cache.setUserId(input.getUserId());
        cache.setConcepts(input.getConcepts());
        cache.setDomain(input.getDomain());
        cache.setTimestamp(LocalDateTime.now());
        cacheRepository.save(cache);
    }

    @Override
    public boolean checkCache(String[] concept) {
        if(cacheRepository.findByConcepts(concept)!=null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    @Async
    public CompletableFuture<SearchResult> getLinks(String query1, String userId, String domain, String concept) throws IOException, ParseException {
        //The domain and concept are searched for using the Google Custom Search API
        SearchResult searchResult = new SearchResult();
            String query =  domain + " " + concept +" page";
            String link = url1 + "wikipedia:" + URLEncoder.encode(query,"UTF-8") + "&fields=items/link";
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int resp = connection.getResponseCode();
            if(resp!=200)
            {
                throw new HttpTimeoutException("HTTP Response Code :" + resp);
            }
            else
            {
                //Reading the obtained values from API Call
                Scanner sc = new Scanner(url.openStream());
                String text="";
                while(sc.hasNext())
                {
                    text+=sc.nextLine();
                }
                sc.close();
                //Writing output as a JSON object
                JSONParser parse = new JSONParser();
                JSONObject jobj = (JSONObject)parse.parse(text);
                JSONArray arr = (JSONArray)jobj.get("items");
                String[] links = new String[10];
                for(int j=0;j<arr.size();j++)
                {
                    JSONObject jobj2 = (JSONObject)arr.get(j);
                    links[j] = (String) jobj2.get("link");
                }
                //Separating the output if we directly get a wikipedia page
                if(links[0].contains("en.wikipedia.org"))
                {
                    searchResult.setId(UUID.randomUUID().toString());
                    searchResult.setUserId(userId);
                    searchResult.setConcept(concept);
                    searchResult.setDomain(domain);
                    String[] links2 = new String[10];
                    links2[0] = links[0];
                    searchResult.setUrl(links2);
                    searchResult.setQuery(query1);
                }
                else
                {
                    //For queries that do not get a wikipedia page directly, 10 links are returned which the Web Crawler uses.
                    String link2 = url + URLEncoder.encode(query,"UTF-8") + "&fields=items/link";
                    url = new URL(link2);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    resp = connection.getResponseCode();
                    if(resp!=200)
                    {
                        throw new HttpTimeoutException("HTTP Response Code :" + resp);
                    }
                    else {
                        sc = new Scanner(url.openStream());
                        String text2 = "";
                        while (sc.hasNext()) {
                            text2 += sc.nextLine();
                        }
                        sc.close();
                        parse = new JSONParser();
                        jobj = (JSONObject) parse.parse(text);
                        arr = (JSONArray) jobj.get("items");
                        String[] links2 = new String[10];
                        for (int j = 0; j < arr.size(); j++) {
                            JSONObject jobj2 = (JSONObject) arr.get(j);
                            links2[j] = (String) jobj2.get("link");
                        }
                        searchResult.setId(UUID.randomUUID().toString());
                        searchResult.setUserId(userId);
                        searchResult.setConcept(concept);
                        searchResult.setDomain(domain);
                        searchResult.setUrl(links2);
                        searchResult.setQuery(query1);
                    }
                }
            }
        return CompletableFuture.completedFuture(searchResult);
    }
}
