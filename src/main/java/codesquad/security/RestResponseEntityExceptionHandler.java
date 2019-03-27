package codesquad.security;

import codesquad.exception.EntityDeletedException;
import codesquad.exception.QuestionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class RestResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {EntityDeletedException.class, QuestionNotFoundException.class})
    public void handleConflict() {
        log.debug("entity is not found");
    }
}
