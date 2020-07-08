package general.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Student extends User {
    @ManyToOne
    private SchoolClass schoolClass;
    @OneToMany(targetEntity = Answer.class, cascade = CascadeType.ALL)
    private List<Answer> answers;

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }
    
    public Student(String user, String password, Collection<Role> roles, School school, SchoolClass schoolClass) {
        super(user, password, school, roles);
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
