package general.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Student extends User{
    @ManyToOne
    private  SchoolClass schoolClass;
    public Student() {
    }


    public Student(String user, String password, Collection<Role> roles , School school) {
        this.setName(user);
        this.setPassword(password);
        this.setRoles(roles);
        this.setSchool(school);
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }
    
}
