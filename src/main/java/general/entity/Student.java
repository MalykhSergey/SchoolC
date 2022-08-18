package general.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Student extends User {
    @ManyToOne
    private SchoolClass schoolClass;
    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Answer> answers;

    public Student() {
    }

    public Student(String user, String password, School school, SchoolClass schoolClass) {
        super(user, password, school, Role.Student);
        this.answers = new HashSet<>();
        this.setSchoolClass(schoolClass);
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }


    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}
