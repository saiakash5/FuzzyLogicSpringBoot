package io.javabrains.springbootstarter.DerbyControllerImplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CranService {

    @Autowired
    private CranRepository cranRepository;

    public List<Cran> getAllTopics(){
//        return cran_docs;
        List<Cran> cran_docs = new ArrayList<>();
        cranRepository.findAll().forEach(cran_docs :: add);
        return cran_docs;
    }

    //to add document to Cran collection
    public void addToCran(Cran cran){
        cranRepository.save(cran);
    }

    //to update an existing Cran Docuement with id
    public void updateTopic(String id,Cran cran){
        cranRepository.save(cran);
    }

    //get document by ID
    public Cran getDocById(String id){
        return cranRepository.findOne(id);
    }

    //delete an existing docuement in Cran
    public void deleteCran(String id){
        cranRepository.delete(id);
    }
}
