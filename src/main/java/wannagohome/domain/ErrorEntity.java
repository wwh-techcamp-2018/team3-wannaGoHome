package wannagohome.domain;

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

    public String getErrorType() {
        log.debug("error entity type: {}", errorType.getErrorType());
        return errorType.getErrorType();
    }

    public boolean sameErrorType(ErrorType type) {
        return errorType == type;
    }
}
