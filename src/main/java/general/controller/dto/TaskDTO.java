package general.controller.dto;

import general.entity.Task;

public class TaskDTO {
    private Long id;
    private String teacherName;
    private String name;
    private String body;

    private String dateString;

    public TaskDTO() {
    }

    public TaskDTO(Task task) {
        id = task.getId();
        teacherName = task.getTeacher().getName();
        name = task.getName();
        body = task.getBody();
        dateString = task.getDateTemplate();
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
