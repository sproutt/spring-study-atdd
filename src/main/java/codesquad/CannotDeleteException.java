package codesquad;

public class CannotDeleteException extends Exception {
    private static final long serialVersionUID = 1L;

    public CannotDeleteException() {
    }

    public CannotDeleteException(String message) {
        super(message);
    }
}