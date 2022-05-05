package general.entities;

import java.util.Set;
import javax.persistence.*;

@Table(name = "classes")
@Entity
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int classNumber;
    @ManyToOne
    private School school;
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.REMOVE)
    private Set<Task> tasks;
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.REMOVE)
    private Set<Student> students;

    @ManyToMany
    @JoinTable(
            name = "teacher_classes",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<Teacher> teachers;

    public SchoolClass(String name, int classNumber, School school) {
        this.name = name;
        this.classNumber = classNumber;
        this.school = school;
    }

    public SchoolClass() {

    }

    public Set<Student> getStudents() {
        return students;
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

    public String getNameWithNumber() {
        return classNumber + "-" + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public School getSchool() {
        return school;
    }

    public Set<Task> getTasks() {
        return tasks;
    }
    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean fastEqualsById(SchoolClass schoolClass) {
        return (this.id.equals(schoolClass.getId())) ? true : false;
    }
}
