package general.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("teacher")
public class Teacher extends User{

    @ManyToMany
    private Set<SchoolClass> schoolClassSet;

    public Set<SchoolClass> getSchoolClassSet() {
        return schoolClassSet;
    }

    public void addSchoolClassSet(SchoolClass schoolClass) {
        schoolClassSet.add(schoolClass);
    }
}
