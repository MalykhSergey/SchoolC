package general.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
@Table(name = "tasks")
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinTable(name = "task_answers")
    @OneToMany(targetEntity = Answer.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;
    private String name;
    @Column(length = 2000)
    private String body;
    private Timestamp timeStamp;
    @ManyToOne
    private SchoolClass schoolClass;

    public Task() {
        answers = new ArrayList<>();
    }

    public Task(String nameString,String body, SchoolClass schoolClass, Timestamp timeStamp) {
        this.name = nameString;
        this.schoolClass = schoolClass;
        this.body = body;
        this.timeStamp = timeStamp;
        answers = new ArrayList<>();
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
        String time = getTimeStamp().toLocaleString();
        return time.substring(0,time.length()-3);
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }
    public boolean fastEqualsById(Task task){
        if (this.id.equals(task.getId())){
            return true;
        }
        return false;
    }
}
