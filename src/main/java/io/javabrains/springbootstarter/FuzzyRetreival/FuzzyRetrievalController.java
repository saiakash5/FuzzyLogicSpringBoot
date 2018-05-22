package io.javabrains.springbootstarter.FuzzyRetreival;

import io.javabrains.springbootstarter.RetreivalServicePackage.RetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

@RestController
public class FuzzyRetrievalController {

    @Autowired
    private RetrievalService main_obj;

    @RequestMapping("/inverted-index")
    public TreeMap<String, TreeMap<Integer, Integer>> getRSV20() {
        return main_obj.getInverted_Index();
    }

    @RequestMapping("/term-frequency")
    public TreeMap<Integer, Integer> getTermFreq() {
        return main_obj.getTerm_frequency();
    }

    @RequestMapping("/cran")
    public HashMap<Integer, String> getCran() {
        return main_obj.getDoc();
    }

    @RequestMapping("cran/{id}")
    public HashMap<Integer, String> getCranById(@PathVariable String id) throws IOException {
        return main_obj.getById(id);
    }

    @RequestMapping("/process/{query}")
    public HashMap<Integer, String> getResQuery(@PathVariable String query) {
        return main_obj.getResult(query);
    }

}
