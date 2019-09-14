package general.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ElementCollection(targetClass = Teacher.class)
    @CollectionTable
    private Set<Teacher> teachers;
    @ElementCollection(targetClass = SchoolClass.class)
    @CollectionTable
    private Set<SchoolClass> schoolClasses;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
