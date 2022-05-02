package general.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Table(name = "classes")
@Entity
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private School school;
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.REMOVE)
    private Set<Task> tasks;
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.REMOVE)
    private Set<Student> students;

    public SchoolClass(String name, School school) {
        this.name = name;
        this.school = school;
    }

    public SchoolClass() {

    }

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

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public boolean fastEqualsById(SchoolClass schoolClass) {
        return (this.id.equals(schoolClass.getId())) ? true : false;
    }
}
