package wannagohome.support;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationUtil {

    private static Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    public static <T> void assertValidate(T object, Set<Class> annotations) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        for (ConstraintViolation<T> violation : violations) {
            assertThat(annotations).contains(violation.getConstraintDescriptor().getAnnotation().annotationType());
        }
    }

    public static <T> void assertValidate(T object, List<Class> annotations) {
        assertValidate(object, new HashSet<>(annotations));
    }
}