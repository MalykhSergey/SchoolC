package general.controller.dto;

import java.util.List;

public class StudentsTasksAndAnswers {
    private List<TaskDTO> actualTaskDTOS;
    private List<TaskDTO> oldTaskDTOS;
    private List<AnswerDTO> answerDTOS;

    public StudentsTasksAndAnswers(List<TaskDTO> actualTaskDTOS, List<TaskDTO> oldTaskDTOS, List<AnswerDTO> answerDTOS) {
        this.actualTaskDTOS = actualTaskDTOS;
        this.oldTaskDTOS = oldTaskDTOS;
        this.answerDTOS = answerDTOS;
    }

    public List<TaskDTO> getActualTaskDTOS() {
        return actualTaskDTOS;
    }

    public void setActualTaskDTOS(List<TaskDTO> actualTaskDTOS) {
        this.actualTaskDTOS = actualTaskDTOS;
    }

    public List<TaskDTO> getOldTaskDTOS() {
        return oldTaskDTOS;
    }

    public void setOldTaskDTOS(List<TaskDTO> oldTaskDTOS) {
        this.oldTaskDTOS = oldTaskDTOS;
    }

    public List<AnswerDTO> getAnswerDTOS() {
        return answerDTOS;
    }

    public void setAnswerDTOS(List<AnswerDTO> answerDTOS) {
        this.answerDTOS = answerDTOS;
    }
}
