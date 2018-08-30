package wannagohome.service.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService implements UploadService {

    @Autowired
    public S3Service s3Service;

    public static final String ATTACHMENT_PATH = "attachment";

    @Override
    public String fileUpload(MultipartFile multipartFile) {
        return s3Service.upload(multipartFile, ATTACHMENT_PATH);
    }

    @Override
    public void fileDelete(String link) {
        s3Service.delete(link, ATTACHMENT_PATH);
    }

}
