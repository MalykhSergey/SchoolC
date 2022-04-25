package general.entities;

public enum Role {
    Admin("ROLE_ADMIN"),
    Operator("ROLE_OPERATOR"),
    Teacher("ROLE_TEACHER"),
    Student("ROLE_STUDENT");


    private String name;


    public String getName() {
        return name;
    }

    Role(String roleName) {
        this.name = roleName;
    }
}
