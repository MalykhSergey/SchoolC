package general.entities;

import javax.persistence.Entity;
import java.util.Collection;

@Entity
public class Operator extends User{
    public Operator(){}
    public Operator(String name, String password, Collection<Role> roles, School school){
    setName(name);
    setPassword(password);
    setRoles(roles);
    setSchool(school);
    }
}
