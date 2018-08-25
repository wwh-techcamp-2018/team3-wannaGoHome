package wannagohome.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationUtil {
    private static final Logger log = LoggerFactory.getLogger(ValidationUtil.class);

    private static Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    public static <T> void assertValidate(T object, Set<Class> annotations) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        for (ConstraintViolation<T> violation : violations) {
            log.debug("validation error message : {}", violation.getMessage());
            log.debug("validation error invalidValue : {}", violation.getInvalidValue());
            assertThat(annotations).contains(violation.getConstraintDescriptor().getAnnotation().annotationType());
        }
    }

    public static <T> void assertValidate(T object, List<Class> annotations) {
        assertValidate(object, new HashSet<>(annotations));
    }
}