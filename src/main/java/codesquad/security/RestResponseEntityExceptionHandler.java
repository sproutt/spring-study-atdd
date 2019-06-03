package codesquad.security;

import codesquad.exception.EntityDeletedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = EntityDeletedException.class)
    public void handleConflict() {
        log.debug("entity is not deleted");
    }
}
