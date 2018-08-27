package wannagohome.domain.error;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorEntity {
    private static final Logger log = LoggerFactory.getLogger(ErrorEntity.class);

    private ErrorType errorType;
    private String message;

    public void setErrorType(String errorType) {
        this.errorType = ErrorType.of(errorType);
    }

    public boolean sameErrorType(ErrorType type) {
        return errorType == type;
    }
}
