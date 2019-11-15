package com.knowably.notificationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainData {
    private String type;
    private List<String> value;
    private String key;
}
