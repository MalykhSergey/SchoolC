package general.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Entity
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    private School school;
    @OneToMany(targetEntity = Task.class, cascade = CascadeType.ALL)
    private Set<Task> tasks;
    @OneToMany(targetEntity = Student.class, cascade = CascadeType.ALL)
    private Set<Student> students;

    public Set<Student> getStudents() {
        return students;
    }


    public void addStudent(Student student) {
        this.students.add(student);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public School getSchool() {
        return school;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<Task>(this.tasks);
        return tasks;
    }

    public void setSchool(School school) {
        this.school = school;
    }
    public void addTask(Task task){
        this.tasks.add(task);
    }
}
