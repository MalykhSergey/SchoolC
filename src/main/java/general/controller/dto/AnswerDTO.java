package general.controller.dto;

import general.entity.Answer;

public class AnswerDTO {
    private TaskDTO taskDTO;
    private Long id;
    private String body;
    private String comment;
    private int rating;

    public AnswerDTO(Answer answer) {
        id = answer.getId();
        taskDTO = new TaskDTO(answer.getTask());
        body = answer.getBody();
        comment = answer.getComment();
        rating = answer.getRating();
    }

    public TaskDTO getTaskDTO() {
        return taskDTO;
    }

    public void setTaskDTO(TaskDTO taskDTO) {
        this.taskDTO = taskDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
