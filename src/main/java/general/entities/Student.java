package general.entities;

import general.reposes.TaskStatusOfStudentRepos;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    public Student() {
    }

    public Student(String user, String password, Collection<Role> roles, School school, SchoolClass schoolClass) {
        this.setName(user);
        this.setPassword(password);
        this.setRoles(roles);
        this.setSchool(school);
        this.setSchoolClass(schoolClass);
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

}
