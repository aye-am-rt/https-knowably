package com.stackroute.nlpmicroservice.services;

import com.stackroute.nlpmicroservice.domain.Medical;
import com.stackroute.nlpmicroservice.model.MedicalDictionary;
import edu.stanford.nlp.util.CoreMap;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class MedicalAnalyzerService {
    private static final Logger LOGGER= LoggerFactory.getLogger(MedicalAnalyzerService.class);

    private MedicalDictionary medicalDictionary;
    private Medical medical;

    @Autowired
    public MedicalAnalyzerService(MedicalDictionary medicalDictionary,Medical medical) {
        this.medicalDictionary = medicalDictionary;
        this.medical=medical;
    }

    void extractInfo(String concept, CoreMap sentence, LinkedHashMap<String, List<String>> mapOfEachWordProps,
                  LinkedHashMap<String, String> entInSentence) {
        medical.setDisease(concept);
        HashMap<Integer, String> matchedPoints = new HashMap<>();
        ArrayList<Integer> indices = new ArrayList<>();
        medicalDictionary.medicalInitializer();
        int index;

        for (int i = 0; i < medicalDictionary.getSymptoms().size(); i++) {
            index = sentence.toString().toLowerCase().indexOf(medicalDictionary.getSymptoms().get(i).toLowerCase());
            if (index != -1) {
                matchedPoints.put(index, "symptom");
                indices.add(index);
            }
        }
        for (int i = 0; i < medicalDictionary.getMedicine().size(); i++) {
            index = sentence.toString().toLowerCase().indexOf(medicalDictionary.getMedicine().get(i).toLowerCase());
            if (index != -1) {
                matchedPoints.put(index, "medicine");
                indices.add(index);
            }
        }
        for (int i = 0; i < medicalDictionary.getCauses().size(); i++) {
            index = sentence.toString().toLowerCase().indexOf(medicalDictionary.getCauses().get(i).toLowerCase());
            if (index != -1) {
                matchedPoints.put(index, "causes");
                indices.add(index);
            }
        }
        for (int i = 0; i < medicalDictionary.getDeathCount().size(); i++) {
            index = sentence.toString().toLowerCase().indexOf(medicalDictionary.getDeathCount().get(i).toLowerCase());
            if (index != -1) {
                matchedPoints.put(index, "deathCount");
                indices.add(index);
            }
        }
        for (int i = 0; i < medicalDictionary.getTreatment().size(); i++) {
            index = sentence.toString().toLowerCase().indexOf(medicalDictionary.getTreatment().get(i).toLowerCase());
            if (index != -1) {
                matchedPoints.put(index, "treatment");
                indices.add(index);
            }
        }
        matchedPoints.put(sentence.toString().length(), "EOS");
        indices.add(sentence.toString().length());
        LinkedHashSet<Integer> hashSet1 = new LinkedHashSet<>(indices);
        indices = new ArrayList<>(hashSet1);
        Collections.sort(indices);


        for (int i = 0; i < indices.size() - 1; i++) {
            if (matchedPoints.get(indices.get(i)).equalsIgnoreCase("symptom"))
            {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                ArrayList<String> exc= new ArrayList<>(Arrays.asList("DT","VB", "VBZ", "VBP", "VBD", "VBN", "VBG"));

                String[] splits = subStr.split(" ");

                for (int j = 1; j < splits.length; j++)
                {
                    try {
                         if (mapOfEachWordProps.get(splits[j]).get(0).equals("NN"))
                        { medical.getSymptoms().add(splits[j]); }
                        else if(exc.contains(mapOfEachWordProps.get(splits[j]).get(0)))
                        { break; }
                    }catch (Exception ex)
                    { LOGGER.info("caught ex in symptoms "); }
                }
            }
            if (matchedPoints.get(indices.get(i)).equalsIgnoreCase("medicine"))
            {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                ArrayList<String> exc= new ArrayList<>(Arrays.asList("VBZ", "VBP", "VBD", "VBN", "VBG"));
                ArrayList<String> inc= new ArrayList<>(Arrays.asList("NN", "FW","JJ"));
                String[] splits = subStr.split(" ");
                for (int j = 1; j < splits.length; j++)
                {
                    try {
                        if (inc.contains(mapOfEachWordProps.get(splits[j]).get(0)))
                        { medical.getMedicine().add(splits[j]); }
                        else if(exc.contains(mapOfEachWordProps.get(splits[j]).get(0)))
                        { break; }
                    }catch (Exception ex)
                    { LOGGER.info(ex.getMessage()); }
                }
            }
            if (matchedPoints.get(indices.get(i)).equalsIgnoreCase("causes"))
            {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                ArrayList<String> exc= new ArrayList<>(Arrays.asList("VBZ", "VBP", "VBD", "VBN", "VBG"));
                ArrayList<String> inc= new ArrayList<>(Arrays.asList("NN", "FW"));
                String[] splits = subStr.split(" ");
                for (int j = 1; j < splits.length; j++)
                {
                    try {
                        if (inc.contains(mapOfEachWordProps.get(splits[j]).get(0)))
                        { medical.getCauses().add(splits[j]); }
                        else if(exc.contains(mapOfEachWordProps.get(splits[j]).get(0)))
                        { break; }
                    }catch (Exception ex)
                    { LOGGER.info(ex.getMessage()); }
                }
            }

            if (matchedPoints.get(indices.get(i)).equalsIgnoreCase("deathCount"))
            {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                List<String> l=new ArrayList<>(entInSentence.keySet());
                for (String s : l) {
                    if (entInSentence.get(s).equalsIgnoreCase("NUMBER")) {
                        medical.getDeathCount().add(s); }
                }
            }
            if (matchedPoints.get(indices.get(i)).equalsIgnoreCase("treatment"))
            {
                String subStr = sentence.toString().substring(indices.get(i), indices.get(i + 1));
                ArrayList<String> exc= new ArrayList<>(Arrays.asList("VBZ", "VBP", "VBD", "VBN", "VBG","IN"));
                ArrayList<String> inc= new ArrayList<>(Arrays.asList("NN", "FW","JJ"));
                String[] splits = subStr.split(" ");
                for (int j = 1; j < splits.length; j++)
                {
                    try {
                        if (inc.contains(mapOfEachWordProps.get(splits[j]).get(0)))
                        { medical.getTreatment().add(splits[j]); }
                        else if(exc.contains(mapOfEachWordProps.get(splits[j]).get(0)))
                        { break; }
                    }catch (Exception ex)
                    { LOGGER.info(ex.getMessage()); }
                }
            }
        }
        matchedPoints.clear();
        indices.clear();
    }
}
