package wannagohome.domain.error;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorEntity {
    private ErrorType errorType;
    private String message;

    public void setErrorType(String errorType) {
        this.errorType = ErrorType.of(errorType);
    }

    public boolean sameErrorType(ErrorType type) {
        return errorType == type;
    }
}
