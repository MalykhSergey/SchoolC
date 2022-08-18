package general.controller.dto;

public class SchoolDTO {
    private String schoolName;

    public SchoolDTO() {
    }

    public SchoolDTO(String name) {
        this.schoolName = name;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
