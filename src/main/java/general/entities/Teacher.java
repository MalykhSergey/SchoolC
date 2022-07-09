package general.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Teacher extends User {
    @OrderBy("classNumber,name")
    @ManyToMany
    @JoinTable(
            name = "teacher_classes",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private Set<SchoolClass> schoolClassSet;

    public Set<SchoolClass> getSchoolClassSet() {
        return schoolClassSet;
    }

    public void addSchoolClass(SchoolClass schoolClass) {
        schoolClassSet.add(schoolClass);
    }

    public Teacher(String name, String password, School school) {
        super(name, password, school, Role.Teacher);
        schoolClassSet = new HashSet<>();
    }

    public Teacher() {
        schoolClassSet = new HashSet<>();
    }
}
