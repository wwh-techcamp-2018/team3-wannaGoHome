package wannagohome.component;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


public class AES256Encoder implements PasswordEncoder {
    private String iv;
    private Key keySpec;

    public AES256Encoder(String key) throws UnsupportedEncodingException {
        this.iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int length = b.length;
        if (length > keyBytes.length) {
            length = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, length);
        this.keySpec = new SecretKeySpec(keyBytes, "AES");
    }

    private String encrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted = cipher.doFinal(str.getBytes("UTF-8"));
        return new String(Base64.encodeBase64(encrypted));
    }

    private String decrypt(String str) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        return new String(cipher.doFinal(byteStr), "UTF-8");
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            return encrypt(rawPassword.toString());
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return decrypt(encodedPassword).equals(rawPassword.toString());
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }
}