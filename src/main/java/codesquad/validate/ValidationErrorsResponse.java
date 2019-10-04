package codesquad.validate;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ValidationErrorsResponse extends RestStatus {

    private List<ValidationError> errors;

    public ValidationErrorsResponse() {
        super(false);
        errors = new ArrayList<>();
    }

    public void addValidationError(ValidationError error) {
        errors.add(error);
    }

}
