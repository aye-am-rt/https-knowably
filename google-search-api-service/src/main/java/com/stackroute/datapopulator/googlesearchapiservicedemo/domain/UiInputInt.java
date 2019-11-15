package com.stackroute.datapopulator.googlesearchapiservicedemo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Domain object for input as received from Internal Pipeline Trigger
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UiInputInt {
    private String query;
    private String userId;
    private String domain;
    private String concept;
}
