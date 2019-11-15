package com.stackroute.nlpmicroservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Movie")
public class Movie {

    @Id
    private String id;
    private String domain;
    private String movieName;
    private List<String> casts=new ArrayList<>();
    private List<String> producers= new ArrayList<>();
    private List<String> directors= new ArrayList<>();
    private List<String> collection= new ArrayList<>();
    private List<String> releaseDate= new ArrayList<>();
    private List<String> productionHouse= new ArrayList<>();
    private List<String> country= new ArrayList<>();
}
