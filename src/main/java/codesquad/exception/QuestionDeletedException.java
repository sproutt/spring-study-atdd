package codesquad.exception;

public class QuestionDeletedException extends RuntimeException {

    public QuestionDeletedException() {
    }

    public QuestionDeletedException(String message) {
        super(message);
    }
}
