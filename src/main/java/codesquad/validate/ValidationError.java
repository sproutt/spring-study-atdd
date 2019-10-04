package codesquad.validate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ValidationError {

    private String fieldName;

    private String errorMessage;

}
