package general.entities;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private School school;
    private Role role;

    public User(String name, String password, School school, Role role) {
        this.name = name;
        this.password = password;
        this.school = school;
        this.role = role;
    }

    public User() {
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Role getRole() {
        return role;
    }

    public void setRoles(Role role) {
        this.role = role;
    }

    public boolean fastEqualsById(User user) {
        return (this.id.equals(user.getId())) ? true : false;
    }
}
