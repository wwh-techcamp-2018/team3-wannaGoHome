package wannagohome.service.file;

import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.error.ErrorType;
import wannagohome.exception.EmptyFileUploadedException;

public interface UploadService {

    String fileUpload(MultipartFile multipartFile);

    void fileDelete(String link);

    default void assertFileFormat(MultipartFile multipartFile) {
    }

    default void assertEmptyFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new EmptyFileUploadedException(ErrorType.EMPTY_FILE, "파일이 첨부되지 않았습니다.");
        }
    }

}
