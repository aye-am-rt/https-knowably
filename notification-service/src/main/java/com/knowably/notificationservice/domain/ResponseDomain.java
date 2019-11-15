package com.knowably.notificationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDomain {
    private String type;
    private List<String> value;
    private String key;
    private String status;
}
