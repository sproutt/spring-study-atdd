package codesquad.exception;

public class QuestionNotFoundException extends RuntimeException {

    public QuestionNotFoundException() {
    }

    public QuestionNotFoundException(String message) {
        super(message);
    }
}
