package com.stackroute.nlpmicroservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectDomains {

    private List<Medical> allCollectedMedicalObjects=new ArrayList<>();
    private List<Movie> allCollectedMovieObjects=new ArrayList<>();
}
