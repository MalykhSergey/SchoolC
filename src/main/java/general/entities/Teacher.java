package general.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
public class Teacher extends User{

    @ManyToMany
    private Set<SchoolClass> schoolClassSet;

    public Set<SchoolClass> getSchoolClassSet() {
        return schoolClassSet;
    }

    public void addSchoolClass(SchoolClass schoolClass) {
        schoolClassSet.add(schoolClass);
    }

    public Teacher() {
    }
    public Teacher(String user, String password, Collection<Role> roles, School school ) {
        this.setName(user);
        this.setPassword(password);
        this.setRoles(roles);
        this.setSchool(school);
    }
}
