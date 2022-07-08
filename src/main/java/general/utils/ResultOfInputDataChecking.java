package general.utils;

public class ResultOfInputDataChecking {
    private boolean bool;
    private String result;

    public ResultOfInputDataChecking(boolean bool, String result) {
        this.bool = bool;
        this.result = result;
    }

    public boolean isDataValid() {
        return bool;
    }

    public String getResult() {
        return result;
    }
}
