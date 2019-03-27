package codesquad.exception;

public class EntityDeletedException extends RuntimeException {

    public EntityDeletedException() {
    }

    public EntityDeletedException(String message) {
        super(message);
    }
}
