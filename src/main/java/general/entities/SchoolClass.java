package general.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "classes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "class_number", "school_id"})
)
@Entity
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "class_number")
    private int classNumber;
    @ManyToOne
    private School school;
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Task> tasks;
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Student> students;

    @ManyToMany(fetch = FetchType.LAZY)
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
        this.students = new HashSet<>();
    }

    public SchoolClass() {

    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        students.add(student);
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

    public String getNameWithNumber() {
        return classNumber + "-" + name;
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

    public void setSchool(School school) {
        this.school = school;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public boolean fastEqualsById(SchoolClass schoolClass) {
        return this.id.equals(schoolClass.getId());
    }
}
