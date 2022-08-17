package general.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Table(name = "tasks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "school_class_id"})
)
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 2000)
    private String body;
    private Timestamp timeStamp;
    @ManyToOne
    private SchoolClass schoolClass;
    @ManyToOne
    private Teacher teacher;
    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Answer> answers;

    public Task() {
    }

    public Task(String nameString, String body, SchoolClass schoolClass, Teacher teacher, Timestamp timeStamp) {
        this.name = nameString;
        this.schoolClass = schoolClass;
        this.body = body;
        this.teacher = teacher;
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String nameString) {
        this.name = nameString;
    }

    public String getDateTemplate() {
        String time = getTimeStamp().toLocaleString();
        return time.substring(0, time.length() - 3);
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}
