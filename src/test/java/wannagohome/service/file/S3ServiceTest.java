package wannagohome.service.file;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import wannagohome.support.SpringTest;

public class S3ServiceTest extends SpringTest {

    private static final Logger log = LoggerFactory.getLogger(S3ServiceTest.class);

    @Autowired
    S3Service s3Service;

    @Test
    public void deleteObject() {
        String link = "https://s3.ap-northeast-2.amazonaws.com/wannagohome/attachment/409bdb55-2c26-4a3e-bb50-02a79ab5aa0f_%3C.png";
        s3Service.delete(link, "attachment");
    }

}