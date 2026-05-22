import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class EncryptDecrypt {
    public static void main(String[] args) {
        String plainText = "www.baeldung.com";
        String password = "baeldung";
        String salt = "12345678";
        GCMParameterSpec gcmParameterSpec = AESUtil.generateIv();
        SecretKey key = AESUtil.getKeyFromPassword(password,salt);
        String cipherText = AESUtil.encryptPasswordBased(plainText, key, gcmParameterSpec);
        String decryptedCipherText = AESUtil.decryptPasswordBased(
                cipherText, key, gcmParameterSpec);String plainText = "www.baeldung.com";
        String password = "baeldung";
        String salt = "12345678";
        GCMParameterSpec gcmParameterSpec = AESUtil.generateIv();
        SecretKey key = AESUtil.getKeyFromPassword(password,salt);
        String cipherText = AESUtil.encryptPasswordBased(plainText, key, gcmParameterSpec);
        String decryptedCipherText = AESUtil.decryptPasswordBased(
                cipherText, key, gcmParameterSpec);
    }
}
