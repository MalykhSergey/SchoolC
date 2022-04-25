package general.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends User {
    @ManyToOne
    private SchoolClass schoolClass;
    @JoinTable(name = "student_answers")
    @OneToMany(targetEntity = Answer.class, cascade = CascadeType.ALL)
    private List<Answer> answers;

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
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

    public Student() {
    }

}
