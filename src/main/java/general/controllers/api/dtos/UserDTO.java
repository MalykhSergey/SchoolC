package general.controllers.api.dtos;

import general.controllers.forms.ClassForm;
import general.controllers.forms.SchoolForm;
import general.controllers.forms.UserForm;
import general.entities.Role;

public class UserDTO {
    private String userName;
    private String password;
    private Role role;
    private String schoolName;
    private String className;

    private Integer classNumber;

    private Long classId;

    public UserDTO(String userName, String password, Role role, String schoolName, String className, Integer classNumber, Long classId) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.schoolName = schoolName;
        this.className = className;
        this.classNumber = classNumber;
        this.classId = classId;
    }

    public UserDTO(UserForm userForm, ClassForm classForm){
        this.userName = userForm.getUserName();
        this.password = userForm.getPassword();
        this.role = userForm.getRole();
        this.className = classForm.getClassName();
        this.classNumber = classForm.getClassNumber();
        this.classId = classForm.getClassId();
    }

    public UserDTO(UserForm userForm, SchoolForm schoolForm, ClassForm classForm){
        this.userName = userForm.getUserName();
        this.password = userForm.getPassword();
        this.role = userForm.getRole();
        this.schoolName = schoolForm.getSchoolName();
        this.className = classForm.getClassName();
        this.classNumber = classForm.getClassNumber();
        this.classId = classForm.getClassId();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}
