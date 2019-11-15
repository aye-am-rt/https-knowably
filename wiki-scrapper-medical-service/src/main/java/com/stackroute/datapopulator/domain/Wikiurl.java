package com.stackroute.datapopulator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wikiurl {
    private String id;
    private String userId;
    private String query;
    private String domain;
    private String concept;
    private String url;
}
