package com.stackroute.datapopulator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//Input Format(Json) which webCrawler will Consume from kafka
public class Input {

    @Id
    private String id;
    private String userId;
    private String query;
    private String domain;
    private String concept;
    private String[] url;


}