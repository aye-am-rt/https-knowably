package com.stackroute.datapopulator.googlesearchapiservicedemo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Domain object for input as received from Front End
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UiInput {
    private String userId;
    private String domain;
    private String concept;
}
