package wannagohome.component;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AES256Encoder implements PasswordEncoder {
    private String iv;
    private Key keySpec;

    public AES256Encoder(String key) throws UnsupportedEncodingException {
        this.iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        this.keySpec = keySpec;
    }

    private String encrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));
        return enStr;
    }

    private String decrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        return new String(c.doFinal(byteStr), "UTF-8");
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            return encrypt(rawPassword.toString());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return decrypt(encodedPassword).equals(rawPassword.toString());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }
}