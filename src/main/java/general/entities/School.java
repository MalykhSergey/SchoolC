package general.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "schools")
@Entity
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @JoinTable(name = "schools_teachers")
    @OneToMany(targetEntity = Teacher.class, cascade = CascadeType.ALL)
    private Set<Teacher> teachers;
    @JoinTable(name = "schools_operators")
    @OneToMany(targetEntity = User.class, cascade = CascadeType.ALL)
    private Set<User> operators;
    @JoinTable(name = "schools_classes")
    @OneToMany(targetEntity = SchoolClass.class, cascade = CascadeType.ALL)
    private Set<SchoolClass> schoolClasses = new HashSet<>();

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

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setSchoolClasses(Set<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    public Set<SchoolClass> getSchoolClasses() {
        return schoolClasses;

    }
    public void addClass(SchoolClass schoolClass){
        schoolClasses.add(schoolClass);
    }

    public void addTeacher(Teacher teacher){teachers.add(teacher);}

    public School() {
    }

    public School(String name) {
        this.name = name;
    }

    public Set<User> getOperators() {
        return operators;
    }

    public void setOperators(Set<User> operators) {
        this.operators = operators;
    }
    public void addOperator(User operator){
    this.operators.add(operator);
    }

}
