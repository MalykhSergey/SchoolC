package general.entities;

import javax.persistence.*;

@Entity
public class TaskStatusOfStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(targetEntity = Student.class, cascade = CascadeType.ALL)
    private Student student;
    @ManyToOne(targetEntity = Task.class, cascade = CascadeType.ALL)
    private Task task;
    private int mark;

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    private String status;

    public TaskStatusOfStudent() {
    }

    public TaskStatusOfStudent(Student student, Task task, String status) {
        this.student = student;
        this.task = task;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
