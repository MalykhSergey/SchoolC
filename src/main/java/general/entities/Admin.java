package general.entities;

import javax.persistence.Entity;
import java.util.Collection;

@Entity
public class Admin extends User{
    public Admin(){}
    public Admin(String name, String password, Collection<Role> roles, School school) {
        setName(name);
        setPassword(password);
        setRoles(roles);
        setSchool(school);
    }
    public Admin(String name, String password, Collection<Role> roles) {
        setName(name);
        setPassword(password);
        setRoles(roles);
    }
}
