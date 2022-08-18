package general.entity;

import javax.persistence.*;

@Table(name = "answers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "task_id"})
)
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Student student;
    @ManyToOne(fetch = FetchType.EAGER)
    private Task task;
    @ManyToOne
    private Teacher teacher;
    private String body;
    private String comment;
    private int rating;

    public Answer() {
    }

    public Answer(Student student, Task task, String body) {
        this.student = student;
        this.task = task;
        this.body = body;
        this.teacher = task.getTeacher();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
