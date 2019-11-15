package com.stackroute.analyticsservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String domain;
    private String query;
    private String result;
    private String response;
}
