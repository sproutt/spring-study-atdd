package codesquad;

public class NullEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NullEntityException() {
        super();
    }

    public NullEntityException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NullEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullEntityException(String message) {
        super(message);
    }

    public NullEntityException(Throwable cause) {
        super(cause);
    }
}
