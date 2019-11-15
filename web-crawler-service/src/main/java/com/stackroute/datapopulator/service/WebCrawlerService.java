package com.stackroute.datapopulator.service;

import com.stackroute.datapopulator.domain.Input;
import com.stackroute.datapopulator.domain.WebCrawl;

import java.util.concurrent.CompletableFuture;

public interface WebCrawlerService {

    public CompletableFuture<WebCrawl> getContent(Input input);


}
