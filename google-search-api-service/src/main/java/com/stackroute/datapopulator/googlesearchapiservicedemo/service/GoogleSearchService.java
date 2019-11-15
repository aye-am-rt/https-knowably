package com.stackroute.datapopulator.googlesearchapiservicedemo.service;

import com.stackroute.datapopulator.googlesearchapiservicedemo.domain.Input;
import com.stackroute.datapopulator.googlesearchapiservicedemo.domain.SearchResult;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GoogleSearchService {
    public void saveCache(Input input);
    public boolean checkCache(String[] concept);
    public CompletableFuture<SearchResult> getLinks(String query1, String userId, String domain, String concept) throws IOException, ParseException;
}
