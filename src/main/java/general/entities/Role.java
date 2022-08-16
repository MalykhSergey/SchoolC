package general.entities;

public enum Role {
    Admin("ROLE_ADMIN"),
    Operator("ROLE_OPERATOR"),
    Teacher("ROLE_TEACHER"),
    Student("ROLE_STUDENT");


    private final String name;


    Role(String roleName) {
        this.name = roleName;
    }

    public String getName() {
        return name;
    }

    public User createUserByRole(String userName, String password, School school, SchoolClass schoolClass) {
        return switch (this) {
            case Student -> new Student(userName, password, school, schoolClass);
            case Teacher -> new Teacher(userName, password, school);
            default -> new User(userName, password, school, this);
        };
    }
}
