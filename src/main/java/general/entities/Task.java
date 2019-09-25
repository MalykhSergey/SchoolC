/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author dmali
 */
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String bodyString;
    @ManyToOne
    SchoolClass schoolClass;

    public Task() {
    }

    public Task(String nameString,String bodyString, SchoolClass schoolClass) {
        this.name = nameString;
        this.schoolClass = schoolClass;
        this.bodyString = bodyString;
    }

    public long getId() {
        return id;
    }

    public String getBodyString() {
        return bodyString;
    }

    public void setBodyString(String bodyString) {
        this.bodyString = bodyString;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameString() {
        return name;
    }

    public void setNameString(String nameString) {
        this.name = nameString;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }
    
}
