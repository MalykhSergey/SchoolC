/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.entities;

import javax.persistence.*;
import java.util.*;

/**
 *
 * @author dmali
 */
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(targetEntity = Answer.class, cascade = CascadeType.ALL)
    private List<Answer> answers;
    private String name;
    @Column(length = 2000)
    private String body;
    private Calendar date;
    @ManyToOne
    private SchoolClass schoolClass;

    public Task() {
    }

    public Task(String nameString,String body, SchoolClass schoolClass, Calendar date) {
        this.name = nameString;
        this.schoolClass = schoolClass;
        this.body = body;
        this.date =date;
    }

    public Long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nameString) {
        this.name = nameString;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }


    public String getDateTemplate() {
        return Integer.toString(date.get(Calendar.DAY_OF_MONTH))+":"+Integer.toString(date.get(Calendar.MONTH) + 1)+":"+Integer.toString(date.get(Calendar.YEAR));
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }
}
