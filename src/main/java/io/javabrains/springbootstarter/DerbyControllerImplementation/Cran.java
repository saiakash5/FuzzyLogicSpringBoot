package io.javabrains.springbootstarter.DerbyControllerImplementation;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cran {

    @Id
    private String id;


    private String content;

    public Cran(){

    }

    public Cran(String id, String content) {
        super();
        this.id = id;
        this.content = content;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
