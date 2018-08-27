package wannagohome.service.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.error.ErrorType;
import wannagohome.exception.EmptyFileUploadedException;
import wannagohome.exception.UnsupportedFileFormatException;

import javax.transaction.Transactional;

@Service
public class ImageUploadService {
    @Autowired
    private S3Service s3Service;

    public static final String PROFILE_PATH = "profile" ;
    public static final String IMAGE_FILE_PREFIX = "image";


    @Transactional
    public String fileUpload(MultipartFile multipartFile){
        assertEmptyFile(multipartFile);
        assertFileFormat(multipartFile);
        return s3Service.upload(multipartFile, PROFILE_PATH);
    }

    public void assertFileFormat(MultipartFile multipartFile) {
        if (!multipartFile.getContentType().contains(IMAGE_FILE_PREFIX)) {
            throw new UnsupportedFileFormatException(ErrorType.UNSUPPORTED_FILE, "이미지 파일 형식만 업로드가 가능합니다.");
        }
    }

    public void assertEmptyFile(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) {
            throw new EmptyFileUploadedException(ErrorType.EMPTY_FILE, "파일이 첨부되지 않았습니다.");
        }
    }
}
