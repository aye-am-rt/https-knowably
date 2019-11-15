package com.stackroute.nlpmicroservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SitesContent {
    private String id;
    private String userId;
    private String domain;
    private String query;
    private String concept;
    private String url;
    private List<String> payload;

}
