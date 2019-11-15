package com.stackroute.datapopulator.googlesearchapiservicedemo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

//Domain object for output
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {
    @Id
    private String id;
    private String userId;
    private String query;
    private String domain;
    private String concept;
    private String[] url;
}
