package general.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private Student student;
    @ManyToOne
    private Task task;
    @CollectionTable
    @ElementCollection
    private List<String> fileNames;
    private String body;
    public Answer() {
    }

    public Answer(Student student, Task task) {
        this.fileNames = new ArrayList<>();
        this.student = student;
        this.task = task;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public List<String> getFilename() {
        return fileNames;
    }

    public void addFileName(String filename) {
        this.fileNames.add(filename);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
