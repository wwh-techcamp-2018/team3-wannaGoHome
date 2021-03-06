package wannagohome.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import wannagohome.domain.error.ErrorEntity;
import wannagohome.domain.error.ErrorType;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FileExceptionAdvisor {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorEntity> handleSizeLimitExceededException(MaxUploadSizeExceededException exception) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .errorType(ErrorType.EXCEED_FILE)
                .message("파일 첨부는 최대 15MB 까지 가능합니다.")
                .build();
        return Arrays.asList(errorEntity);
    }

    @ExceptionHandler(EmptyFileUploadedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorEntity> handleEmptyFileException(EmptyFileUploadedException exception) {
        return Arrays.asList(exception.entity());
    }

    @ExceptionHandler(UnsupportedFileFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorEntity> handleFileFormatException(UnsupportedFileFormatException exception) {
        return Arrays.asList(exception.entity());
    }
}
