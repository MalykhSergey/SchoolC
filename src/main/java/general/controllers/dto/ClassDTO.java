package general.controllers.dto;

public class ClassDTO {
    private String className;
    private Integer classNumber;
    private Long classId;

    public ClassDTO() {
    }

    public ClassDTO(String className, Integer classNumber, Long classId) {
        this.className = className;
        this.classNumber = classNumber;
        this.classId = classId;
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
