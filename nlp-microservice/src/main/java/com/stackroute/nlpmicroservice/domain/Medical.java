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
@Document(collection = "Medical")
public class Medical {
    @Id
    private String id;
    private String domain;
    private String disease;
    private List<String> symptoms=new ArrayList<>();
    private List<String> medicine=new ArrayList<>();
    private List<String> causes=new ArrayList<>();
    private List<String> deathCount=new ArrayList<>();
    private List<String> treatment=new ArrayList<>();

}
