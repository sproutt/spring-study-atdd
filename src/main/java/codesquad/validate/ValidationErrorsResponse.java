package codesquad.validate;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorsResponse extends RestStatus {
    private final List<ValidationError> errors;

    public ValidationErrorsResponse() {
        super(false);
        errors = new ArrayList<>();
    }

    public void addValidationError(ValidationError error) {
        errors.add(error);
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
