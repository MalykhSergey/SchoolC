package general.entities;

import javax.persistence.*;

@Entity
@DiscriminatorValue("student")
public class Student extends User{

    @ManyToOne
    private  SchoolClass schoolClass;

}
