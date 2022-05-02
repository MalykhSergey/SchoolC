package general.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends User {
    @ManyToOne(cascade = CascadeType.PERSIST)
    private SchoolClass schoolClass;
    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private List<Answer> answers;

    public Student() {
    }

    public Student(String user, String password, Role role, School school, SchoolClass schoolClass) {
        super(user, password, school, role);
        this.answers = new ArrayList<>();
        this.setSchoolClass(schoolClass);
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }


    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
