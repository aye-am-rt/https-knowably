package com.stackroute.queryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalOutput {
    private String query;
    private String userId;
    private String domain;
    private String concept;
}
