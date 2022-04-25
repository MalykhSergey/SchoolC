package general.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Teacher extends User{
    @JoinTable(name = "teacher_classes")
    @ManyToMany
    private Set<SchoolClass> schoolClassSet;
    
    public Set<SchoolClass> getSchoolClassSet() {
        return schoolClassSet;
    }

    public void addSchoolClass(SchoolClass schoolClass) {
        schoolClassSet.add(schoolClass);
    }

    public Teacher(String name, String password, School school, Role role) {
        super(name, password, school, role);
        schoolClassSet = new HashSet<>();
    }

    public Teacher() {
        schoolClassSet = new HashSet<>();
    }
}
