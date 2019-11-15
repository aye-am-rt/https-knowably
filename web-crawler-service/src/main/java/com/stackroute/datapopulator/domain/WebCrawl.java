package com.stackroute.datapopulator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//Output format of WebCrawler which produces to Kafka
public class WebCrawl {

    private String id;
    private String userId;
    private String domain;
    private String query;
    private String concept;
    private String url;
    private List<String> payload;
}
