package wannagohome.service.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.error.ErrorType;
import wannagohome.exception.UnsupportedFileFormatException;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Service {


    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    private final AmazonS3Client amazonS3Client;

    @Transactional
    public String upload(MultipartFile multipartFile, String dirName) {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new UnsupportedFileFormatException(ErrorType.UNSUPPORTED_FILE
                        ,"MultipartFile을 File로 전환이 실패했습니다."));
        return upload(uploadFile, dirName);
    }

    private Optional<File> convert(MultipartFile file) {
        File convertFile = new File(file.getOriginalFilename());
        try {
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            throw new UnsupportedFileFormatException(ErrorType.UNSUPPORTED_FILE, "파일 형식이 잘못됐습니다.");
        }
        return Optional.empty();
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + getUUIDFileName(uploadFile.getName());
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String getUUIDFileName(String fileName) {
        UUID uuid = UUID.randomUUID();
        return UUID.randomUUID().toString() + "_" + fileName;
    }


    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(BUCKET_NAME, fileName).toString();
    }

    private boolean removeNewFile(File targetFile) {
        return targetFile.delete();
    }

    public void delete(String link, String dirName) {

        amazonS3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, getKey(link, dirName)));
    }

    private String getKey(String link, String dirName) {
        try {
            String decodeLink = URLDecoder.decode(link, "UTF-8");
            return decodeLink.substring(decodeLink.indexOf(dirName), decodeLink.length());
        }
        catch (UnsupportedEncodingException e) {
            throw new UnsupportedFileFormatException(ErrorType.UNSUPPORTED_FILE, "파일 링크 잘못됐습니다.");
        }
    }

}
