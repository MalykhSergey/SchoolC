package general.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ElementCollection(targetClass = Teacher.class)
    @CollectionTable
    private Set<Teacher> teachers;
    @ElementCollection(targetClass = Operator.class)
    @CollectionTable
    private Set<Operator> operators;
    @ElementCollection(targetClass = SchoolClass.class)
    @CollectionTable
    private Set<SchoolClass> schoolClasses;

    public long getId() {
        return id;
    }
    public void setId(long id) {
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
