package codesquad.domain;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerValidationTest {
    private static final Logger log = LoggerFactory.getLogger(AnswerValidationTest.class);

    private static Validator validator;

    @BeforeClass
    public static void setup(){
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator= validatorFactory.getValidator();
    }

    @Test
    public void empty_content() throws Exception{
        Answer answer = new Answer();
        Set<ConstraintViolation<Answer>> constraintViolations = validator.validate(answer);
        assertThat(constraintViolations.size()).isEqualTo(1);
    }
}
