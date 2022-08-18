package general.entity;

import java.util.List;

public class UserRatingDTO {

    private String userName;

    private List<Object[]> answers;

    public UserRatingDTO(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Object[]> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Object[]> answers) {
        this.answers = answers;
    }
}
