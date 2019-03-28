package codesquad;

public class AlreadyDeletedException extends Exception{
    public AlreadyDeletedException(String message){
        super(message);
    }
}
