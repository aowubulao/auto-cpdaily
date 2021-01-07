package daily.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Neo.Zzj
 * @date 2020/7/23
 */
public class DesUtil {

    private static final String DEFAULT_KEY = "b3L26XNL";
    private static final byte[] IV = {1, 2, 3, 4, 5, 6, 7, 8};

    private static final String CHARSET_NAME = "utf-8";
    private static final String DES = "DES";
    private static final String CIPHER_NAME = "DES/CBC/PKCS5Padding";

    private static byte[] DESEncrypt(String data, String key, byte[] iv) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), DES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(data.getBytes(CHARSET_NAME));
    }

    private static String DESDecrypt(byte[] data, String key, byte[] iv) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), DES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new String(cipher.doFinal(data), CHARSET_NAME);
    }

    private static String base64Encrypt(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private static byte[] base64Decrypt(String data) throws UnsupportedEncodingException {
        return Base64.getMimeDecoder().decode(data.getBytes(CHARSET_NAME));
    }

    public static String encode(String data) throws NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
        return base64Encrypt(DESEncrypt(data, DEFAULT_KEY, IV));
    }

    public static String decode(String data) throws UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        return DESDecrypt(base64Decrypt(data), DEFAULT_KEY, IV);
    }
}
