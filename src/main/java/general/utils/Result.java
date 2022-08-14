package general.utils;

public class Result<T> {
    private boolean bool;
    private T result;

    public Result(boolean bool, T result) {
        this.bool = bool;
        this.result = result;
    }

    public boolean isDataValid() {
        return bool;
    }

    public T getResult() {
        return result;
    }
}
