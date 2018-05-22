package io.javabrains.springbootstarter.DerbyControllerImplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CranController {

    @Autowired
    private CranService cran_obj;

    @RequestMapping("cran_db/getAll")
    public List<Cran> getCranAllDocuments(){
        return cran_obj.getAllTopics();
    }

    @RequestMapping("cran_db/{id}")
    public Cran getCranDocById(@PathVariable String id){
        return cran_obj.getDocById(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cran_doc")
    public void addDocById(@RequestBody Cran cran){
        cran_obj.addToCran(cran);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cran_doc/{id}")
    public void addDocById(@RequestBody Cran cran, @PathVariable String id){
        cran_obj.updateTopic(id,cran);
    }
}
