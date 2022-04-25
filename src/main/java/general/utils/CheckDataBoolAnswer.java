package general.utils;

public class CheckDataBoolAnswer {
    private boolean bool;
    private String answer;

    public CheckDataBoolAnswer(boolean bool, String answer) {
        this.bool = bool;
        this.answer = answer;
    }

    public boolean isTrue() {
        return bool;
    }

    public String getAnswer() {
        return answer;
    }
}
