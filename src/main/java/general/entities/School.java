package general.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "schools")
@Entity
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OrderBy("classNumber, name")
    @OneToMany(mappedBy = "school", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<SchoolClass> schoolClasses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchoolClasses(Set<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    public Set<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

    public School() {
    }

    public School(String name) {
        this.name = name;
    }

}
