package com.stackroute.nlpmicroservice.model;

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
public class MedicalDictionary {
    List<String> symptoms = new ArrayList<>();
    List<String> medicine= new ArrayList<>();
    List<String> causes= new ArrayList<>();
    List<String> deathCount= new ArrayList<>();
    List<String> treatment= new ArrayList<>();

    public void medicalInitializer()
    {
        symptoms.add("symptom");
        symptoms.add("shows");
        symptoms.add("cause of");
        symptoms.add("manifestation");
        symptoms.add("expression");
        symptoms.add("indication");
        symptoms.add("sign");
        symptoms.add("characteristic");
        symptoms.add("include");

        medicine.add("medicine");
        medicine.add("medication");
        medicine.add("drug");
        medicine.add("antibiotic");
        medicine.add("cure");
        medicine.add("medication");
        medicine.add("pharmaceutical");
        medicine.add("prescription");
        medicine.add("remedy");
        medicine.add("capsule");
        medicine.add("tablet");


        causes.add("cause by");
        causes.add("lead");
        causes.add("condition");

        deathCount.add("death");
        deathCount.add("death count");
        deathCount.add("die");
        deathCount.add("kill");
        deathCount.add("casualty");
        deathCount.add("fatality");
        deathCount.add("suffer");


        treatment.add("therapy");
        treatment.add("healing");
        treatment.add("operation");
        treatment.add("diagnose");
        treatment.add("test");
        treatment.add("analyze");
        treatment.add("treatment");




    }
}
