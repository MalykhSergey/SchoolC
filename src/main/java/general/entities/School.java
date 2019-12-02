package general.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(targetEntity = Teacher.class, cascade = CascadeType.ALL)
    private Set<Teacher> teachers;
    @OneToMany(targetEntity = Operator.class, cascade = CascadeType.ALL)
    private Set<Operator> operators;
    @OneToMany(targetEntity = SchoolClass.class, cascade = CascadeType.ALL)
    private Set<SchoolClass> schoolClasses;

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

    public Set<Operator> getOperators() {
        return operators;
    }

    public void setOperators(Set<Operator> operators) {
        this.operators = operators;
    }
    public void addOperator(Operator operator){
    this.operators.add(operator);
    }
}
