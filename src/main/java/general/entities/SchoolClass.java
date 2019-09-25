package general.entities;

import java.util.List;
import javax.persistence.*;

@Entity
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    School school;
    @OneToMany(targetEntity = Task.class, cascade = CascadeType.ALL)
    List<Task> tasks;

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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
    public void addTask(Task task){
        this.tasks.add(task);
    }
}
