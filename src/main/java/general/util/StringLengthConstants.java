package general.util;

public enum StringLengthConstants {
    ClassName(1,10),
    SchoolName(10,55),
    Name(5, 55),
    Password(5, 20),
    TaskName(5, 50),
    TaskBody(15, 2000),
    AnswerBody(5, 5000),
    Comment(0,500);

    private final int minLength;
    private final int maxLength;
    private final String lengthDescription;

    StringLengthConstants(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        lengthDescription = "длинее " + minLength + " и короче " + maxLength + " символов.";
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String getLengthDescription() {
        return lengthDescription;
    }
}
