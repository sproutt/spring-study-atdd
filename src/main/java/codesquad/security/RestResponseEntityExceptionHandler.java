package codesquad.security;

import codesquad.exception.QuestionDeletedException;
import codesquad.exception.QuestionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class RestResponseEntityExceptionHandler{
    private static final Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {QuestionDeletedException.class, QuestionNotFoundException.class})
    public void handleConflict() {
    log.debug("entity is not found");
        }
}
