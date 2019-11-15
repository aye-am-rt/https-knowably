package com.stackroute.nlpmicroservice.services;

import com.stackroute.nlpmicroservice.domain.Medical;
import com.stackroute.nlpmicroservice.repository.MedicalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MedicalDbService {
    private static final Logger LOGGER= LoggerFactory.getLogger(MedicalDbService.class);

    @Autowired
    private MedicalRepository  medicalRepository;

    @Autowired
    public MedicalDbService(MedicalRepository medicalRepository){this.medicalRepository=medicalRepository;}

    public Medical saveMedical(Medical medicalObj) throws Exception {
        if(medicalRepository.existsById(medicalObj.getId()))
        {
            throw new Exception ("Medical id already exists");
        }
        return medicalRepository.save(medicalObj);
    }

    List<Medical> getAllMedicals() {
        return medicalRepository.findAll();
    }
    public void deleteMedicalsData() {
        medicalRepository.deleteAll();
    }

    public Medical getMedicalById(String id) throws Exception {
        List<Medical> allMedicalList=medicalRepository.findAll();
        Medical findOneMedical=new Medical();
        if(medicalRepository.existsById(id))
        {
            for (Medical medical : allMedicalList) {
                if (medical.getId().equals(id)) {
                    findOneMedical = medical;
                }
            }
        }
        else
        {
            throw new Exception("Medical Object does not exists with given id= "+id);
        }
        return findOneMedical;
    }
}
